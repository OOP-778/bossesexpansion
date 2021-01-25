package com.honeybeedev.bossesexpansion.plugin.util

import com.oop.orangeengine.message.impl.OChatMessage
import com.oop.orangeengine.message.impl.chat.ChatLine

fun newChatMessage(): OChatMessage = OChatMessage()

fun newChatMessage(consumer: (OChatMessage).() -> Unit): OChatMessage {
    return newChatMessage().apply {
        consumer(this)
    }
}
