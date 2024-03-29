package org.royalix.bossesexpansion.plugin.config.group

import com.oop.orangeengine.yaml.Config
import com.oop.orangeengine.yaml.ConfigSection
import org.royalix.bossesexpansion.plugin.util.logger

interface GroupFilterable {
    val filters: MutableMap<String, Pair<GroupFilter, StringStarSection>>
    val id: String

    fun loadFiltersFrom(section: ConfigSection?) {
        if (section == null) return

        if (!section.key.equals("filters", true))
            return loadFiltersFrom(section.sections["filters"])

        section.sections.values
            .map { fs ->
                Pair(
                    GroupFilter.values().firstOrNull { f -> f.name.equals(fs.key, true) },
                    StringStarSection(fs)
                )
            }
            .filter { p -> p.first != null }
            .forEach { p -> filters[p.first!!.name.toLowerCase()] = Pair(p.first!!, p.second) }
    }

    fun loadFiltersFrom(config: Config) =
        config.ifSectionPresent("filters") { loadFiltersFrom(it) }

    fun accepts(boss: org.royalix.bossesexpansion.plugin.boss.BBoss): Boolean {
        for (filter in filters.values) {
            if (!filter.first.filter(boss, filter.second))
                return false
        }

        logger.printDebug("Boss ${boss.provider.getInternalName(boss.entity)} was accepted by $id")
        return true
    }
}
