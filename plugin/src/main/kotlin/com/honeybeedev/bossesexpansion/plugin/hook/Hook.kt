package com.honeybeedev.bossesexpansion.plugin.hook

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

abstract class Hook(val pluginName: String) {
    var plugin: JavaPlugin? = null
    var loaded: Boolean = false
    val required: Boolean = false

    init {
        var plugin = plugin
        if (plugin == null)
            plugin = Bukkit.getPluginManager().getPlugin(pluginName) as JavaPlugin

        loaded = plugin != null
        this.plugin = plugin
    }
}