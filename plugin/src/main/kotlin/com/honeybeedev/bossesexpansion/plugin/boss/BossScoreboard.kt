package com.honeybeedev.bossesexpansion.plugin.boss

import com.honeybeedev.bossesexpansion.plugin.config.group.action.ScoreboardAction
import com.honeybeedev.bossesexpansion.plugin.player.PlayersController
import com.honeybeedev.bossesexpansion.plugin.util.Placeholders
import com.honeybeedev.bossesexpansion.plugin.util.PluginComponent
import com.honeybeedev.bossesexpansion.plugin.util.damageFormat
import com.oop.inteliframework.scoreboard.InteliScoreboard
import com.oop.inteliframework.scoreboard.LineEntry
import com.oop.orangeengine.main.Helper
import com.oop.orangeengine.main.player.PlayerController
import org.bukkit.entity.Player

class BossScoreboard(val action: ScoreboardAction, val boss: BBoss) : PluginComponent {
    val scoreboard = InteliScoreboard(boss.internalName)

    init {
        scoreboard.setTitleSupplier {
            val placeholders = Placeholders.parsePlaceholders(boss)
            var placeholderedLine: String = action.title!!
            for (placeholder in placeholders) {
                placeholderedLine = placeholderedLine.replace(placeholder.key, placeholder.value)
            }
            Helper.color(placeholderedLine)
        }
    }

    fun updateLines() {
        val lines = mutableListOf<LineEntry>()
        val sortDamagers = boss.sortDamagers()

        var added = 0
        val damagers = mutableListOf<BossDamager>()

        sortDamagers.values.forEachIndexed { index, bossDamager ->
            if (added == action.showDamagers) return@forEachIndexed
            added += 1

            damagers.add(bossDamager)
        }

        val placeholders = Placeholders.parsePlaceholders(boss)

        for (line in action.lines!!) {
            if (line.contains("{DAMAGER_TEMPLATE}")) {
                val template = line.replace("{DAMAGER_TEMPLATE}", "")
                damagers.forEach {
                    lines.add { _ ->
                        Helper.color(
                            template.replace(
                                "%damager_name%",
                                it.offlinePlayer.name
                            ).replace(
                                "%damager_damage%",
                                damageFormat.format(it.damage)
                            ).replace(
                                "%damager_position%",
                                damageFormat.format(it.position.toInt())
                            )
                        )
                    }
                }
                continue
            }

            lines.add {
                var placeholderedLine: String = line
                for (placeholder in placeholders) {
                    placeholderedLine = placeholderedLine.replace(placeholder.key, placeholder.value)
                }
                placeholderedLine
            }
        }
        scoreboard.linesObject.modify{ list ->
            list.clear()
            list.addAll(lines)
        }
    }

    fun updatePlayers() {
        scoreboard.updateAll()

        // Get new players
        val receivers = action.receivers!!(boss.location)

        // Remove players
        scoreboard.viewers.forEach {
            // Remove the viewer
            if (!receivers.contains(it)) {
                scoreboard.remove(it)

                val expansionPlayer = PlayersController
                    .get(it.uniqueId)
                expansionPlayer.currentScoreboard = null
                return
            }
        }

        // Add new viewers
        for (receiver in receivers) {
            // Get expansion player
            val expansionPlayer = PlayersController
                .get((receiver as Player).uniqueId)

            if (expansionPlayer.currentScoreboard == null) {
                scoreboard.add(receiver)
                expansionPlayer.currentScoreboard = boss.uuid
            }
        }
    }
}
