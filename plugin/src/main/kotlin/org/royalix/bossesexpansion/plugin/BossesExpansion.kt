package org.royalix.bossesexpansion.plugin

import org.bukkit.event.HandlerList
import org.royalix.bossesexpansion.BELoader
import org.royalix.bossesexpansion.api.BossesExpansionAPI
import org.royalix.bossesexpansion.plugin.boss.BossController
import org.royalix.bossesexpansion.plugin.boss.BossProviderController
import org.royalix.bossesexpansion.plugin.command.CommandRegistry
import org.royalix.bossesexpansion.plugin.config.ConfigController
import org.royalix.bossesexpansion.plugin.event.BEventDispatcher
import org.royalix.bossesexpansion.plugin.handler.*
import org.royalix.bossesexpansion.plugin.hook.HookController
import org.royalix.bossesexpansion.plugin.hook.hooks.KangarkoBossHook
import org.royalix.bossesexpansion.plugin.hook.hooks.MythicMobsHook
import org.royalix.bossesexpansion.plugin.hook.hooks.WorldGuardHook
import org.royalix.bossesexpansion.plugin.task.TimedSpawnerTask

class BossesExpansion(val loader: BELoader) : org.royalix.bossesexpansion.api.BossesExpansion {
    val _bossProviderController = BossProviderController()
    val hookController = HookController()
    val _bossController = BossController()
    val _eventDispatcher: BEventDispatcher = BEventDispatcher
    val configController: ConfigController = ConfigController()

    override fun onEnable() {
        loader.oLogger
            .mainColor = "&a"

        loader.oLogger
            .secondaryColor = "&2"

        instance = this
        hookController.registerHooks(
            { MythicMobsHook::class },
            { WorldGuardHook::class },
            { KangarkoBossHook::class })
        BossesExpansionAPI.set(this)

        loader.pluginComponentController
            .add(configController, true)
            .load()

        SpawnHandler
        DamageHandler
        SummaryHandler
        RewardHandler
        ScoreboardHandler

        CommandRegistry

        // Initialize hooks
        TimedSpawnerTask()
    }

    override fun onDisable() {
        instance = null
        HandlerList.unregisterAll(loader)
    }

    companion object {
        var instance: BossesExpansion? = null
    }

    override fun getBossProviderController(): BossProviderController = _bossProviderController
    override fun getBossController(): BossController = _bossController
    override fun getEventDispatcher(): BEventDispatcher = _eventDispatcher
}
