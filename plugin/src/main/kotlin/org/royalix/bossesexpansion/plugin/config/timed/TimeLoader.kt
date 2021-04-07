package org.royalix.bossesexpansion.plugin.config.timed

import com.oop.orangeengine.yaml.Config
import org.royalix.bossesexpansion.plugin.util.NullableVar
import org.royalix.bossesexpansion.plugin.util.calculateTime
import org.royalix.bossesexpansion.plugin.util.toSeconds
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.properties.Delegates

class TimeLoader(section: Config) {
    var zone by NullableVar<ZoneId>()
    var dateModifier: ((ZonedDateTime) -> ZonedDateTime)? = null
    var timeModifier: ((ZonedDateTime) -> ZonedDateTime) by Delegates.notNull()

    var hash: Int by Delegates.notNull()

    init {
        section.ensureHasValues("time")

        var _every: String? = null
        val _time: String = section.getAs("time")

        if (section.isValuePresent("every")) {
            _every = section.getAs("every")
            dateModifier = DateModifier.parse(_every!!)
        }

        val calculateTime = calculateTime(toSeconds(_time))
        timeModifier = {
            it.plusHours(calculateTime[2])
                .plusMinutes(calculateTime[1])
                .plusSeconds(calculateTime[0])
        }

        section.ifValuePresent("timezone", String::class.java) {
            try {
                zone = ZoneId.of(it)
            } catch (ex: IllegalStateException) {
                throw IllegalStateException("Failed to find zone id by $it")
            }
        }

        if (zone == null)
            zone = ZoneId.systemDefault()

        hash = Objects.hash(zone?.id, _every, _time)
    }

    fun current(): ZonedDateTime =
        ZonedDateTime
            .now(zone)

    fun next(): ZonedDateTime {
        var date = if (dateModifier == null) current() else currentTruncated()
        date = timeModifier(date)

        dateModifier?.let {
            date = it(date)
        }

        return date
    }

    fun currentTruncated(): ZonedDateTime =
        current()
            .withHour(0)
            .withSecond(0)
            .withMinute(0)
}
