package com.honeybeedev.bossesexpansion.api.boss;

import org.bukkit.Location;

import java.util.Optional;
import java.util.UUID;

public interface Boss {
    /*
    Add damage to a player
    */
    void addDamage(UUID uuid, double amount);

    /**
    Get damage of player
    @return Optional can be empty if player has no damage
    */
    Optional<Double> getDamage(UUID uuid);

    /*
    Get provider of the boss
    */
    BossProvider getProvider();

    /*
    Get total damage done
    */
    double getTotalDamage();

    /*
    Get location of the boss
    */
    Location getLocation();
}
