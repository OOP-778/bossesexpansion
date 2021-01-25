package com.honeybeedev.bossesexpansion.plugin.boss

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.*

data class BossDamager(
    val uuid: UUID,
    var damage: Double = 0.0,
    val boss: BBoss
) {
    val offlinePlayer: OfflinePlayer = Bukkit.getOfflinePlayer(uuid)
}
