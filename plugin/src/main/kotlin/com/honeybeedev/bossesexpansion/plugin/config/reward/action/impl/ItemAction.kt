package com.honeybeedev.bossesexpansion.plugin.config.reward.action.impl

import com.honeybeedev.bossesexpansion.plugin.boss.BossDamager
import com.honeybeedev.bossesexpansion.plugin.config.reward.action.RewardAction
import com.oop.orangeengine.item.ItemBuilder
import com.oop.orangeengine.item.custom.OItem
import com.oop.orangeengine.yaml.ConfigSection

class ItemAction(section: ConfigSection) : RewardAction(section) {
    val item = OItem.fromConfiguration<ItemBuilder<*>>(section.getSection("item").get())

    override fun onReward(damager: BossDamager) {
        damager.offlinePlayer.player.inventory.addItem(item.clone().itemStack)
    }
}