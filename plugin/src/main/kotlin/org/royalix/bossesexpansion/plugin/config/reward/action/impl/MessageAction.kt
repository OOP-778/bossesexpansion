package org.royalix.bossesexpansion.plugin.config.reward.action.impl

import com.oop.orangeengine.message.OMessage
import com.oop.orangeengine.message.YamlMessage
import com.oop.orangeengine.yaml.ConfigSection
import org.royalix.bossesexpansion.plugin.boss.BossDamager
import org.royalix.bossesexpansion.plugin.config.reward.action.RewardAction
import org.royalix.bossesexpansion.plugin.util.Placeholders.replaceMessage

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
