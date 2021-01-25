package com.honeybeedev.bossesexpansion.plugin.config.group

import com.honeybeedev.bossesexpansion.plugin.config.filter.FilterMethod
import com.honeybeedev.bossesexpansion.plugin.config.filter.FilteredSection
import com.honeybeedev.bossesexpansion.plugin.util.Star
import com.oop.orangeengine.yaml.ConfigSection

data class StringStarSection(
    val section: ConfigSection,
    private var list: MutableList<Star<String>> = arrayListOf()
) : FilteredSection() {
    init {
        load(section)
        list = rawList.map { Star.stringParser.parse(it) }.toMutableList()
    }

    fun add(value: String) {
        rawList.add(value)
        list.add(Star.stringParser.parse(value))
    }

    fun accepts(value: String): Boolean {
        return when (method) {
            FilterMethod.OFF -> true
            FilterMethod.BLACKLIST -> list.none { it.isStar || it.value!!.equals(value, true) }
            FilterMethod.WHITELIST -> list.any { it.isStar || it.value!!.equals(value, true) }
        }
    }

    fun remove(input: String) {
        rawList.remove(input)
        list.remove(Star.stringParser.parse(input))
    }
}