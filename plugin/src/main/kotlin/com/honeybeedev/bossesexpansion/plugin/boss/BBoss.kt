package com.honeybeedev.bossesexpansion.plugin.boss

import com.honeybeedev.bossesexpansion.api.boss.Boss
import com.honeybeedev.bossesexpansion.api.boss.BossProvider
import com.honeybeedev.bossesexpansion.api.boss.BossSpawnType
import com.honeybeedev.bossesexpansion.plugin.config.group.BossGroup
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.codemc.worldguardwrapper.region.IWrappedRegion
import java.util.*
import java.util.concurrent.ConcurrentHashMap

data class BBoss(val entity: Entity, val _provider: BossProvider) : Boss {
    val spawnRegion: IWrappedRegion? = null
    val children: Queue<Entity> = LinkedList()
    var bossGroup: BossGroup? = null

    var damageMap: MutableMap<UUID, BossDamager> = ConcurrentHashMap()
    private var _totalDamage: Double = 0.0

    var displayName: String =
        provider.getDisplayName(entity)

    var internalName: String =
        provider.getInternalName(entity)

    override fun addDamage(uuid: UUID, amount: Double) {
        _totalDamage += amount
        damageMap
            .computeIfAbsent(uuid) { BossDamager(uuid, 0.0, this) }
            .damage += amount
    }

    override fun getDamage(uuid: UUID?): Optional<Double> = Optional.ofNullable(damageMap[uuid!!]!!.damage)
    override fun getProvider(): BossProvider = _provider
    override fun getTotalDamage(): Double = _totalDamage
    override fun getLocation(): Location = entity.location
    override fun getUUID(): UUID = entity.uniqueId

    fun sortDamagers(): Map<UUID, BossDamager> {
        damageMap = damageMap.entries
            .sortedByDescending { it.value.damage }
            .toList()
            .associateBy({ it.key }, { it.value })
            .toMutableMap()

        // Set positions for boss damagers
        damageMap
            .entries
            .forEachIndexed { pos, entry -> entry.value.position.setValue(pos+1) }

        return damageMap
    }
}
