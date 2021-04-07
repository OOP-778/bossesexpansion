package org.royalix.bossesexpansion.plugin.hook

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.royalix.bossesexpansion.plugin.util.PluginComponent

abstract class Hook(val pluginName: String) : PluginComponent {
    var hookPlugin: JavaPlugin? = null
    var loaded: Boolean = false
    val required: Boolean = false

    init {
        if (hookPlugin == null)
            hookPlugin = Bukkit.getPluginManager().getPlugin(pluginName) as JavaPlugin?

        loaded = plugin != null
        this.hookPlugin = plugin
    }
}
