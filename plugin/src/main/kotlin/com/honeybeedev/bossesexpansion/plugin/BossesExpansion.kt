package com.honeybeedev.bossesexpansion.plugin

import com.honeybeedev.bossesexpansion.api.BossesExpansionAPI
import com.honeybeedev.bossesexpansion.plugin.boss.BossController
import com.honeybeedev.bossesexpansion.plugin.boss.BossProviderController
import com.honeybeedev.bossesexpansion.plugin.command.CommandRegistry
import com.honeybeedev.bossesexpansion.plugin.config.ConfigController
import com.honeybeedev.bossesexpansion.plugin.event.BEventDispatcher
import com.honeybeedev.bossesexpansion.plugin.handler.*
import com.honeybeedev.bossesexpansion.plugin.hook.HookController
import com.honeybeedev.bossesexpansion.plugin.hook.hooks.KangarkoBossHook
import com.honeybeedev.bossesexpansion.plugin.hook.hooks.MythicMobsHook
import com.honeybeedev.bossesexpansion.plugin.hook.hooks.WorldGuardHook
import com.honeybeedev.bossesexpansion.plugin.task.TimedSpawnerTask
import com.oop.orangeengine.main.plugin.EnginePlugin
import com.oop.orangeengine.main.task.ClassicTaskController
import com.oop.orangeengine.main.task.TaskController
import org.bukkit.event.HandlerList

class BossesExpansion : EnginePlugin(), com.honeybeedev.bossesexpansion.api.BossesExpansion {
    val _bossProviderController = BossProviderController()
    val hookController = HookController()
    val _bossController = BossController()
    val _eventDispatcher: BEventDispatcher = BEventDispatcher
    val configController: ConfigController = ConfigController()

    override fun enable() {
        instance = this
        hookController.registerHooks({ MythicMobsHook::class }, { WorldGuardHook::class }, { KangarkoBossHook::class })

        oLogger
            .mainColor = "&a"

        oLogger
            .secondaryColor = "&2"

        BossesExpansionAPI.set(this)

        pluginComponentController
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

    override fun disable() {
        instance = null
        HandlerList.unregisterAll(this)
    }

    override fun provideTaskController(): TaskController = ClassicTaskController(this)

    companion object {
        var instance: BossesExpansion? = null
    }

    override fun getBossProviderController(): BossProviderController = _bossProviderController
    override fun getBossController(): BossController = _bossController
    override fun getEventDispatcher(): BEventDispatcher = _eventDispatcher
}
