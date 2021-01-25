package com.honeybeedev.bossesexpansion.plugin.controller

import com.google.common.collect.Maps
import com.honeybeedev.bossesexpansion.api.BossesExpansionAPI
import com.honeybeedev.bossesexpansion.api.boss.Boss
import com.honeybeedev.bossesexpansion.api.boss.BossProvider
import com.honeybeedev.bossesexpansion.api.controller.BossController
import com.honeybeedev.bossesexpansion.api.event.BossChildrenSpawnEvent
import com.honeybeedev.bossesexpansion.api.event.BossDeathEvent
import com.honeybeedev.bossesexpansion.api.event.BossDespawnEvent
import com.honeybeedev.bossesexpansion.api.event.BossSpawnEvent
import com.honeybeedev.bossesexpansion.plugin.boss.BBoss
import com.honeybeedev.bossesexpansion.plugin.event.BEventDispatcher
import com.honeybeedev.bossesexpansion.plugin.util.BEComponent
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import java.util.*

class BossController : BossController, BEComponent {
    val bosses = Maps.newConcurrentMap<UUID, BBoss>()

    override fun getBosses(): MutableSet<Boss> {
        return mutableSetOf(bosses.values) as MutableSet<Boss>
    }

    override fun onDeath(entity: Entity, provider: BossProvider) {
        val onFullDeath: (BBoss) -> Unit = {
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
        if (parent != null) {
            bosses[parent.uniqueId]?.let {
                logger.printDebug("New Boss Children spawned of $it")
                (BossesExpansionAPI.getPlugin().eventDispatcher as BEventDispatcher).call(
                    BossChildrenSpawnEvent()
                )
                it.children.add(entity)
            }
            return
        }

        val boss = BBoss(entity, provider)
        logger.printDebug("New Boss Spawned $boss")
        (BossesExpansionAPI.getPlugin().eventDispatcher as BEventDispatcher).call(
            BossSpawnEvent(boss as Boss)
        )

        bosses[entity.uniqueId] = boss
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