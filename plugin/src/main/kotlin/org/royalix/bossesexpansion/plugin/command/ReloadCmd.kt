package org.royalix.bossesexpansion.plugin.command

import com.oop.orangeengine.command.OCommand
import org.royalix.bossesexpansion.plugin.constant.Lang

class ReloadCmd : OCommand() {
    init {
        label("reload")
        permission("be.reload")
        description("Reload the plugin")
        onCommand {
            org.royalix.bossesexpansion.plugin.BossesExpansion.instance?.loader!!.pluginComponentController.reload()
            Lang.PLUGIN_RELOADED.normalMessage().send(it.sender)
        }
    }
}
