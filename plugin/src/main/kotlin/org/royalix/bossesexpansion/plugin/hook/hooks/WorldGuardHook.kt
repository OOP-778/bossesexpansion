package org.royalix.bossesexpansion.plugin.hook.hooks

import org.bukkit.Bukkit
import org.bukkit.World
import org.codemc.worldguardwrapper.WorldGuardWrapper
import org.codemc.worldguardwrapper.region.IWrappedRegion
import org.royalix.bossesexpansion.api.event.boss.pre.BossPreSpawnEvent
import org.royalix.bossesexpansion.plugin.hook.Hook
import org.royalix.bossesexpansion.plugin.util.dispatchListen

class WorldGuardHook : Hook("WorldGuard") {
    init {
        dispatchListen<BossPreSpawnEvent> {
            (boss as org.royalix.bossesexpansion.plugin.boss.BBoss).spawnRegion = WorldGuardWrapper
                .getInstance()
                .getRegions(boss.location)
                .firstOrNull()

            debug("Found region for ${(boss as org.royalix.bossesexpansion.plugin.boss.BBoss).internalName} to ${(boss as org.royalix.bossesexpansion.plugin.boss.BBoss).spawnRegion}")
        }
    }

    fun getRegion(name: String, world: World?): IWrappedRegion? {
        if (world != null)
            return WorldGuardWrapper.getInstance().getRegion(world, name).orElse(null)

        return getRegions()
            .firstOrNull { it.id!!.equals(name, true) }
    }

    fun getRegions(): Set<IWrappedRegion> {
        return Bukkit.getWorlds()
            .flatMap { WorldGuardWrapper.getInstance().getRegions(it).values }
            .toMutableSet()
    }

}
