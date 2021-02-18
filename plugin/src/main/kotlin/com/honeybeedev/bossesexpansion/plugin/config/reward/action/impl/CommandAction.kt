package com.honeybeedev.bossesexpansion.plugin.config.reward.action.impl

import com.honeybeedev.bossesexpansion.plugin.boss.BossDamager
import com.honeybeedev.bossesexpansion.plugin.config.reward.action.RewardAction
import com.honeybeedev.bossesexpansion.plugin.util.Placeholders.parse
import com.oop.orangeengine.yaml.ConfigSection
import org.bukkit.Bukkit

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
