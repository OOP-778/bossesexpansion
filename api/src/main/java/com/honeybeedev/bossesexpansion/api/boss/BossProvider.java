package com.honeybeedev.bossesexpansion.api.boss;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.Set;
import java.util.function.Consumer;

public interface BossProvider {
    /*
    Return list of available bosses inside the provider
    */
    Set<String> listBosses();

    /*
    Provide an consumer for spawning specific boss
    */
    Consumer<Location> provideSpawner(String bossName);

    /*
    Getting display name of the boss
    Mostly used for messages
    */
    String getDisplayName(Entity entity);

    /*
    Getting internal name of the boss
    It's an identifier of the boss
    */
    String getInternalName(Entity entity);

    /*
    Get identifier of the provider
    */
    String getIdentifier();
}
