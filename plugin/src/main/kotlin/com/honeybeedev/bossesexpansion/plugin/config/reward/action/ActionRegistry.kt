package com.honeybeedev.bossesexpansion.plugin.config.reward.action

import com.honeybeedev.bossesexpansion.plugin.config.reward.action.impl.CommandAction
import com.oop.orangeengine.yaml.ConfigSection

object ActionRegistry {
    val actions: MutableMap<String, (ConfigSection) -> RewardAction> = hashMapOf()

    init {
        actions["command"] = { CommandAction(it) }
    }
}