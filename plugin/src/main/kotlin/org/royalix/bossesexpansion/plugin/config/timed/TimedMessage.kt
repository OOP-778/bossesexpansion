package org.royalix.bossesexpansion.plugin.config.timed

import com.oop.orangeengine.yaml.ConfigSection
import org.royalix.bossesexpansion.plugin.config.message.BEMessage
import org.royalix.bossesexpansion.plugin.util.toSeconds

class TimedMessage(val section: ConfigSection) {
    var message = BEMessage(section)
    val beforeSeconds = toSeconds(section.key)
}
