package org.royalix.bossesexpansion.plugin.config.reward.action.impl

import com.oop.orangeengine.item.ItemBuilder
import com.oop.orangeengine.item.custom.OItem
import com.oop.orangeengine.yaml.ConfigSection
import org.royalix.bossesexpansion.plugin.boss.BossDamager
import org.royalix.bossesexpansion.plugin.config.reward.action.RewardAction

class ItemAction(section: ConfigSection) : RewardAction(section) {
    val item = OItem.fromConfiguration<ItemBuilder<*>>(section.getSection("item").get())

    override fun onReward(damager: BossDamager) {
        damager.offlinePlayer.player.inventory.addItem(item.clone().itemStack)
    }
}
