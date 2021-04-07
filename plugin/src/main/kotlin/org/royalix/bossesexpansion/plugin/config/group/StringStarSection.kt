package org.royalix.bossesexpansion.plugin.config.group

import com.oop.orangeengine.yaml.ConfigSection
import org.royalix.bossesexpansion.plugin.config.filter.FilterMethod
import org.royalix.bossesexpansion.plugin.config.filter.FilteredSection
import org.royalix.bossesexpansion.plugin.util.Star

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
