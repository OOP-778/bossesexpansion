package org.royalix.bossesexpansion.plugin.config.group

import com.oop.orangeengine.yaml.ConfigSection

interface ConfigSerializeable {
    fun load(section: ConfigSection)
    fun save(section: ConfigSection)
}
