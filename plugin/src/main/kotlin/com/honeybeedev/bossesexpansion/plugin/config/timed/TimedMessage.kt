package com.honeybeedev.bossesexpansion.plugin.config.timed

import com.honeybeedev.bossesexpansion.plugin.config.message.BEMessage
import com.honeybeedev.bossesexpansion.plugin.util.toSeconds
import com.oop.orangeengine.yaml.ConfigSection

class TimedMessage(val section: ConfigSection) {
    var message = BEMessage(section)
    val beforeSeconds = toSeconds(section.key)
}