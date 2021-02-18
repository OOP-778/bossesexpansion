package com.honeybeedev.bossesexpansion.api.event.boss.pre;

import com.honeybeedev.bossesexpansion.api.boss.Boss;
import com.honeybeedev.bossesexpansion.api.event.boss.BossEvent;
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
