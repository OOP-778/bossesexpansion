package org.royalix.bossesexpansion.plugin.boss

import com.google.common.collect.Maps
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.royalix.bossesexpansion.api.BossesExpansionAPI
import org.royalix.bossesexpansion.api.boss.Boss
import org.royalix.bossesexpansion.api.boss.BossController
import org.royalix.bossesexpansion.api.boss.BossProvider
import org.royalix.bossesexpansion.api.event.boss.BossChildrenSpawnEvent
import org.royalix.bossesexpansion.api.event.boss.BossDeathEvent
import org.royalix.bossesexpansion.api.event.boss.BossDespawnEvent
import org.royalix.bossesexpansion.api.event.boss.BossSpawnEvent
import org.royalix.bossesexpansion.api.event.boss.pre.BossPreSpawnEvent
import org.royalix.bossesexpansion.plugin.event.BEventDispatcher
import org.royalix.bossesexpansion.plugin.util.PluginComponent
import org.royalix.bossesexpansion.plugin.util.executeTask
import java.util.*

class BossController : BossController, PluginComponent {
    val bosses = Maps.newConcurrentMap<UUID, org.royalix.bossesexpansion.plugin.boss.BBoss>()

    override fun getBosses(): MutableSet<Boss> {
        return mutableSetOf(bosses.values) as MutableSet<Boss>
    }

    override fun onDeath(entity: Entity, provider: BossProvider) {
        val onFullDeath: (org.royalix.bossesexpansion.plugin.boss.BBoss) -> Unit = {
            bosses.remove(it.entity.uniqueId)
            (BossesExpansionAPI.getPlugin().eventDispatcher as BEventDispatcher).call(
                BossDeathEvent(it)
            )
        }

        bosses[entity.uniqueId]?.let {
            logger.printDebug("boss death")
            if (it.children.isEmpty())
                onFullDeath(it)
        }
            ?: bosses.values
                .firstOrNull { it.children.contains(entity) }
                ?.let {
                    it.children.remove(entity)
                    logger.printDebug("children death")
                    if (it.children.isEmpty() && (it.entity as LivingEntity).health <= 0)
                        onFullDeath(it)
                }
    }

    override fun onSpawn(entity: Entity, parent: Entity?, provider: BossProvider) {
        // Split the spawning into two parts, first of all wait half of second and call preSpawnEvent
        // Then wait half of second and call spawnEvent

        executeTask("delayed-pre-spawn-event-handling", delay = 500) {
            val boss = org.royalix.bossesexpansion.plugin.boss.BBoss(entity, provider)
            val preSpawnEvent = BossPreSpawnEvent(boss)

            // Call the pre spawn event
            plugin.plugin.eventDispatcher.call(preSpawnEvent)

            // If prespawn event is cancelled, we do not handle this entity
            if (preSpawnEvent.isCancelled)
                return@executeTask

            executeTask("delayed-spawn-event-handling", delay = 500) spawntask@{
                if (parent != null) {
                    bosses[parent.uniqueId]?.let {
                        logger.printDebug("New Boss Children spawned of $it")
                        (BossesExpansionAPI.getPlugin().eventDispatcher as BEventDispatcher).call(
                            BossChildrenSpawnEvent()
                        )
                        it.children.add(entity)
                    }
                    return@spawntask
                }

                logger.printDebug("New Boss Spawned $boss")
                (BossesExpansionAPI.getPlugin().eventDispatcher as BEventDispatcher).call(
                    BossSpawnEvent(boss as Boss)
                )

                bosses[entity.uniqueId] = boss
            }
        }
    }

    override fun onDespawn(entity: Entity) {
        bosses[entity.uniqueId]?.let {
            logger.printDebug("boss death")
            if (it.children.isEmpty()) {
                bosses.remove(entity.uniqueId)
                (BossesExpansionAPI.getPlugin().eventDispatcher as BEventDispatcher).call(
                    BossDespawnEvent()
                )
            }

        }
            ?: bosses.values
                .firstOrNull { it.children.contains(entity) }
                ?.let {
                    it.children.remove(entity)
                    if (it.children.isEmpty())
                        (BossesExpansionAPI.getPlugin().eventDispatcher as BEventDispatcher).call(
                            BossDespawnEvent()
                        )
                }
    }

    override fun getBoss(entity: Entity): Boss? =
        getBoss(entity.uniqueId)

    override fun getBoss(uuid: UUID): Boss? =
        bosses[uuid] ?: bosses
            .values
            .firstOrNull { !it.children.none { e -> e.uniqueId == uuid } }
}
