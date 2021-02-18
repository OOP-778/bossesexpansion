package com.honeybeedev.bossesexpansion.plugin.player

import com.google.common.collect.Maps
import com.honeybeedev.bossesexpansion.plugin.util.PluginComponent
import java.util.*

object PlayersController : PluginComponent {
    val players = Maps.newConcurrentMap<UUID, ExpansionPlayer>()

    fun get(uuid: UUID): ExpansionPlayer =
        players.computeIfAbsent(uuid) {
            ExpansionPlayer(uuid, null)
        }
}
