package com.honeybeedev.bossesexpansion.api.controller;

import com.honeybeedev.bossesexpansion.api.boss.Boss;
import com.honeybeedev.bossesexpansion.api.boss.BossProvider;
import org.bukkit.entity.Entity;

import java.util.Set;
import java.util.UUID;

public interface BossController {
    /*
    Get set of current tracked bosses
    */
    Set<Boss> getBosses();

    /*
    Call boss on death
    */
    void onDeath(Entity entity, BossProvider provider);

    /*
    Call boss spawn
    */
    void onSpawn(Entity entity, Entity parent, BossProvider provider);

    /*
    Call boss despawn
    */
    void onDespawn(Entity entity);

    /*
    Get a boss from entity
    */
    Boss getBoss(Entity entity);

    /*
    Get a boss from unique identifier
    */
    Boss getBoss(UUID uuid);
}
