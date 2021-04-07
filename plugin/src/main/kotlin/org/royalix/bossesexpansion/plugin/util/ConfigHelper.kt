package org.royalix.bossesexpansion.plugin.util

import com.oop.orangeengine.yaml.interfaces.Pathable

fun prepareError(valueable: Pathable, message: String): IllegalStateException {
    return IllegalStateException("at ${valueable.path}: $message")
}
