package com.honeybeedev.bossesexpansion.plugin.handler

import com.honeybeedev.bossesexpansion.api.event.boss.BossDeathEvent
import com.honeybeedev.bossesexpansion.plugin.BossesExpansion
import com.honeybeedev.bossesexpansion.plugin.boss.BBoss
import com.honeybeedev.bossesexpansion.plugin.config.group.action.RewardsAction
import com.honeybeedev.bossesexpansion.plugin.config.reward.BeReward
import com.honeybeedev.bossesexpansion.plugin.util.PluginComponent
import com.honeybeedev.bossesexpansion.plugin.util.RepeatableQueue
import com.honeybeedev.bossesexpansion.plugin.util.dispatchListen
import java.util.concurrent.ThreadLocalRandom

object RewardHandler : PluginComponent {
    private const val maxTriesPerPlace = 1000

    init {
        dispatchListen<BossDeathEvent> {
            // If boss group is not found, return
            if ((boss as BBoss).bossGroup == null) return@dispatchListen

            // If there's no damagers don't execute
            if ((boss!! as BBoss).damageMap.isEmpty()) return@dispatchListen

            (boss as BBoss).bossGroup?.action<RewardsAction>()?.let { action ->
                // If the action doesn't accept the boss, return
                if (!action.accepts(boss as BBoss)) return@dispatchListen

                val damageSorted = (boss as BBoss)
                    .sortDamagers()
                    .toMutableMap()

                damageSorted
                    .entries
                    .filterIndexed { index, _ -> action.receivers.find { it.test(index + 1) } != null }
                    .forEachIndexed { index, data ->
                        action.places
                            .find { it.key.test(index + 1) }
                            ?.let {

                                val rewards = mutableSetOf<BeReward>()
                                for (reward in BossesExpansion.instance!!.configController.rewards.values) {
                                    // If filters meets the requirements
                                    if (!reward.accepts((boss as BBoss))) continue

                                    // If meets the place requirements
                                    if (!reward.places.test(index + 1)) continue

                                    rewards.add(reward)
                                }

                                // If none rewards match, return
                                if (rewards.isEmpty()) return@let

                                val rewardsQueue = RepeatableQueue(rewards.toTypedArray())

                                val chanceChecker: (BeReward) -> Boolean = {
                                    val generated = ThreadLocalRandom.current().nextDouble()
                                    val required = it.chance
                                    generated < required
                                }

                                val receivingRewards = it.value.getter()
                                for (i in 0 until receivingRewards) {
                                    for (tried in 0 until maxTriesPerPlace) {
                                        val poll = rewardsQueue.poll()
                                        if (!chanceChecker(poll)) continue

                                        poll.give(data.value)
                                        break
                                    }
                                }
                            }
                    }
            }
        }
    }
}
