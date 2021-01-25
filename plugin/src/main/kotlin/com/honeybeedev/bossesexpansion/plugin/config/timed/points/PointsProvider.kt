package com.honeybeedev.bossesexpansion.plugin.config.timed.points

import com.oop.orangeengine.yaml.ConfigSection
import org.bukkit.Location
import java.util.concurrent.CompletableFuture

abstract class PointsProvider(section: ConfigSection) {
    abstract fun provide(parallel: Boolean, amount: Int): CompletableFuture<Collection<Location>>
}