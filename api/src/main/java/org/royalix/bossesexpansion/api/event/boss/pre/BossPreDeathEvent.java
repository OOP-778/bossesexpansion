package org.royalix.bossesexpansion.api.event.boss.pre;

import org.royalix.bossesexpansion.api.boss.Boss;
import org.royalix.bossesexpansion.api.event.boss.BossEvent;
import org.bukkit.event.Cancellable;

public class BossPreDeathEvent extends BossEvent implements Cancellable {

  private boolean cancelled;

  public BossPreDeathEvent(Boss boss) {
    super(boss);
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean b) {
    this.cancelled = b;
  }
}
