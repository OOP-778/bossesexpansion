package com.honeybeedev.bossesexpansion.plugin.util

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import java.util.regex.Pattern

object ReceiversSuppliers {
    val suppliers: MutableSet<Pair<(String) -> Boolean, (String) -> (Location?) -> Collection<CommandSender>>> =
        hashSetOf()

    init {
        // Register all expression
        register(Pattern.compile("^all\$")) {
            { Bukkit.getOnlinePlayers() }
        }

        // Register region expression
        register(Pattern.compile("^players in ([ ^]+) region\$")) { args ->
            throw NotImplementedError("Not implemented")
        }

        // Register world expression
        register(Pattern.compile("^players in ([^ ]+) world\$")) { args ->
            val worldName = args[0]
            {
                Collections.unmodifiableCollection(Bukkit.getWorld(worldName).players)
            }
        }

        register(Pattern.compile("^players in ([0-9]+) radius\$")) { args ->
            val radius = args[0].toDouble()
            return@register { location ->
                location!!.world.getNearbyEntities(location, radius, radius, radius)
                    .filterIsInstance<Player>()
            }
        }
    }

    fun parseSupplier(input: String): (Location?) -> Collection<CommandSender> {
        var supplier: ((Location?) -> Collection<CommandSender>)? = null
        for (pair in suppliers)
            if (pair.first(input)) {
                supplier = pair.second(input)
                break
            }

        return supplier ?: throw IllegalStateException("Failed to find ReceiversSupplier by $input")
    }

    fun register(
        regex: Pattern,
        supplier: (Array<String>) -> (Location?) -> Collection<CommandSender>
    ) {
        val predicate: (String) -> Boolean = { input -> regex.matcher(input).find() }

        val supplierFunction: (String) -> (Location?) -> Collection<CommandSender> = {
            val matcher = regex.matcher(it)
            matcher.find()

            var matches = Array(matcher.groupCount()) { "" }
            println(matcher.groupCount())
            for (i in 1..matcher.groupCount()) {
                matches[i - 1] = matcher.group(i)
            }
            supplier(matches)
        }
        suppliers.add(Pair(predicate, supplierFunction))
    }
}
