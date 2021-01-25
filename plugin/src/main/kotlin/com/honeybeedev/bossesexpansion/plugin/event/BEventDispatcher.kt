package com.honeybeedev.bossesexpansion.plugin.event

import com.honeybeedev.bossesexpansion.api.event.BEEvent
import com.honeybeedev.bossesexpansion.api.event.EventDispatcher
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.function.Consumer
import kotlin.reflect.KClass

object BEventDispatcher : EventDispatcher {
    private val listeners: MutableMap<KClass<out BEEvent>, MutableList<Pair<JavaPlugin, (BEEvent) -> Unit>>> =
        hashMapOf()

    override fun <T : BEEvent> listen(listeningPlugin: JavaPlugin, eventClass: Class<T>, consumer: Consumer<T>) {
        listeners
            .computeIfAbsent(eventClass.kotlin) { LinkedList() }
            .add(Pair(listeningPlugin) { consumer.accept(it as T) })
    }

    fun <T : BEEvent> call(event: T) {
        listeners[event::class]?.let {
            for (pair in it.toTypedArray()) {
                if (!pair.first.isEnabled) {
                    it.remove(pair)
                    continue
                }

                pair.second(event)
            }
        }
    }
}