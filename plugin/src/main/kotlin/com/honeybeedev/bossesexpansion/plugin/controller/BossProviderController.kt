package com.honeybeedev.bossesexpansion.plugin.controller

import com.honeybeedev.bossesexpansion.api.boss.BossProvider
import com.honeybeedev.bossesexpansion.api.controller.BossProviderController
import com.honeybeedev.bossesexpansion.plugin.util.BEComponent
import org.bukkit.Location
import java.util.*
import java.util.stream.Collectors

class BossProviderController : BEComponent, BossProviderController {
    private val providers: MutableSet<BossProvider> = HashSet()

    override fun registerProvider(provider: BossProvider) {
        logger.print("Registered ${provider.identifier} boss provider")
        providers.add(provider)
    }

    fun findSpawnProvider(bossId: String): ((Location) -> Unit)? {
        val provider = providers
            .map { it.provideSpawner(bossId) }
            .firstOrNull()
            ?: return null

        return {
            provider.accept(it)
        }
    }

    val allBosses: MutableList<String>
        get() = providers
            .stream()
            .map { obj: BossProvider -> obj.listBosses() }
            .flatMap { it!!.stream() }
            .collect(Collectors.toList())
}