package org.royalix.bossesexpansion.plugin.config.group.action

import com.oop.orangeengine.yaml.ConfigSection
import org.royalix.bossesexpansion.plugin.config.main.SummaryMessage

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
            org.royalix.bossesexpansion.plugin.BossesExpansion.instance!!.configController.mainConfig.defaultSummaryMessage
    }

    override fun accepts(boss: org.royalix.bossesexpansion.plugin.boss.BBoss): Boolean {
        return ((boss.damageMap.size < damagersRequired) or (damagersRequired == -1)) and super.accepts(
            boss
        )
    }
}
