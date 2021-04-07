package org.royalix.bossesexpansion.plugin.handler

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.royalix.bossesexpansion.plugin.util.PluginComponent
import org.royalix.bossesexpansion.plugin.util.listen

object DamageHandler : PluginComponent {
    init {
        listen<EntityDamageByEntityEvent> {
            val damager = findDamager(this)
            damager?.let {
                // Check if the entity is instance of boss
                org.royalix.bossesexpansion.plugin.BossesExpansion.instance!!._bossController
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
