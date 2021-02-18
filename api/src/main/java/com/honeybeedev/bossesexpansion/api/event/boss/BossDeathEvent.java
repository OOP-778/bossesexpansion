package com.honeybeedev.bossesexpansion.api.event.boss;

import com.honeybeedev.bossesexpansion.api.boss.Boss;

public class BossDeathEvent extends BossEvent {
  public BossDeathEvent(Boss boss) {
    super(boss);
  }
}
