package com.honeybeedev.bossesexpansion.plugin.config.group.action

import com.oop.orangeengine.yaml.ConfigSection
import com.oop.orangeengine.yaml.interfaces.Valuable

enum class GroupAction(val supplier: (ConfigSection) -> AbstractAction) {
    SPAWN({ SpawnAction(it) }),
    SUMMARY({ SummaryAction(it) }),
    REWARDS({ RewardsAction(it) }),
    SCOREBOARD( { ScoreboardAction(it) });

    companion object {
        fun load(config: Valuable): List<AbstractAction> {
            val abstractActions: MutableList<AbstractAction> = arrayListOf()
            for (value in values()) {
                value.apply {
                    val optSection = config.getSection(name.toLowerCase())
                    abstractActions += if (!optSection.isPresent)
                        supplier(config.createSection(name.toLowerCase()))
                    else
                        supplier(optSection.get())
                }
            }

            return abstractActions
        }
    }
}
