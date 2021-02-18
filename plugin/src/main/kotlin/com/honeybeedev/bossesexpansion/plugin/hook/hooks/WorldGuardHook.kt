package com.honeybeedev.bossesexpansion.plugin.hook.hooks

import com.honeybeedev.bossesexpansion.api.event.boss.pre.BossPreSpawnEvent
import com.honeybeedev.bossesexpansion.plugin.boss.BBoss
import com.honeybeedev.bossesexpansion.plugin.hook.Hook
import com.honeybeedev.bossesexpansion.plugin.util.dispatchListen
import org.codemc.worldguardwrapper.WorldGuardWrapper

class WorldGuardHook : Hook("WorldGuard") {
    init {
        dispatchListen<BossPreSpawnEvent> {
            (boss as BBoss).spawnRegion = WorldGuardWrapper
                .getInstance()
                .getRegions(boss.location)
                .firstOrNull()

            debug("Found region for ${(boss as BBoss).internalName} to ${(boss as BBoss).spawnRegion}")
        }
    }
}