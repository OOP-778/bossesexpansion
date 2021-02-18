package com.honeybeedev.bossesexpansion.plugin.player

import java.util.UUID

data class ExpansionPlayer(
    val uuid: UUID,
    var currentScoreboard: UUID?
)
