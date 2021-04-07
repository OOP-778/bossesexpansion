package org.royalix.bossesexpansion.plugin.util

import org.bukkit.Location
import kotlin.math.max
import kotlin.math.min

fun getChunkPairsBetween(pos1: Location, pos2: Location): List<Pair<Int, Int>> {
    val list = arrayListOf<Pair<Int, Int>>()

    val c1x = pos1.blockX shr 4
    val c1z = pos1.blockZ shr 4

    val c2x = pos2.blockX shr 4
    val c2z = pos2.blockZ shr 4

    val minX = min(c1x, c2x)
    val minZ = min(c1z, c2z)

    val maxX = max(c1x, c2x)
    val maxZ = max(c1z, c2z)

    for (x in minX..maxX)
        for (z in minZ..maxZ)
            list.add(Pair(x, z))

    return list
}
