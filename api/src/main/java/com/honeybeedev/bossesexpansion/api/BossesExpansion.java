package com.honeybeedev.bossesexpansion.api;

import com.honeybeedev.bossesexpansion.api.boss.BossController;
import com.honeybeedev.bossesexpansion.api.boss.BossProviderController;
import com.honeybeedev.bossesexpansion.api.event.EventDispatcher;

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
}
