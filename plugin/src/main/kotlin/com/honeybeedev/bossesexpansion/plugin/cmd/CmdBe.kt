package com.honeybeedev.bossesexpansion.plugin.cmd

import com.honeybeedev.bossesexpansion.plugin.cmd.timedspawner.TimedSpawnerInfo
import com.oop.orangeengine.command.OCommand

class CmdBe : OCommand() {
    init {
        label("bossesexpansion")
        alias("be")

        subCommand(CmdReload())
        subCommand(CmdHandleTP())
        subCommand(TimedSpawnerInfo())
    }
}