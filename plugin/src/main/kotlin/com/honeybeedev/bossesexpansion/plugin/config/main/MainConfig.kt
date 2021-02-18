package com.honeybeedev.bossesexpansion.plugin.config.main

import com.oop.orangeengine.yaml.Config

class MainConfig(config: Config) {
    var localeName: String = config.getAs("locale")
    var debugMode: Boolean = config.getAs("debug")

    var defaultSummaryMessage: SummaryMessage =
        SummaryMessage(config.getSection("default summary message").get())
    var defaultSpawnMessage: SpawnMessage =
        SpawnMessage(config.getSection("default spawn message").get())
}
