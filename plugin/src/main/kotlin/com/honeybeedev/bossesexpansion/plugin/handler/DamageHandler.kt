package com.honeybeedev.bossesexpansion.plugin.handler

import com.honeybeedev.bossesexpansion.plugin.BossesExpansion
import com.honeybeedev.bossesexpansion.plugin.util.BEComponent
import com.honeybeedev.bossesexpansion.plugin.util.listen
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent

object DamageHandler : BEComponent {
    init {
        listen<EntityDamageByEntityEvent> {
            val damager = findDamager(this)
            damager?.let {
                // Check if the entity is instance of boss
                BossesExpansion.instance!!._bossController
                    .getBoss(entity)?.let {
                        val finalDamage = finalDamage.coerceAtMost((entity as LivingEntity).health)
                        it.addDamage(damager.uniqueId, finalDamage)
                    }
            }
        }
    }

    private fun findDamager(event: EntityDamageByEntityEvent): Player? {
        // If the damager is player
        if (event.damager is Player)
            return event.damager as Player

        // If the damager is projectile
        return if (event.damager is Projectile && (event.damager as Projectile).shooter is Player) (event.damager as Projectile).shooter as Player
        else null
    }
}