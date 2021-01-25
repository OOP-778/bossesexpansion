package com.honeybeedev.bossesexpansion.api.event;

import com.honeybeedev.bossesexpansion.api.boss.Boss;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class BossEvent implements BEEvent {

    private Boss boss;

    protected BossEvent(Boss boss) {
        this.boss = boss;
    }

    public Boss getBoss() {
        return boss;
    }
}
