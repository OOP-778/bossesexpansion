package com.honeybeedev.bossesexpansion.plugin.cmd

import com.honeybeedev.bossesexpansion.plugin.util.BEComponent
import com.oop.orangeengine.command.CommandController
import com.oop.orangeengine.command.scheme.SchemeHolder
import com.oop.orangeengine.file.OFile
import com.oop.orangeengine.yaml.Config

object CommandRegistry : BEComponent {
    var commandController: CommandController =
        CommandController(SchemeHolder(Config(OFile(plugin.dataFolder, "commandScheme.yml").createIfNotExists(true))))

    init {
        commandController.register(CmdBe())
    }

}