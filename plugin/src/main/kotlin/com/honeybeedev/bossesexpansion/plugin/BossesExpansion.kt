package com.honeybeedev.bossesexpansion.plugin

import com.honeybeedev.bossesexpansion.api.BossesExpansionAPI
import com.honeybeedev.bossesexpansion.plugin.cmd.CommandRegistry
import com.honeybeedev.bossesexpansion.plugin.controller.BossController
import com.honeybeedev.bossesexpansion.plugin.controller.BossProviderController
import com.honeybeedev.bossesexpansion.plugin.controller.ConfigController
import com.honeybeedev.bossesexpansion.plugin.controller.HookController
import com.honeybeedev.bossesexpansion.plugin.event.BEventDispatcher
import com.honeybeedev.bossesexpansion.plugin.handler.DamageHandler
import com.honeybeedev.bossesexpansion.plugin.handler.DeathHandler
import com.honeybeedev.bossesexpansion.plugin.handler.SpawnHandler
import com.honeybeedev.bossesexpansion.plugin.hook.hooks.MythicMobsHook
import com.honeybeedev.bossesexpansion.plugin.tasks.TimedSpawnerTask
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

        oLogger
            .mainColor = "&a"

        oLogger
            .secondaryColor = "&2"

        BossesExpansionAPI.set(this)
        oLogger.isDebugMode = true

        pluginComponentController
            .add(configController, true)
            .load()

        SpawnHandler
        DamageHandler
        DeathHandler

        CommandRegistry

        // Initialize hooks
        hookController.registerHooks({ MythicMobsHook::class })
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