package com.honeybeedev.bossesexpansion.plugin.config.group.action

import com.honeybeedev.bossesexpansion.plugin.config.group.GroupFilter
import com.honeybeedev.bossesexpansion.plugin.config.group.GroupFilterable
import com.honeybeedev.bossesexpansion.plugin.config.group.StringStarSection
import com.oop.orangeengine.yaml.ConfigSection

abstract class AbstractAction(val section: ConfigSection) : GroupFilterable {
    override val filters: MutableMap<String, Pair<GroupFilter, StringStarSection>> = hashMapOf()
    override val id: String = section.key
    abstract val actionName: String

    init {
        loadFiltersFrom(section)
    }
}