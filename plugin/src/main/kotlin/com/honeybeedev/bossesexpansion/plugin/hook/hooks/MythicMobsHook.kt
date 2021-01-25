package com.honeybeedev.bossesexpansion.plugin.hook.hooks

import com.honeybeedev.bossesexpansion.api.BossesExpansionAPI
import com.honeybeedev.bossesexpansion.plugin.BossesExpansion
import com.honeybeedev.bossesexpansion.plugin.hook.Hook
import com.honeybeedev.bossesexpansion.plugin.util.executeTask
import com.oop.orangeengine.main.events.SyncEvents.listen
import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDespawnEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.event.EventPriority
import java.util.function.Consumer

open class MythicMobsHook : Hook("MythicMobs") {
    init {
        val provider = BossProvider
        BossesExpansion.instance!!.bossProviderController.registerProvider(provider)

        // Listen for spawn
        listen(MythicMobSpawnEvent::class.java, EventPriority.LOWEST) {
            it.apply {
                executeTask("DelayedMythicMobsSpawn", 50) { _ ->
                    BossesExpansionAPI.getPlugin().bossController.onSpawn(
                        it.entity,
                        it.mob.parent?.entity?.bukkitEntity,
                        provider
                    )
                }
            }
        }

        // Listen for death
        listen(MythicMobDeathEvent::class.java, EventPriority.LOWEST) {
            it.apply {
                executeTask("DelayedMythicMobsDeath", 50) { _ ->
                    BossesExpansionAPI.getPlugin().bossController.onDeath(it.entity, provider)
                }
            }
        }

        // Listen for despawn
        listen(MythicMobDespawnEvent::class.java, EventPriority.LOWEST) {
            it.apply {
                BossesExpansionAPI.getPlugin().bossController.onDespawn(it.entity)
            }
        }
    }

    private object BossProvider : com.honeybeedev.bossesexpansion.api.boss.BossProvider {
        override fun listBosses(): Set<String> = HashSet(MythicMobs.inst().mobManager.mobNames)

        override fun provideSpawner(bossName: String): Consumer<Location>? {
            val mythicMobName = MythicMobs.inst().mobManager.mobNames
                .firstOrNull() { it.equals(bossName, true) } ?: return null
            return MythicMobs.inst().apiHelper.getMythicMob(mythicMobName)?.let { mob ->
                return Consumer {
                    MythicMobs.inst().apiHelper.spawnMythicMob(mob, it, 1)
                }
            }
        }

        override fun getDisplayName(entity: Entity?): String? {
            return MythicMobs.inst().apiHelper.getMythicMobInstance(entity)?.displayName ?: getInternalName(entity)
        }

        override fun getInternalName(entity: Entity?): String? {
            return MythicMobs.inst().apiHelper.getMythicMobInstance(entity)?.type?.internalName
        }

        override fun getIdentifier(): String = "MythicMobs"
    }
}