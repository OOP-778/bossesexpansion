package org.royalix.bossesexpansion.plugin.command

import com.oop.orangeengine.command.OCommand
import com.oop.orangeengine.main.task.StaticTask
import org.royalix.bossesexpansion.plugin.command.argument.UUIDArg
import org.royalix.bossesexpansion.plugin.config.group.action.SpawnAction
import java.util.*

class HandleTeleportationCmd : OCommand() {
    init {
        label("handleTeleport")
        description("Handle teleportation of player to a boss")
        argument(UUIDArg().setRequired(true))
        onCommand {
            val uuid = it.getArgAsReq<UUID>("uuid")

            org.royalix.bossesexpansion.plugin.BossesExpansion.instance!!.bossController.getBoss(
                uuid
            )?.let { boss ->
                val times =
                    (boss as org.royalix.bossesexpansion.plugin.boss.BBoss).playersTeleported.merge(
                        it.senderAsPlayer.uniqueId,
                        1
                    ) { v1, v2 ->
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
