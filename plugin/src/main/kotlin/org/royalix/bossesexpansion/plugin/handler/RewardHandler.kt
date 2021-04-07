package org.royalix.bossesexpansion.plugin.handler

import org.royalix.bossesexpansion.api.event.boss.BossDeathEvent
import org.royalix.bossesexpansion.plugin.config.group.action.RewardsAction
import org.royalix.bossesexpansion.plugin.config.reward.BeReward
import org.royalix.bossesexpansion.plugin.util.PluginComponent
import org.royalix.bossesexpansion.plugin.util.RepeatableQueue
import org.royalix.bossesexpansion.plugin.util.dispatchListen
import java.util.concurrent.ThreadLocalRandom

object RewardHandler : PluginComponent {
    private const val maxTriesPerPlace = 1000

    init {
        dispatchListen<BossDeathEvent> {
            // If boss group is not found, return
            if ((boss as org.royalix.bossesexpansion.plugin.boss.BBoss).bossGroup == null) return@dispatchListen

            // If there's no damagers don't execute
            if ((boss!! as org.royalix.bossesexpansion.plugin.boss.BBoss).damageMap.isEmpty()) return@dispatchListen

            (boss as org.royalix.bossesexpansion.plugin.boss.BBoss).bossGroup?.action<RewardsAction>()
                ?.let { action ->
                    // If the action doesn't accept the boss, return
                    if (!action.accepts(boss as org.royalix.bossesexpansion.plugin.boss.BBoss)) return@dispatchListen

                    val damageSorted = (boss as org.royalix.bossesexpansion.plugin.boss.BBoss)
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
                                    for (reward in org.royalix.bossesexpansion.plugin.BossesExpansion.instance!!.configController.rewards.values) {
                                        // If filters meets the requirements
                                        if (!reward.accepts((boss as org.royalix.bossesexpansion.plugin.boss.BBoss))) continue

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
