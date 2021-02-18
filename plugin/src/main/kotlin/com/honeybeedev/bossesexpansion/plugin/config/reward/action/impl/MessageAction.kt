package com.honeybeedev.bossesexpansion.plugin.config.reward.action.impl

import com.honeybeedev.bossesexpansion.plugin.boss.BossDamager
import com.honeybeedev.bossesexpansion.plugin.config.reward.action.RewardAction
import com.honeybeedev.bossesexpansion.plugin.util.Placeholders.replaceMessage
import com.oop.orangeengine.message.OMessage
import com.oop.orangeengine.message.YamlMessage
import com.oop.orangeengine.yaml.ConfigSection

class MessageAction(section: ConfigSection) : RewardAction(section) {
    val message: OMessage<*> = if (section.isValuePresent("message"))
        YamlMessage.load("message", section)
    else
        YamlMessage.load(section.getSection("message").get())

    override fun onReward(damager: BossDamager) {
        replaceMessage(message.clone(), damager)
            .send(damager.offlinePlayer.player)
    }
}
