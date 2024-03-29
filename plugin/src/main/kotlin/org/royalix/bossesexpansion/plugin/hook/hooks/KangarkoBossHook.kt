package org.royalix.bossesexpansion.plugin.hook.hooks

import com.oop.orangeengine.main.events.SyncEvents
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.event.EventPriority
import org.mineacademy.boss.api.BossAPI
import org.mineacademy.boss.api.BossSpawnReason
import org.mineacademy.boss.api.event.BossDeathEvent
import org.mineacademy.boss.api.event.BossPostSpawnEvent
import org.royalix.bossesexpansion.api.BossesExpansionAPI
import org.royalix.bossesexpansion.plugin.hook.Hook
import java.util.function.Function

class KangarkoBossHook : Hook("Boss") {
    init {
        val provider = BossProvider
        org.royalix.bossesexpansion.plugin.BossesExpansion.instance!!.bossProviderController.registerProvider(
            provider
        )

        // Listen for spawn
        SyncEvents.listen(BossPostSpawnEvent::class.java, EventPriority.LOWEST) {
            BossesExpansionAPI.getPlugin().bossController.onSpawn(
                it.entity,
                null,
                provider
            )
        }

        // Listen for death
        SyncEvents.listen(BossDeathEvent::class.java, EventPriority.LOWEST) {
            BossesExpansionAPI.getPlugin().bossController.onDeath(it.entity, provider)
        }
    }

    private object BossProvider : org.royalix.bossesexpansion.api.boss.BossProvider {
        override fun listBosses(): Set<String> = HashSet(BossAPI.getValidTypes().map { it.name })

        override fun provideSpawner(bossName: String): java.util.function.Function<Location, Entity>? {
            val _bossName = listBosses()
                .firstOrNull { it.equals(bossName, true) } ?: return null
            return Function {
                BossAPI.getBoss(_bossName).spawn(it, BossSpawnReason.CUSTOM).entity
            }
        }

        override fun getDisplayName(entity: Entity?): String? {
            return BossAPI.getBoss(entity)?.settings?.customName
        }

        override fun getInternalName(entity: Entity?): String? {
            return BossAPI.getBoss(entity).name
        }

        override fun getIdentifier(): String = "Boss"
    }
}
