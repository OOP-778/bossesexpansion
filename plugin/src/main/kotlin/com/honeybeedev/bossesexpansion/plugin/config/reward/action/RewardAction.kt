package com.honeybeedev.bossesexpansion.plugin.config.reward.action

import com.honeybeedev.bossesexpansion.plugin.boss.BossDamager
import com.oop.orangeengine.yaml.ConfigSection

abstract class RewardAction(open val section: ConfigSection) {
    abstract fun onReward(damager: BossDamager)
}