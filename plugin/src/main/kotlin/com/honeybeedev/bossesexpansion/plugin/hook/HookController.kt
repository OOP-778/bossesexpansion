package com.honeybeedev.bossesexpansion.plugin.hook

import com.honeybeedev.bossesexpansion.plugin.util.PluginComponent
import java.util.*
import java.util.function.Consumer
import kotlin.reflect.KClass

class HookController : PluginComponent {
    private val hooks: MutableMap<KClass<out Hook>, Hook> = HashMap()

    fun registerHooks(vararg hooks: () -> KClass<out Hook>) {
        for (clazzSupplier in hooks) {
            val clazz = getClazz(clazzSupplier)
            try {
                val hook: Hook = clazz!!.java.newInstance()
                if (!hook.loaded)
                    if (!hook.required) continue else throw IllegalStateException("Failed to hook into ${hook.pluginName} because it's not loaded and it is required!")

                this.hooks[clazz] = hook
                logger.print("Hooked into (${hook.pluginName}) version ${hook.hookPlugin!!.description.version}")
            } catch (throwable: Throwable) {
                if (throwable is IllegalStateException || throwable is NullPointerException) throw IllegalStateException(
                    "Failed to start HookController",
                    throwable
                )
            }
        }
    }

    fun <T : Hook> useIfPresent(supplier: () -> KClass<T>, consumer: Consumer<T>?) =
        findHook(supplier).ifPresent(consumer!!)

    fun <T : Hook> useIfPresent(supplier: () -> KClass<T>, runnable: Runnable) =
        useIfPresent(supplier, Consumer { hook: T -> runnable.run() })

    fun <T : Hook> findHook(supplier: () -> KClass<T>): Optional<T> {
        val clazz = getClazz(supplier) ?: return Optional.empty()
        return Optional.ofNullable(hooks[clazz]) as Optional<T>
    }

    fun disableIf(hook: Hook, b: Boolean, s: String) {
        hooks.remove(hook::class)
        if (b) {
            logger.printWarning("${hook.pluginName} failed to hook, cause: $s")
        }
    }

    fun getClazz(supplier: () -> KClass<out Hook>): KClass<out Hook>? {
        return try {
            supplier()
        } catch (ex: Throwable) {
            null
        }
    }
}
