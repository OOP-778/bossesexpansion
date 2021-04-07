package org.royalix.bossesexpansion.plugin.config.timed

import org.royalix.bossesexpansion.plugin.util.WrappedNumber
import org.royalix.bossesexpansion.plugin.util.parseNumber
import java.util.regex.Pattern

val boss = Pattern.compile("boss\\[([^,]*),([^]]+)\\]")
fun parse(input: String): Pair<WrappedNumber, String> {
    val matcher = boss.matcher(input)
    if (!matcher.find()) throw IllegalStateException("Failed to find boss by $input")

    val bossName = matcher.group(1)
    val number = matcher.group(2)

    return Pair(parseNumber(number), bossName)
}
