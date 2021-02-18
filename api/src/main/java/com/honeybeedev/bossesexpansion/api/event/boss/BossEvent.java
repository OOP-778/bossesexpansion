package com.honeybeedev.bossesexpansion.api.event.boss;

import com.honeybeedev.bossesexpansion.api.boss.Boss;
import com.honeybeedev.bossesexpansion.api.event.BEEvent;

public abstract class BossEvent implements BEEvent {

  private Boss boss;

  protected BossEvent(Boss boss) {
    this.boss = boss;
  }

  public Boss getBoss() {
    return boss;
  }
}
