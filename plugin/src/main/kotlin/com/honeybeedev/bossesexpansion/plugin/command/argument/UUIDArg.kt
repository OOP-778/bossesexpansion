package com.honeybeedev.bossesexpansion.plugin.command.argument

import com.oop.orangeengine.command.arg.CommandArgument
import com.oop.orangeengine.command.arg.CommandArgument.ArgumentMapper
import com.oop.orangeengine.main.util.data.pair.OPair
import java.util.*

class UUIDArg : CommandArgument<UUID>() {
    init {
        identity = "uuid"
        description = "UUID"
        mapper = ArgumentMapper {
            var uuid: UUID? = null
            try {
                uuid = UUID.fromString(it)
            } catch (err: Throwable) {
            }
            OPair(uuid, "failed to parse uuid from $it")
        }
    }
}
