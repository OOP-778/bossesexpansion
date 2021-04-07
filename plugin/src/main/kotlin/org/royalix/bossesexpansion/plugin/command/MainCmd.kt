package org.royalix.bossesexpansion.plugin.command

import com.oop.orangeengine.command.OCommand
import org.royalix.bossesexpansion.plugin.command.timedspawner.TimedSpawnerInfoCmd

class MainCmd : OCommand() {
    init {
        label("bossesexpansion")
        alias("be")

        subCommand(ReloadCmd())
        subCommand(HandleTeleportationCmd())
        subCommand(TimedSpawnerInfoCmd())
    }
}
