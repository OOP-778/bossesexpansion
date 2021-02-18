package com.honeybeedev.bossesexpansion.plugin.command.timedspawner

import com.honeybeedev.bossesexpansion.plugin.BossesExpansion
import com.honeybeedev.bossesexpansion.plugin.constant.Lang
import com.honeybeedev.bossesexpansion.plugin.util.Placeholders
import com.oop.orangeengine.command.OCommand
import com.oop.orangeengine.message.impl.OChatMessage
import com.oop.orangeengine.message.impl.chat.ChatLine
import com.oop.orangeengine.message.impl.chat.InsertableList

class TimedSpawnerInfoCmd : OCommand() {
    init {
        label("info")
        onCommand {
            val message = Lang.TIMEDSPAWNERS_INFO.normalMessage()
                .clone() as OChatMessage

            val templateLine = message
                .findLine { it.raw().startsWith("{TEMPLATE}") }?.clone() ?: return@onCommand

            val lineIndex = message.indexOf { it.raw()!!.contentEquals(templateLine.raw()) }

            templateLine.replace("{TEMPLATE}", "")
            val timedSpawners = BossesExpansion.instance!!.configController.timedSpawners.values

            val lines = mutableListOf<ChatLine>()

            for (timedSpawner in timedSpawners) {
                val timedSpawnerLine = templateLine.clone()
                Placeholders.replaceMessage(timedSpawnerLine, timedSpawner)

                lines.add(timedSpawnerLine)
            }

            message.lineList().insert(
                lineIndex,
                InsertableList.InsertMethod.REPLACE,
                *lines.toTypedArray()
            )

            message.send(it.sender)
        }
    }
}
