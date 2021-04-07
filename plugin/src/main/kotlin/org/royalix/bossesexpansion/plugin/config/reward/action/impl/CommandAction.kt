package org.royalix.bossesexpansion.plugin.config.reward.action.impl

import com.oop.orangeengine.yaml.ConfigSection
import org.bukkit.Bukkit
import org.royalix.bossesexpansion.plugin.boss.BossDamager
import org.royalix.bossesexpansion.plugin.config.reward.action.RewardAction
import org.royalix.bossesexpansion.plugin.util.Placeholders.parse

class CommandAction(override val section: ConfigSection) : RewardAction(section) {
    val command: String

    init {
        section.ensureHasValues("command")
        command = section.getAs("command")
    }

    override fun onReward(damager: BossDamager) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.parse(damager))
    }
}
