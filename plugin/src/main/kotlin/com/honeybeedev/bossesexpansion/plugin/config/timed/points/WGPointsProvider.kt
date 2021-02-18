package com.honeybeedev.bossesexpansion.plugin.config.timed.points

import com.google.common.collect.Sets
import com.honeybeedev.bossesexpansion.plugin.util.getChunkPairsBetween
import com.honeybeedev.bossesexpansion.plugin.util.logger
import com.oop.orangeengine.main.task.StaticTask
import com.oop.orangeengine.yaml.ConfigSection
import org.apache.commons.lang.mutable.MutableBoolean
import org.bukkit.ChunkSnapshot
import org.bukkit.Location
import org.bukkit.World
import org.codemc.worldguardwrapper.WorldGuardWrapper
import org.codemc.worldguardwrapper.region.IWrappedRegion
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ThreadLocalRandom
import java.util.stream.IntStream

class WGPointsProvider(val section: ConfigSection) : PointsProvider(section) {
    val points: MutableSet<Location> = Sets.newConcurrentHashSet()

    val world: World
    val maxPoint: Location
    val minPoint: Location
    var region: IWrappedRegion

    private val regionName: String = section.getAs("value")

    init {
        region = WorldGuardWrapper.getInstance().regions
            .firstOrNull { it.id.toString().contentEquals(regionName) }
            ?: throw IllegalStateException("Failed to find region by $regionName")

        world = region.selection.world
        maxPoint =
            Location(world, region.maximumPoint.x, region.maximumPoint.y, region.maximumPoint.z)
        minPoint =
            Location(world, region.minimumPoint.x, region.minimumPoint.y, region.minimumPoint.z)
    }

    override fun provide(parallel: Boolean, amount: Int): CompletableFuture<Collection<Location>> {
        points.clear()
        val future = CompletableFuture<Collection<Location>>()

        StaticTask.getInstance().ensureSync {
            logger.printDebug("Loading spawn points for region $regionName")

            // Get & Load all the chunks
            val chunkPairsBetween = getChunkPairsBetween(
                maxPoint, minPoint
            )

            val snapshots: MutableList<ChunkSnapshot> = arrayListOf()
            chunkPairsBetween.forEach {
                snapshots.add(
                    world.getChunkAt(it.first, it.second).getChunkSnapshot(true, false, false)
                )
            }

            val start = System.currentTimeMillis()
            val mBreak = MutableBoolean(false)

            val executer: () -> Unit = {
                IntStream
                    .rangeClosed(0, amount)
                    .parallel()
                    .forEach {
                        if (mBreak.booleanValue()) return@forEach
                        for (tried in 0..((snapshots.size / amount) * 2)) {
                            if (mBreak.booleanValue()) return@forEach

                            val chunk = snapshots[ThreadLocalRandom.current()
                                .nextInt(0, snapshots.size - 1)]

                            val randomX = ThreadLocalRandom.current().nextInt(0, 16)
                            val randomZ = ThreadLocalRandom.current().nextInt(0, 16)

                            val highestBlockYAt = chunk.getHighestBlockYAt(randomX, randomZ)
                            var underType =
                                chunk.getBlockType(randomX, highestBlockYAt - 1, randomZ)

                            if (!underType.isSolid)
                                continue

                            var y = highestBlockYAt
                            val adder: () -> Unit = {
                                val worldX = (chunk.x * 16) + randomX
                                val worldZ = (chunk.z * 16) + randomZ

                                synchronized(mBreak) {
                                    val location = Location(
                                        world,
                                        worldX.toDouble(),
                                        y.toDouble(),
                                        worldZ.toDouble()
                                    )
                                    if (!region.contains(location)) return@synchronized

                                    if (!mBreak.booleanValue() && !points.contains(location)) {
                                        points.add(location)

                                        if (points.size == amount) {
                                            mBreak.setValue(true)
                                            logger.printDebug("Done! ${System.currentTimeMillis() - start}ms for $regionName")
                                            future.complete(points)
                                        }
                                    }
                                }
                            }

                            val isLeaves = underType.name.contains("LEAVES")
                            if (isLeaves) {
                                var currentY = highestBlockYAt - 1

                                for (a in 0..255) {
                                    currentY -= 1
                                    if (currentY == 0) break
                                    val blockType = chunk.getBlockType(randomX, currentY, randomZ)
                                    if (!blockType.isSolid || blockType.name.contains("LEAVES"))
                                        continue

                                    y = currentY + 1
                                }
                            } else {
                                adder()
                                continue
                            }

                            if (y == highestBlockYAt && isLeaves) continue
                            underType = chunk.getBlockType(randomX, y - 1, randomZ)

                            if (!underType.isSolid)
                                continue

                            adder()
                        }
                    }
            }

            if (parallel)
                StaticTask.getInstance().async(executer)
            else
                executer()
        }
        return future
    }
}
