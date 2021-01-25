package com.honeybeedev.bossesexpansion.plugin.util

import com.google.common.base.Preconditions
import java.util.concurrent.ThreadLocalRandom
import java.util.regex.Pattern

val minRegex = Pattern.compile("min\\(([0-9]+)\\)")
val rangeRegex = Pattern.compile("range\\(([0-9]+),([0-9]+)\\)")
val randRegex = Pattern.compile("rand\\(([0-9]+),([0-9]+)\\)")

data class WrappedNumber(val test: (Int) -> Boolean, val serialized: String, val getter: () -> Int)

fun parseNumber(_input: String, vararg disabledParsers: String): WrappedNumber {
    val input = _input.replace(Regex("\\s+"), "").toLowerCase()

    // Parse min
    if (input.startsWith("min") && "min" !in disabledParsers) {
        val matcher = minRegex.matcher(input)
        if (matcher.find()) {
            val place = matcher.group(1).toInt()
            return WrappedNumber({ it >= place }, "min($place)") { place }
        } else
            throw IllegalStateException("Failed to parse min(x) for place $input")
    }

    // Parse range
    if (input.startsWith("range") && "range" !in disabledParsers) {
        val matcher = rangeRegex.matcher(input)
        if (matcher.find()) {
            val min = matcher.group(1).toInt()
            val max = matcher.group(2).toInt()
            Preconditions.checkArgument(min < max, "Max must be higher than min for $input")

            val range = min..max
            return WrappedNumber({ range.contains(it) }, "range($min, $max)") { max }
        } else
            throw IllegalStateException("Failed to parse range(min, max) for place $input")
    }

    if (input.startsWith("rand") && "rand" !in disabledParsers) {
        val matcher = randRegex.matcher(input)
        if (matcher.find()) {
            val min = matcher.group(1).toInt()
            val max = matcher.group(2).toInt()
            Preconditions.checkArgument(min < max, "Max must be higher than min for $input")

            return WrappedNumber(
                { throw IllegalStateException("Function not supported") },
                "rand($min, $max)"
            ) { ThreadLocalRandom.current().nextInt(min, max) }
        }
    }

    if (input == "any" || input == "*" && "any" !in disabledParsers) {
        return WrappedNumber({ true }, "any") { Int.MAX_VALUE }
    }

    if (input.matches(Regex("^[0-9]+\$"))) {
        val place = input.toInt()
        return WrappedNumber({ it == place }, "$place") { place }
    } else throw IllegalStateException("Failed to parse number for $input")
}
