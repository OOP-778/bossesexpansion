package org.royalix.bossesexpansion.plugin.config.reward.action

import com.oop.orangeengine.yaml.ConfigSection
import org.royalix.bossesexpansion.plugin.config.reward.action.impl.CommandAction
import org.royalix.bossesexpansion.plugin.config.reward.action.impl.ItemAction
import org.royalix.bossesexpansion.plugin.config.reward.action.impl.MessageAction

object ActionRegistry {
    val actions: MutableMap<String, (ConfigSection) -> RewardAction> = hashMapOf()

    init {
        actions["command"] = { CommandAction(it) }
        actions["message"] = { MessageAction(it) }
        actions["item"] = { ItemAction(it) }
    }
}
