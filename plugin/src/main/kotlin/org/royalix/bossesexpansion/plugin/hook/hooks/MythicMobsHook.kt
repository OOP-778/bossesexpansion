package org.royalix.bossesexpansion.plugin.hook.hooks

import com.oop.orangeengine.main.events.SyncEvents.listen
import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDespawnEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.event.EventPriority
import org.royalix.bossesexpansion.api.BossesExpansionAPI
import org.royalix.bossesexpansion.plugin.hook.Hook
import java.util.function.Function

open class MythicMobsHook : Hook("MythicMobs") {
    init {
        val provider = BossProvider
        org.royalix.bossesexpansion.plugin.BossesExpansion.instance!!.bossProviderController.registerProvider(
            provider
        )

        // Listen for spawn
        listen(MythicMobSpawnEvent::class.java, EventPriority.LOWEST) {
            BossesExpansionAPI.getPlugin().bossController.onSpawn(
                it.entity,
                it.mob.parent?.entity?.bukkitEntity,
                provider
            )
        }

        // Listen for death
        listen(MythicMobDeathEvent::class.java, EventPriority.LOWEST) {
            BossesExpansionAPI.getPlugin().bossController.onDeath(it.entity, provider)
        }

        // Listen for despawn
        listen(MythicMobDespawnEvent::class.java, EventPriority.LOWEST) {
            it.apply {
                BossesExpansionAPI.getPlugin().bossController.onDespawn(it.entity)
            }
        }
    }

    private object BossProvider : org.royalix.bossesexpansion.api.boss.BossProvider {
        override fun listBosses(): Set<String> = HashSet(MythicMobs.inst().mobManager.mobNames)

        override fun provideSpawner(bossName: String): Function<Location, Entity>? {
            val mythicMobName = listBosses()
                .firstOrNull { it.equals(bossName, true) } ?: return null
            return MythicMobs.inst().apiHelper.getMythicMob(mythicMobName)?.let { mob ->
                return Function {
                    MythicMobs.inst().apiHelper.spawnMythicMob(mob, it, 1)
                }
            }
        }

        override fun getDisplayName(entity: Entity?): String? {
            return MythicMobs.inst().apiHelper.getMythicMobInstance(entity)?.displayName
                ?: getInternalName(entity)
        }

        override fun getInternalName(entity: Entity?): String? {
            return MythicMobs.inst().apiHelper.getMythicMobInstance(entity)?.type?.internalName
        }

        override fun getIdentifier(): String = "MythicMobs"
    }
}
