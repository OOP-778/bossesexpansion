package com.honeybeedev.bossesexpansion.plugin.handler

import com.google.common.collect.Maps
import com.honeybeedev.bossesexpansion.api.event.boss.BossDeathEvent
import com.honeybeedev.bossesexpansion.api.event.boss.BossSpawnEvent
import com.honeybeedev.bossesexpansion.plugin.boss.BBoss
import com.honeybeedev.bossesexpansion.plugin.boss.BossScoreboard
import com.honeybeedev.bossesexpansion.plugin.config.group.action.ScoreboardAction
import com.honeybeedev.bossesexpansion.plugin.player.PlayersController
import com.honeybeedev.bossesexpansion.plugin.util.PluginComponent
import com.honeybeedev.bossesexpansion.plugin.util.dispatchListen
import com.honeybeedev.bossesexpansion.plugin.util.executeTask
import java.util.*
import java.util.concurrent.TimeUnit

object ScoreboardHandler : PluginComponent {
    private val scoreboardMap: MutableMap<UUID, BossScoreboard> = Maps.newConcurrentMap()

    init {
        executeTask(
            "boss-sb-update",
            async = true,
            repeat = true,
            delay = TimeUnit.SECONDS.toMillis(1).toInt()
        ) {
            for (sbEntry in scoreboardMap.entries) {
                // Update lines
                sbEntry.value.updateLines()

                // Update viewers
                sbEntry.value.updatePlayers()
            }
        }

        // Scoreboard init listen
        dispatchListen<BossSpawnEvent> {
            // If boss group is not found, return
            if ((boss as BBoss).bossGroup == null) return@dispatchListen

            (boss as BBoss).bossGroup?.action<ScoreboardAction>()?.let { action ->
                if (!action.accepts(boss as BBoss)) return@let

                val bossScoreboard = BossScoreboard(action, boss as BBoss)
                scoreboardMap[boss.uuid] = bossScoreboard
            }
        }

        // Scoreboard end listen
        dispatchListen<BossDeathEvent> {
            scoreboardMap[boss.uuid]?.let {
                it.scoreboard.viewers.forEach {
                    val expansionPlayer = PlayersController
                        .get(it.uniqueId)
                    expansionPlayer.currentScoreboard = null
                }
                it.scoreboard.remove()
                scoreboardMap.remove(boss.uuid)
            }
        }
    }
}
