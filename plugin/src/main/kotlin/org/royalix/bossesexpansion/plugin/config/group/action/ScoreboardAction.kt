package org.royalix.bossesexpansion.plugin.config.group.action

import com.oop.orangeengine.yaml.ConfigSection
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.royalix.bossesexpansion.plugin.util.NullableVar
import org.royalix.bossesexpansion.plugin.util.ReceiversSuppliers

class ScoreboardAction(section: ConfigSection) : AbstractAction(section) {
    override val actionName: String = "scoreboard"

    var receivers by NullableVar<(Location?) -> Collection<CommandSender>>()
    var title by NullableVar<String>()
    var showDamagers by NullableVar<Int>()
    var lines by NullableVar<List<String>>()

    init {
        receivers = ReceiversSuppliers.parseSupplier(section.getAs("to"))
        title = section.getAs("title")
        showDamagers = section.getAs("show damagers")
        lines = section.getAs("lines")
    }
}
