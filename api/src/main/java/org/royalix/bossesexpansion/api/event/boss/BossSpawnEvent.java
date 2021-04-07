package org.royalix.bossesexpansion.api.event.boss;

import org.royalix.bossesexpansion.api.boss.Boss;

public class BossSpawnEvent extends BossEvent {
  public BossSpawnEvent(Boss boss) {
    super(boss);
  }
}
