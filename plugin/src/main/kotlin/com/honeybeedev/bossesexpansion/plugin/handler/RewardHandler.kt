package com.honeybeedev.bossesexpansion.plugin.handler

import com.honeybeedev.bossesexpansion.api.event.BossDeathEvent
import com.honeybeedev.bossesexpansion.plugin.boss.BBoss
import com.honeybeedev.bossesexpansion.plugin.config.group.action.RewardsAction
import com.honeybeedev.bossesexpansion.plugin.util.BEComponent
import com.honeybeedev.bossesexpansion.plugin.util.dispatchListen

object RewardHandler : BEComponent {
    init {
        dispatchListen<BossDeathEvent> {
            // If boss group is not found, return
            if ((boss as BBoss).bossGroup == null) return@dispatchListen

            // If there's no damagers don't execute
            if ((boss!! as BBoss).damageMap.isEmpty()) return@dispatchListen

            (boss as BBoss).bossGroup?.action<RewardsAction>()?.let { action ->
                action
            }
        }
    }
}