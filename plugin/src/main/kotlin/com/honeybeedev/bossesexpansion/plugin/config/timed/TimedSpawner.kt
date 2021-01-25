package com.honeybeedev.bossesexpansion.plugin.config.timed

import com.honeybeedev.bossesexpansion.plugin.BossesExpansion
import com.honeybeedev.bossesexpansion.plugin.config.timed.points.PointsProvider
import com.honeybeedev.bossesexpansion.plugin.config.timed.points.WGPointsProvider
import com.honeybeedev.bossesexpansion.plugin.util.NullableVar
import com.honeybeedev.bossesexpansion.plugin.util.WrappedNumber
import com.honeybeedev.bossesexpansion.plugin.util.leftComplex
import com.honeybeedev.bossesexpansion.plugin.util.prepareError
import com.oop.orangeengine.main.task.StaticTask
import com.oop.orangeengine.yaml.Config
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*

class TimedSpawner(val config: Config) {
    var timeLoader = TimeLoader(config)
    var grouping: String? = null
    var broadcasts: MutableSet<TimedMessage> = mutableSetOf()

    var executingAt by NullableVar<ZonedDateTime>()
    var bosses: MutableSet<Pair<WrappedNumber, String>> = mutableSetOf()
    var pointsProvider: PointsProvider? = null

    init {
        if (config.isSectionPresent("cache")) {
            val section = config.getSection("cache").get()
            val lastHash = section.getAs<Int>("lasthash")
            val lastTimer = section.getAs<Int>("timer")

            if (timeLoader.hash == lastHash) {
                executingAt = ZonedDateTime.ofInstant(Instant.ofEpochSecond(lastTimer.toLong()), timeLoader.zone!!)
                if (Duration.between(timeLoader.current(), executingAt).seconds <= 0)
                    executingAt = timeLoader.next()
            }
        } else {
            executingAt = timeLoader.next()

            val cache = config.createSection("cache")
            cache.comments.addAll(arrayListOf("Do not delete this", "This section holds data of time"))

            cache.set("lasthash", timeLoader.hash)
            cache.set("timer", executingAt!!.toEpochSecond())
        }

        config.ifSectionPresent("points") {
            val type = it.getAs<String>("type")
            if (type.equals("worldguard", true)) {
                pointsProvider = WGPointsProvider(it)
            } else
                throw prepareError(it, "Invalid points provider")
        }

        config.ifSectionPresent("grouping") {
            if (it.getAs("enabled"))
                grouping = it.getAs("lead")
        }

        config.ifSectionPresent("broadcasts") {
            it.sections.values.forEach { broadcastSection ->
                broadcasts.add(TimedMessage(broadcastSection))
            }
        }

        config.ifValuePresent("bosses", List::class.java) {
            it.forEach {
                bosses.add(parse(it.toString()))
            }
        }
    }

    fun left(): String =
        leftComplex(Duration.between(timeLoader.current(), executingAt).seconds)

    fun tick() {
        val runningIn = Duration.between(timeLoader.current(), executingAt).seconds

        broadcasts.forEach {
            if (it.beforeSeconds == runningIn) {
                it.message.send(null, null)
            }
        }

        if (runningIn <= 1) {
            executingAt = timeLoader.next()
            pointsProvider?.let {
                val bossesSpawning = bosses
                    .map { Pair(it.first.getter(), it.second) }

                val spotsRequired = bossesSpawning
                    .map { it.first }
                    .sum()

                it.provide(true, spotsRequired).whenComplete { spots, t ->
                    val spotsQueue = LinkedList(spots)
                    StaticTask.getInstance().ensureSync {
                        for (bossPair in bossesSpawning) {
                            val spawner = BossesExpansion.instance!!
                                ._bossProviderController
                                .findSpawnProvider(bossPair.second) ?: continue

                            for (num in 0 until bossPair.first) {
                                val poll = spotsQueue.poll()
                                spawner(poll)
                            }
                        }
                    }
                }
            }
        }
    }
}