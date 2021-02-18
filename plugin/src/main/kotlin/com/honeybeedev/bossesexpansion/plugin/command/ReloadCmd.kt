package com.honeybeedev.bossesexpansion.plugin.command

import com.honeybeedev.bossesexpansion.plugin.BossesExpansion
import com.honeybeedev.bossesexpansion.plugin.constant.Lang
import com.oop.orangeengine.command.OCommand

class ReloadCmd : OCommand() {
    init {
        label("reload")
        permission("be.reload")
        description("Reload the plugin")
        onCommand {
            BossesExpansion.instance?.pluginComponentController?.reload()
            Lang.PLUGIN_RELOADED.normalMessage().send(it.sender)
        }
    }
}
