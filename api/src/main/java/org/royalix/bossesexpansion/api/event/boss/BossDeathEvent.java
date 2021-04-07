package org.royalix.bossesexpansion.api.event.boss;

import org.royalix.bossesexpansion.api.boss.Boss;

public class BossDeathEvent extends BossEvent {
  public BossDeathEvent(Boss boss) {
    super(boss);
  }
}
