package org.royalix.bossesexpansion.api.boss;

import java.util.Set;
import java.util.function.Function;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface BossProvider {
  /*
  Return list of available bosses inside the provider
  */
  Set<String> listBosses();

  /*
  Provide an consumer for spawning specific boss
  */
  Function<Location, Entity> provideSpawner(String bossName);

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
