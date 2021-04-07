package org.royalix.bossesexpansion.plugin.config.reward

import com.google.common.base.Preconditions
import com.oop.orangeengine.yaml.Config
import org.royalix.bossesexpansion.plugin.boss.BossDamager
import org.royalix.bossesexpansion.plugin.config.group.GroupFilter
import org.royalix.bossesexpansion.plugin.config.group.GroupFilterable
import org.royalix.bossesexpansion.plugin.config.group.StringStarSection
import org.royalix.bossesexpansion.plugin.config.reward.action.ActionRegistry
import org.royalix.bossesexpansion.plugin.config.reward.action.RewardAction
import org.royalix.bossesexpansion.plugin.util.WrappedNumber
import org.royalix.bossesexpansion.plugin.util.parseNumber

data class BeReward(val config: Config) : GroupFilterable {
    override val filters: MutableMap<String, Pair<GroupFilter, StringStarSection>> = hashMapOf()
    override val id: String = config.file.fileName
    var chance: Double
    var places: WrappedNumber

    var actions: MutableMap<String, RewardAction> = hashMapOf()

    init {
        loadFiltersFrom(config)

        config.ensureHasValues("chance")
        chance = config.getAs("chance", Double::class.java)

        config.ensureHasValues("places")
        places = parseNumber(config.getAs("places", String::class.java))

        config.getSection("actions")
            .ifPresent { actionsSection ->
                actionsSection.sections.values.forEach {
                    it.ensureHasValues("type")
                    val type = it.getAs<String>("type").toLowerCase()
                    Preconditions.checkArgument(
                        type in ActionRegistry.actions,
                        "Invalid Reward Action by $type"
                    )
                    Preconditions.checkArgument(
                        it.isSectionPresent("data"),
                        "Failed to find data for action $type"
                    )

                    actions[type] =
                        ActionRegistry.actions[it.getAs<String>("type")
                            .toLowerCase()]!!(it.getSection("data").get())
                }
            }
    }

    fun give(who: BossDamager) {
        if (!who.offlinePlayer.isOnline)
            TODO("Not yet implemented")

        for (action in actions.values) {
            action.onReward(who)
        }
    }
}
