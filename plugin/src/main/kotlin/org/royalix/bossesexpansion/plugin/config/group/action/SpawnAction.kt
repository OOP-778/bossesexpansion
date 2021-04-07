package org.royalix.bossesexpansion.plugin.config.group.action

import com.oop.orangeengine.yaml.ConfigSection
import org.royalix.bossesexpansion.plugin.config.main.SpawnMessage

class SpawnAction(section: ConfigSection) : AbstractAction(section) {
    override val actionName: String = "spawn"

    var sendTeleportMessage: Boolean = false
    var spawnMessage: SpawnMessage? = null
    var teleportMessagePerm: String? = null

    init {
        if ("send teleport message" in section.values)
            sendTeleportMessage = section
                .getAs("send teleport message", Boolean::class.java)

        if ("teleport permission" in section.values)
            teleportMessagePerm = section.getAs("teleport permission")

        spawnMessage = if ("spawn message" in section.sections)
            SpawnMessage(section.sections["spawn message"])
        else
            org.royalix.bossesexpansion.plugin.BossesExpansion.instance!!.configController.mainConfig.defaultSpawnMessage
    }
}
