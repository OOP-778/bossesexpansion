package com.honeybeedev.bossesexpansion.plugin.config.group

import com.honeybeedev.bossesexpansion.plugin.boss.BBoss

enum class GroupFilter(val description: String, val filter: (BBoss, StringStarSection) -> Boolean) {
    WORLD("Filter worlds", { boss, sts ->
        sts.accepts(boss.location.world.name)
    }),
    REGION("Filter regions", { boss, sts ->
        boss.spawnRegion?.let { sts.accepts(it.id) } ?: true
    }),
    BOSS("Filter bosses", { boss, sts ->
        sts.accepts(boss.internalName) || sts.accepts(boss.displayName)
    })
}