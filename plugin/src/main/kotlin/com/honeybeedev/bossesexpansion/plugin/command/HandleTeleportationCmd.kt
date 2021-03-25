package com.honeybeedev.bossesexpansion.plugin.command

import com.honeybeedev.bossesexpansion.plugin.BossesExpansion
import com.honeybeedev.bossesexpansion.plugin.boss.BBoss
import com.honeybeedev.bossesexpansion.plugin.command.argument.UUIDArg
import com.honeybeedev.bossesexpansion.plugin.config.group.action.SpawnAction
import com.oop.orangeengine.command.OCommand
import com.oop.orangeengine.main.task.StaticTask
import java.util.*

class HandleTeleportationCmd : OCommand() {
    init {
        label("handleTeleport")
        description("Handle teleportation of player to a boss")
        argument(UUIDArg().setRequired(true))
        onCommand {
            val uuid = it.getArgAsReq<UUID>("uuid")

            BossesExpansion.instance!!.bossController.getBoss(uuid)?.let { boss ->
                val times = (boss as BBoss).playersTeleported.merge(it.senderAsPlayer.uniqueId, 1) { v1, v2 ->
                    v1 + v2
                }!!

                if (!(it.senderAsPlayer.isOp || it.senderAsPlayer.hasPermission("be.bypassTeleportLimits"))) {
                    boss.bossGroup?.action<SpawnAction>()?.spawnMessage?.teleportLimit?.let {
                        if (it < 0) return@let

                        if (it < times) return@onCommand
                    }
                }

                StaticTask.getInstance().ensureSync { it.senderAsPlayer.teleport(boss.location) }
            }
        }
    }
}
