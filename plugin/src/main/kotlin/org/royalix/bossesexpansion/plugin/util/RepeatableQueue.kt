package org.royalix.bossesexpansion.plugin.util

import com.google.common.collect.Sets

class RepeatableQueue<V>(private val array: Array<V>) {
    private val size: Int
    private var index = 0

    fun poll(): V {
        if (index == size) reset()
        val value = array[index]
        index++
        return value
    }

    val isEmpty: Boolean
        get() = index == size

    fun reset() {
        index = 0
    }

    fun size(): Int {
        return size
    }

    fun toSet(): Set<V> {
        return Sets.newHashSet(*array)
    }

    fun array(): Array<V> {
        return array
    }

    init {
        size = array.size
    }
}
