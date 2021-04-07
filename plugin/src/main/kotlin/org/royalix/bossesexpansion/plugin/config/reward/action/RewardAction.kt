package org.royalix.bossesexpansion.plugin.config.reward.action

import com.oop.orangeengine.yaml.ConfigSection
import org.royalix.bossesexpansion.plugin.boss.BossDamager

abstract class RewardAction(open val section: ConfigSection) {
    abstract fun onReward(damager: BossDamager)
}
