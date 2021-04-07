package org.royalix.bossesexpansion.api;

import org.royalix.bossesexpansion.api.boss.BossController;
import org.royalix.bossesexpansion.api.boss.BossProviderController;
import org.royalix.bossesexpansion.api.event.EventDispatcher;
import org.bukkit.plugin.Plugin;

public interface BossesExpansion {

  /*
  Get the provider of bosses
  */
  BossProviderController getBossProviderController();

  /*
  Get boss controller
  */
  BossController getBossController();

  /*
  Get event dispatcher
  */
  EventDispatcher getEventDispatcher();

  void onDisable();

  void onEnable();
}
