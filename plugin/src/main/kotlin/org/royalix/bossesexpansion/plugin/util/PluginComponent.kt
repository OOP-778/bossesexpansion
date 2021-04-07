package org.royalix.bossesexpansion.plugin.util

import com.oop.orangeengine.main.logger.OLogger
import com.oop.orangeengine.main.plugin.OComponent
import org.royalix.bossesexpansion.BELoader

interface PluginComponent : OComponent<BELoader> {
    val logger: OLogger
        get() = plugin.oLogger

    fun print(`object`: Any, vararg args: Any?) {
        logger.print("<$name>: $`object`", args)
    }

    fun error(`object`: Any, vararg args: Any?) {
        logger.printError("<$name>: $`object`", args)
    }

    fun warning(`object`: Any, vararg args: Any?) {
        logger.printWarning("<$name>: $`object`", args)
    }

    fun debug(`object`: Any, vararg args: Any?) {
        logger.printDebug("<$name>: $`object`", args)
    }
}
