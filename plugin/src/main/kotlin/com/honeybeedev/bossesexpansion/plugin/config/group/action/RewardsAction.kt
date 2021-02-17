package com.honeybeedev.bossesexpansion.plugin.config.group.action

import com.honeybeedev.bossesexpansion.plugin.util.WrappedNumber
import com.honeybeedev.bossesexpansion.plugin.util.parseNumber
import com.oop.orangeengine.main.util.data.pair.OPair
import com.oop.orangeengine.yaml.ConfigSection

class RewardsAction(section: ConfigSection) : AbstractAction(section) {
    override val actionName: String = "rewards"

    val places: MutableSet<OPair<WrappedNumber, WrappedNumber>> = linkedSetOf()
    var receivers: MutableSet<WrappedNumber> = linkedSetOf()

    init {
        section.sections.values.forEach {
            places.add(OPair(parseNumber(it.key), parseNumber(it.getAs("amount", String::class.java))))
        }

        section.getAs<List<String>>("receivers")
            .map { parseNumber(it) }
            .forEach { receivers.add(it) }
    }
}