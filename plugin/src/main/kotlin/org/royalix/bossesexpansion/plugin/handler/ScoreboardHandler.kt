package org.royalix.bossesexpansion.plugin.handler

import com.google.common.collect.Maps
import org.royalix.bossesexpansion.api.event.boss.BossDeathEvent
import org.royalix.bossesexpansion.api.event.boss.BossSpawnEvent
import org.royalix.bossesexpansion.plugin.boss.BossScoreboard
import org.royalix.bossesexpansion.plugin.config.group.action.ScoreboardAction
import org.royalix.bossesexpansion.plugin.player.PlayersController
import org.royalix.bossesexpansion.plugin.util.PluginComponent
import org.royalix.bossesexpansion.plugin.util.dispatchListen
import org.royalix.bossesexpansion.plugin.util.executeTask
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
            if ((boss as org.royalix.bossesexpansion.plugin.boss.BBoss).bossGroup == null) return@dispatchListen

            (boss as org.royalix.bossesexpansion.plugin.boss.BBoss).bossGroup?.action<ScoreboardAction>()
                ?.let { action ->
                    if (!action.accepts(boss as org.royalix.bossesexpansion.plugin.boss.BBoss)) return@let

                    val bossScoreboard = BossScoreboard(
                        action,
                        boss as org.royalix.bossesexpansion.plugin.boss.BBoss
                    )
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
