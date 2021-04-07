package org.royalix.bossesexpansion.plugin.handler

import com.oop.orangeengine.message.impl.chat.ChatLine
import com.oop.orangeengine.message.impl.chat.InsertableList
import org.bukkit.Bukkit
import org.royalix.bossesexpansion.api.event.boss.BossDeathEvent
import org.royalix.bossesexpansion.plugin.config.group.action.SummaryAction
import org.royalix.bossesexpansion.plugin.util.Placeholders.replaceMessage
import org.royalix.bossesexpansion.plugin.util.PluginComponent
import org.royalix.bossesexpansion.plugin.util.damageFormat
import org.royalix.bossesexpansion.plugin.util.dispatchListen
import kotlin.math.round

object SummaryHandler : PluginComponent {
    init {
        dispatchListen<BossDeathEvent> {
            // If boss group is not found, return
            if ((boss as org.royalix.bossesexpansion.plugin.boss.BBoss).bossGroup == null) return@dispatchListen

            // If there's no damagers don't execute
            if ((boss!! as org.royalix.bossesexpansion.plugin.boss.BBoss).damageMap.isEmpty()) return@dispatchListen

            // If spawn action is present
            (boss as org.royalix.bossesexpansion.plugin.boss.BBoss).bossGroup?.action<SummaryAction>()
                ?.let {
                    // If the action doesn't accept the boss, return
                    if (!it.accepts(boss as org.royalix.bossesexpansion.plugin.boss.BBoss)) return@dispatchListen

                    logger.printDebug("Boss $boss got accepted by ${(boss as org.royalix.bossesexpansion.plugin.boss.BBoss).bossGroup!!} summary action")

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
                    val sortedDamages =
                        (boss as org.royalix.bossesexpansion.plugin.boss.BBoss).sortDamagers()

                    val totalDamage = boss.totalDamage

                    var pos = 1
                    sortedDamages.forEach { (uuid, damage) ->
                        val color =
                            it.summaryMessage!!.colors!![pos] ?: it.summaryMessage!!.defaultColor

                        val damagerLine = damagerPlaceholderLine.clone()
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
