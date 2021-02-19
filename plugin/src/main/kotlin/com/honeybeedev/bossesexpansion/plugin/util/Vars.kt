package com.honeybeedev.bossesexpansion.plugin.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class NullableVar<T : Any> : ReadWriteProperty<Any?, T?> {
    private var value: T? = null

    fun isSet(): Boolean =
        value != null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return value
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.value = value
    }
}
