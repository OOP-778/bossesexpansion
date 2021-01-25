package com.honeybeedev.bossesexpansion.plugin.constant

import com.honeybeedev.bossesexpansion.plugin.util.newChatMessage
import com.oop.orangeengine.message.OMessage
import com.oop.orangeengine.message.impl.OChatMessage
import com.oop.orangeengine.message.impl.chat.LineContent

enum class Lang(val message: OMessage<*>) {
    PREFIX("&a&lBE | &f"),
    PREFIX_ERROR("&c&lBE | &4"),
    PLUGIN_RELOADED("Plugin was successfully reloaded!"),
    TIMEDSPAWNERS_INFO(
        newChatMessage {
            append("List of active Timed Spawners")
            append("{TEMPLATE}&a&l- &a{timedspawner_id} &frunning in &a{timedspawner_left_complex}")
        }
    )
    ;

    val cache: Array<OMessage<*>?> = arrayOfNulls(2)

    constructor(vararg list: String) : this(OChatMessage(*list))

    open fun normalMessage(): OMessage<*> {
        return if (message is OChatMessage) {
            if (cache[0] == null) {
                val chatMessage = message.clone()
                chatMessage.lineList()[0].insert(
                    LineContent(
                        PREFIX.message.asChat().lineList()
                            .get(0).raw()
                    ), 0
                )
                cache[0] = chatMessage
                chatMessage
            } else cache[0]!!
        } else message
    }

    open fun errorMessage(): OMessage<*> {
        return if (message is OChatMessage) {
            if (cache[0] == null) {
                val chatMessage = message.clone()
                chatMessage.lineList()[0].insert(
                    LineContent(
                        PREFIX_ERROR.message.asChat().lineList()
                            .get(0).raw()
                    ), 0
                )
                cache[0] = chatMessage
                chatMessage
            } else cache[0]!!
        } else message
    }
}