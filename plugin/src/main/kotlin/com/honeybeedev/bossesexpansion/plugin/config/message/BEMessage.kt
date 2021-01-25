package com.honeybeedev.bossesexpansion.plugin.config.message

import com.oop.orangeengine.message.OMessage
import com.oop.orangeengine.message.YamlMessage
import com.oop.orangeengine.yaml.ConfigSection
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender

class BEMessage() {
    lateinit var message: OMessage<*>
    lateinit var receivers: (Location?) -> Collection<CommandSender>

    constructor(section: ConfigSection) : this() {
        message = YamlMessage.load(section)
        receivers = { Bukkit.getOnlinePlayers() }
        section.ifValuePresent("receivers", String::class.java) { ReceiversSuppliers.parseSupplier(it) }
    }

    fun send(location: Location?, modifier: ((OMessage<*>).() -> Unit)? = null) {
        val messageClone = message.clone()
        modifier?.let { modifier(messageClone) }
        messageClone.send(*receivers(location).toTypedArray())
    }

    fun clone(): BEMessage {
        val clone = BEMessage()
        clone.message = message.clone()
        clone.receivers = receivers
        return clone
    }
}