package org.royalix.bossesexpansion.plugin.command

import com.oop.orangeengine.command.CommandController
import com.oop.orangeengine.command.scheme.SchemeHolder
import com.oop.orangeengine.file.OFile
import com.oop.orangeengine.yaml.Config
import org.royalix.bossesexpansion.plugin.util.PluginComponent

object CommandRegistry : PluginComponent {
    var commandController: CommandController =
        CommandController(
            SchemeHolder(
                Config(
                    OFile(
                        plugin.dataFolder,
                        "commandScheme.yml"
                    ).createIfNotExists(true)
                )
            )
        )

    init {
        commandController.register(MainCmd())
    }
}
