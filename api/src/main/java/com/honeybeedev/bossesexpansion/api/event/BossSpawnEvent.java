package com.honeybeedev.bossesexpansion.api.event;

import com.honeybeedev.bossesexpansion.api.boss.Boss;

public class BossSpawnEvent extends BossEvent {
    public BossSpawnEvent(Boss boss) {
        super(boss);
    }
}
