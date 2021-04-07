package org.royalix.bossesexpansion.api.event.boss;

import org.royalix.bossesexpansion.api.boss.Boss;
import org.royalix.bossesexpansion.api.event.BEEvent;

public abstract class BossEvent implements BEEvent {

  private Boss boss;

  protected BossEvent(Boss boss) {
    this.boss = boss;
  }

  public Boss getBoss() {
    return boss;
  }
}
