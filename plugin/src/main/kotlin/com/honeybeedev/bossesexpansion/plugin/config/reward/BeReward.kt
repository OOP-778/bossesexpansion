package com.honeybeedev.bossesexpansion.plugin.config.reward

import com.google.common.base.Preconditions
import com.honeybeedev.bossesexpansion.plugin.config.filter.FilteredSection
import com.honeybeedev.bossesexpansion.plugin.config.reward.action.ActionRegistry
import com.honeybeedev.bossesexpansion.plugin.config.reward.action.RewardAction
import com.honeybeedev.bossesexpansion.plugin.util.WrappedNumber
import com.honeybeedev.bossesexpansion.plugin.util.parseNumber
import com.oop.orangeengine.yaml.Config

data class BeReward(val config: Config) {
    val bosses: FilteredSection = FilteredSection()
    var chance: Double
    var places: WrappedNumber

    var actions: MutableMap<String, RewardAction> = hashMapOf()

    init {
        Preconditions.checkArgument(config.isSectionPresent("bosses"), "Bosses section is not present!")
        bosses.load(config.getSection("bosses").get())

        config.ensureHasValues("chance")
        chance = config.getAs("chance", Double::class.java)

        config.ensureHasValues("places")
        places = parseNumber(config.getAs("places", String::class.java))

        config.getSection("actions")
            .ifPresent { actionsSection ->
                actionsSection.sections.values.forEach {
                    it.ensureHasValues("type")
                    val type = it.getAs<String>("type").toLowerCase()
                    Preconditions.checkArgument(type in ActionRegistry.actions, "Invalid Reward Action by $type")
                    Preconditions.checkArgument(it.isSectionPresent("data"), "Failed to find data for action $type")

                    actions[type] =
                        ActionRegistry.actions[it.getAs<String>("type").toLowerCase()]!!(it.getSection("data").get())
                }
            }
    }
}