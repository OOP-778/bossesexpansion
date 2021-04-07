package org.royalix.bossesexpansion.plugin.handler

import com.oop.orangeengine.message.impl.OChatMessage
import com.oop.orangeengine.message.impl.chat.InsertableList
import org.bukkit.ChatColor
import org.royalix.bossesexpansion.api.event.boss.BossSpawnEvent
import org.royalix.bossesexpansion.plugin.BossesExpansion
import org.royalix.bossesexpansion.plugin.config.group.action.SpawnAction
import org.royalix.bossesexpansion.plugin.util.Placeholders.replaceMessage
import org.royalix.bossesexpansion.plugin.util.PluginComponent
import org.royalix.bossesexpansion.plugin.util.dispatchListen

object SpawnHandler : PluginComponent {
    init {
        // Assign group to the boss if present
        dispatchListen<BossSpawnEvent> {
            logger.printDebug("Boss Spawn Event $boss")
            for (value in (plugin.plugin as BossesExpansion).configController.groups.values) {
                if (value.accepts(boss as org.royalix.bossesexpansion.plugin.boss.BBoss)) {
                    logger.printDebug("$boss got accepted by $value")
                    (boss as org.royalix.bossesexpansion.plugin.boss.BBoss).bossGroup = value
                }
            }
        }

        dispatchListen<BossSpawnEvent> {
            // If boss group is not found, return
            if ((boss as org.royalix.bossesexpansion.plugin.boss.BBoss).bossGroup == null) return@dispatchListen

            // If spawn action is present
            (boss as org.royalix.bossesexpansion.plugin.boss.BBoss).bossGroup?.action<SpawnAction>()
                ?.let {
                    // If the action doesn't accept the boss, return
                    if (!it.accepts(boss as org.royalix.bossesexpansion.plugin.boss.BBoss)) return@dispatchListen

                    logger.printDebug("Boss $boss gots accepted by ${(boss as org.royalix.bossesexpansion.plugin.boss.BBoss).bossGroup!!} spawn action")

                    // Spawn message
                    val spawnMessage =
                        it.spawnMessage!!.spawnMessage!!.message.clone() as OChatMessage

                    val teleportMessageLineIndex =
                        spawnMessage.indexOf {
                            ChatColor.stripColor(it.raw()!!).startsWith("{TELEPORT_MESSAGE}")
                        }

                    val messageWithoutTeleport = spawnMessage.clone()

                    messageWithoutTeleport.lineList().removeAt(teleportMessageLineIndex)
                    replaceMessage(messageWithoutTeleport, boss)

                    val messageWithTeleport = spawnMessage.clone()

                    // Replace the teleport message
                    it.spawnMessage!!.teleportMessage!!.clone()?.let { tm ->
                        messageWithTeleport.lineList().insert(
                            teleportMessageLineIndex,
                            InsertableList.InsertMethod.REPLACE,
                            *tm.lineList().toTypedArray()
                        )
                    }

                    replaceMessage(messageWithTeleport, boss)

                    // Get the receivers
                    val receivers = it.spawnMessage!!.spawnMessage!!.receivers.invoke(boss.location)
                    for (receiver in receivers) {
                        if (it.sendTeleportMessage && if (it.teleportMessagePerm == null || receiver.isOp) true else receiver.hasPermission(
                                it.teleportMessagePerm
                            )
                        )
                            messageWithTeleport.send(receiver)
                        else
                            messageWithoutTeleport.send(receiver)
                    }
                }
        }
    }
}
