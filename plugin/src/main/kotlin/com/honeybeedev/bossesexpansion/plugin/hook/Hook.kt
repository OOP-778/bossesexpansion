package com.honeybeedev.bossesexpansion.plugin.hook

import com.honeybeedev.bossesexpansion.plugin.util.PluginComponent
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

abstract class Hook(val pluginName: String) : PluginComponent {
    var hookPlugin: JavaPlugin? = null
    var loaded: Boolean = false
    val required: Boolean = false

    init {
        if (hookPlugin == null)
            hookPlugin = Bukkit.getPluginManager().getPlugin(pluginName) as JavaPlugin

        loaded = plugin != null
        this.hookPlugin = plugin
    }
}
