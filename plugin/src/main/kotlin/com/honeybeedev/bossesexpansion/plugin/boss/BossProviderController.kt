package com.honeybeedev.bossesexpansion.plugin.boss

import com.honeybeedev.bossesexpansion.api.boss.BossProvider
import com.honeybeedev.bossesexpansion.api.boss.BossProviderController
import com.honeybeedev.bossesexpansion.plugin.util.PluginComponent
import org.bukkit.Location
import org.bukkit.entity.Entity
import java.util.*
import java.util.stream.Collectors

class BossProviderController : PluginComponent,
    BossProviderController {
    private val providers: MutableSet<BossProvider> = HashSet()

    override fun registerProvider(provider: BossProvider) {
        logger.print("Registered ${provider.identifier} boss provider")
        providers.add(provider)
    }

    fun findSpawnProvider(bossId: String): ((Location) -> Entity)? {
        val provider = providers
            .map { it.provideSpawner(bossId) }
            .firstOrNull()
            ?: return null

        return {
            provider.apply(it)
        }
    }

    val allBosses: MutableList<String>
        get() = providers
            .stream()
            .map { obj: BossProvider -> obj.listBosses() }
            .flatMap { it!!.stream() }
            .collect(Collectors.toList())
}
