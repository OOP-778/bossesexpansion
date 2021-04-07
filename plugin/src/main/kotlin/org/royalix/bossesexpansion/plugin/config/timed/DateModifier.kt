package org.royalix.bossesexpansion.plugin.config.timed

import java.time.DayOfWeek
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters
import java.util.regex.Pattern

object DateModifier {
    val daysRegex = Pattern.compile("^days\\(([1-7])\\)$")
    val weeksRegex = Pattern.compile("^weeks\\(([1-4]),([1-7])\\)$")
    val monthsRegex = Pattern.compile("^months\\(([1-12]),([1-4]),([1-7])\\)$")

    fun parse(input: String): (ZonedDateTime) -> ZonedDateTime {
        val _input = input.toLowerCase().trim()

        // Try to parse days
        var matcher = daysRegex.matcher(_input)
        if (matcher.find()) {
            val days = matcher.group(1).toInt()
            return { it.plusDays(days.toLong()) }
        }

        matcher = weeksRegex.matcher(_input)
        if (matcher.find()) {
            val weeks = matcher.group(1).toInt()
            val day = matcher.group(2).toInt()
            return {
                it.plusWeeks(weeks.toLong())
                    .with(TemporalAdjusters.nextOrSame(DayOfWeek.of(day)))
            }
        }

        matcher = monthsRegex.matcher(_input)
        if (matcher.find()) {
            val months = matcher.group(1).toInt()
            val week = matcher.group(2).toInt()
            val day = matcher.group(3).toInt()
            return {
                it.plusMonths(months.toLong())
                    .withDayOfMonth(0)
                    .plusWeeks(week.toLong())
                    .with(TemporalAdjusters.nextOrSame(DayOfWeek.of(day)))
            }
        }

        throw IllegalStateException("Failed to find correct time modifier by $input")
    }
}
