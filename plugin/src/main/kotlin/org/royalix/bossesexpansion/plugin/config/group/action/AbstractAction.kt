package org.royalix.bossesexpansion.plugin.config.group.action

import com.oop.orangeengine.yaml.ConfigSection
import org.royalix.bossesexpansion.plugin.config.group.GroupFilter
import org.royalix.bossesexpansion.plugin.config.group.GroupFilterable
import org.royalix.bossesexpansion.plugin.config.group.StringStarSection

abstract class AbstractAction(val section: ConfigSection) : GroupFilterable {
    override val filters: MutableMap<String, Pair<GroupFilter, StringStarSection>> = hashMapOf()
    override val id: String = section.key
    abstract val actionName: String

    init {
        loadFiltersFrom(section)
    }
}
