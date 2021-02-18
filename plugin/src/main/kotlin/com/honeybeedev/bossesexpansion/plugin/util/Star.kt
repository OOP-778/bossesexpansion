package com.honeybeedev.bossesexpansion.plugin.util

import kotlin.reflect.KClass

data class Star<T : Any>(
    var clazz: KClass<T>,
    var provider: (String) -> T,
    var isStar: Boolean = false,
    var value: T? = null
) {
    protected constructor(parent: Star<T>, isStar: Boolean) : this(
        parent.clazz,
        parent.provider,
        isStar
    )

    protected constructor(parent: Star<T>, value: T) : this(
        parent.clazz,
        parent.provider,
        false,
        value
    )

    fun parse(`object`: String): Star<T> {
        return if (`object`.equals("*", ignoreCase = true) || `object`.equals(
                "all",
                ignoreCase = true
            )
        ) Star(
            this,
            true
        ) else {
            try {
                Star(this, provider(`object`))
            } catch (throwable: Throwable) {
                throw IllegalStateException("Failed to parse " + `object` + " into " + clazz.simpleName)
            }
        }
    }

    companion object {
        val stringParser: Star<String> =
            Star(String::class, { string: String -> string })
    }
}
