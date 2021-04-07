package org.royalix.bossesexpansion.api.boss;

import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;

public interface Boss {
  /** Add damage to a player */
  void addDamage(UUID uuid, double amount);

  /**
   * Get damage of player
   *
   * @return Optional can be empty if player has no damage
   */
  Optional<Double> getDamage(UUID uuid);

  /** Get provider of the boss */
  BossProvider getProvider();

  /** Get total damage done */
  double getTotalDamage();

  /** Get location of the boss */
  Location getLocation();

  /** Get uuid of the boss */
  UUID getUUID();
}
