package com.honeybeedev.bossesexpansion.plugin.command

import com.honeybeedev.bossesexpansion.plugin.command.timedspawner.TimedSpawnerInfoCmd
import com.oop.orangeengine.command.OCommand

class MainCmd : OCommand() {
    init {
        label("bossesexpansion")
        alias("be")

        subCommand(ReloadCmd())
        subCommand(HandleTeleportationCmd())
        subCommand(TimedSpawnerInfoCmd())
    }
}
