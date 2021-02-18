package com.honeybeedev.bossesexpansion.plugin.util

import com.oop.orangeengine.message.OMessage

inline fun <reified T : OMessage<*>> T.parse(vararg objs: Any): T {
    Placeholders.parsePlaceholders(objs).forEach {
        replace(it.key, it.value)
    }
    return this
}
