package com.honeybeedev.bossesexpansion.plugin.util

import com.honeybeedev.bossesexpansion.api.event.BEEvent
import com.honeybeedev.bossesexpansion.plugin.BossesExpansion
import com.oop.orangeengine.eventssubscription.SubscriptionFactory
import com.oop.orangeengine.eventssubscription.SubscriptionProperties
import com.oop.orangeengine.eventssubscription.subscription.SubscribedEvent
import com.oop.orangeengine.main.events.SyncEvents
import com.oop.orangeengine.main.task.OTask
import org.bukkit.event.Event
import java.text.DecimalFormat

val damageFormat = DecimalFormat("#.#")

fun executeTask(
    name: String,
    delay: Int = -1,
    async: Boolean = false,
    repeat: Boolean = false,
    consumer: (OTask) -> Unit,

): OTask {
    val task = OTask()
    task.apply {
        task.sync(!async)
        task.repeat(repeat)
        task.delay(delay)
        task.consumer {
            try {
                consumer(it)
            } catch (er: Throwable) {
                logger.error(IllegalStateException("Failed to execute task by name `$name`", er))
            }
        }
        task.execute()
    }
    return task
}

inline fun <reified T : Event> subscribe(
    props: SubscriptionProperties<T>,
    crossinline listener: T.() -> Unit
): SubscribedEvent<T> {
    return SubscriptionFactory.getInstance().subscribeTo(T::class.java, { listener(it) }, props)
}

inline fun <reified T : Event> listen(crossinline listener: (T).() -> Unit) {
    SyncEvents.listen(T::class.java) {
        listener(it)
    }
}

inline fun <reified T : BEEvent> dispatchListen(crossinline listener: (T).() -> Unit) {
    val instance = BossesExpansion.instance!!
    instance.eventDispatcher.listen(instance, T::class.java) {
        try {
            listener(it)
        } catch (err: Throwable) {
            logger.error(IllegalStateException("Failed to dispatch listener by type `${T::class.simpleName}`", err))
        }
    }
}

val logger = BossesExpansion.instance!!.oLogger
