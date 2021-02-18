package com.honeybeedev.bossesexpansion.plugin.command

import com.honeybeedev.bossesexpansion.plugin.BossesExpansion
import com.honeybeedev.bossesexpansion.plugin.command.argument.UUIDArg
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
                StaticTask.getInstance().ensureSync { it.senderAsPlayer.teleport(boss.location) }
            }
        }
    }
}
