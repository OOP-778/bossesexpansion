package com.honeybeedev.bossesexpansion.plugin.handler

import com.honeybeedev.bossesexpansion.api.event.boss.BossDeathEvent
import com.honeybeedev.bossesexpansion.plugin.boss.BBoss
import com.honeybeedev.bossesexpansion.plugin.config.group.action.SummaryAction
import com.honeybeedev.bossesexpansion.plugin.util.Placeholders.replaceMessage
import com.honeybeedev.bossesexpansion.plugin.util.PluginComponent
import com.honeybeedev.bossesexpansion.plugin.util.damageFormat
import com.honeybeedev.bossesexpansion.plugin.util.dispatchListen
import com.oop.orangeengine.message.impl.chat.ChatLine
import com.oop.orangeengine.message.impl.chat.InsertableList
import org.bukkit.Bukkit
import kotlin.math.round

object SummaryHandler : PluginComponent {
    init {
        dispatchListen<BossDeathEvent> {
            // If boss group is not found, return
            if ((boss as BBoss).bossGroup == null) return@dispatchListen

            // If there's no damagers don't execute
            if ((boss!! as BBoss).damageMap.isEmpty()) return@dispatchListen

            // If spawn action is present
            (boss as BBoss).bossGroup?.action<SummaryAction>()?.let {
                // If the action doesn't accept the boss, return
                if (!it.accepts(boss as BBoss)) return@dispatchListen

                logger.printDebug("Boss $boss got accepted by ${(boss as BBoss).bossGroup!!} summary action")

                // Summary message
                val summaryMessage = it.summaryMessage!!.message!!.clone()
                replaceMessage(summaryMessage, boss)

                val damagerPlaceholderLine = summaryMessage
                    .findLine { it.raw().contains("{DAMAGER_PLACEHOLDER}") }
                    .replace("{DAMAGER_PLACEHOLDER}", "")
                val damagerPlaceholderLineIndex =
                    summaryMessage.lineList().indexOf(damagerPlaceholderLine)

                // Parse damagers
                val damagerLines = arrayListOf<ChatLine>()
                val sortedDamages = (boss as BBoss).sortDamagers()

                val totalDamage = boss.totalDamage

                var pos = 1
                sortedDamages.forEach { (uuid, damage) ->
                    val color =
                        it.summaryMessage!!.colors!![pos] ?: it.summaryMessage!!.defaultColor

                    val damagerLine = damagerPlaceholderLine.clone()
                    println(damagerLine.raw())
                    damagerLine
                        .replace("%position%", pos)
                        .replace("%position_color%", color!!)
                        .replace("%damager_name%", Bukkit.getOfflinePlayer(uuid).name)
                        .replace("%damage%", damageFormat.format(damage.damage))
                        .replace(
                            "%damage_percentage%",
                            round(damage.damage / totalDamage * 100).toInt()
                        )
                    pos++
                    damagerLines.add(damagerLine)
                }

                summaryMessage.lineList().insert(
                    damagerPlaceholderLineIndex,
                    InsertableList.InsertMethod.REPLACE,
                    *damagerLines.toTypedArray()
                )
                summaryMessage.centered(true)

                for (onlinePlayer in Bukkit.getOnlinePlayers())
                    summaryMessage.send(onlinePlayer)
            }
        }
    }
}
