package com.honeybeedev.bossesexpansion.plugin.config.filter

import com.honeybeedev.bossesexpansion.plugin.config.group.ConfigSerializeable
import com.oop.orangeengine.yaml.ConfigSection

open class FilteredSection(
    var method: FilterMethod = FilterMethod.OFF,
    var rawList: MutableList<String> = arrayListOf()
) : ConfigSerializeable {
    override fun load(section: ConfigSection) {
        section.ifValuePresent("filter", String::class.java) { method = FilterMethod.match(it) }
        section.ifValuePresent("list", MutableList::class.java) { rawList = it as MutableList<String> }
    }

    override fun save(section: ConfigSection) {
        section["filter"] = method.name.toLowerCase()
        section["list"] = rawList
    }
}