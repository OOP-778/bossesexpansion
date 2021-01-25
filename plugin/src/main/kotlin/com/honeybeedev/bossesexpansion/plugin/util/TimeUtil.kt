package com.honeybeedev.bossesexpansion.plugin.util

import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

fun toSeconds(string: String): Long {
    var string = string
    var seconds: Long = 0
    try {
        return string.toLong()
    } catch (ignored: Exception) {
    }
    string = string.toLowerCase()
    val characters = Arrays.stream(string.split(Regex("\\d")).toTypedArray())
        .filter { s: String -> s.isNotEmpty() }
        .collect(Collectors.toList())

    val numbers = listOf(*string.split(Regex("\\D")).toTypedArray())
    for (i in characters.indices) {
        val amount = numbers[i].toInt()
        when (characters[i]) {
            "s" -> seconds += amount.toLong()
            "m" -> seconds = TimeUnit.MINUTES.toSeconds(amount.toLong())
            "h" -> seconds = TimeUnit.HOURS.toSeconds(amount.toLong())
            "d" -> seconds = TimeUnit.DAYS.toSeconds(amount.toLong())
        }
    }
    return seconds
}

fun leftSimple(seconds: Long): String {
    val longs = calculateTime(seconds)
    val times: MutableList<String?> = ArrayList()
    times.add(longs[0].toString() + "s")
    times.add(longs[1].toString() + "m")
    times.add(longs[2].toString() + "h")
    times.add(longs[3].toString() + "d")
    times.reverse()

    times.removeIf { time: String? -> time!!.startsWith("0") }
    return java.lang.String.join(", ", times)
}

fun leftComplex(seconds: Long): String {
    val longs = calculateTime(seconds)
    val times: MutableList<String?> = ArrayList()
    times.add(longs[0].toString() + " Seconds")
    times.add(longs[1].toString() + " Minutes")
    times.add(longs[2].toString() + " Hours")
    times.add(longs[3].toString() + " Days")
    times.reverse()

    times.removeIf { time: String? -> time!!.startsWith("0") }
    return java.lang.String.join(", ", times)
}

fun calculateTime(seconds: Long): LongArray {
    val sec = seconds % 60
    val minutes = seconds % 3600 / 60
    val hours = seconds % 86400 / 3600
    val days = seconds / 86400
    return longArrayOf(sec, minutes, hours, days)
}

inline fun ZonedDateTime.apply(apply: (ZonedDateTime).() -> ZonedDateTime): ZonedDateTime =
    apply(this)

