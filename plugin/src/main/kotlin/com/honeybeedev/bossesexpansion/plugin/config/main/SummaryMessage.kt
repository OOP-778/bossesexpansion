package com.honeybeedev.bossesexpansion.plugin.config.main

import com.oop.orangeengine.message.YamlMessage
import com.oop.orangeengine.message.impl.OChatMessage
import com.oop.orangeengine.yaml.ConfigSection

class SummaryMessage(section: ConfigSection?) {
    var message: OChatMessage? = null
    var useRomanNumbers: Boolean = false
    var defaultColor: String? = null
    var colors: MutableMap<Int, String>? = null

    init {
        if (section != null) {
            // Load Message
            section.ifSectionPresent("message") {
                message = YamlMessage.Chat.load(it)
            }

            // Load roman numbers
            section.ifValuePresent("roman numbers", Boolean::class.java) {
                useRomanNumbers = it
            }

            // Load default color
            section.ifValuePresent("default color", String::class.java) {
                defaultColor = it
            }

            // Load colors
            section.ifValuePresent("colors", List::class.java) {
                colors = hashMapOf()
                it.map {
                    val split = it.toString().split(":")
                    colors!![split[0].toInt()] = split[1]
                }
            }
        }
    }

    fun cloneWithMerge(merge: SummaryMessage): SummaryMessage {
        val spawnMessage = SummaryMessage(null)
        spawnMessage.useRomanNumbers = merge.useRomanNumbers
        spawnMessage.message = message?.clone()
        spawnMessage.colors = colors?.let { HashMap(it) }
        spawnMessage.defaultColor = defaultColor
        spawnMessage.useRomanNumbers = useRomanNumbers

        if (spawnMessage.message == null && merge.message != null)
            spawnMessage.message = merge.message!!.clone()

        if (spawnMessage.colors == null && merge.colors != null)
            spawnMessage.colors = HashMap(merge.colors!!)

        if (spawnMessage.defaultColor == null && merge.defaultColor != null)
            spawnMessage.defaultColor = merge.defaultColor

        return spawnMessage
    }

}