package com.honeybeedev.bossesexpansion.plugin.config.group.action

import com.honeybeedev.bossesexpansion.plugin.BossesExpansion
import com.honeybeedev.bossesexpansion.plugin.boss.BBoss
import com.honeybeedev.bossesexpansion.plugin.config.main.SummaryMessage
import com.oop.orangeengine.yaml.ConfigSection

class SummaryAction(section: ConfigSection) : AbstractAction(section) {
    override val actionName: String = "summary"

    var damagersRequired: Int = -1
    var summaryMessage: SummaryMessage? = null

    init {
        if ("damagers required" !in section.values)
            damagersRequired =
                section.set("damagers required", damagersRequired).getAs(Int::class.java)

        summaryMessage = if ("summary message" in section.sections)
            SummaryMessage(section.sections["summary message"])
        else
            BossesExpansion.instance!!.configController.mainConfig.defaultSummaryMessage
    }

    override fun accepts(boss: BBoss): Boolean {
        return ((boss.damageMap.size < damagersRequired) or (damagersRequired == -1)) and super.accepts(
            boss
        )
    }
}
