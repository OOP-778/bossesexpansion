package com.honeybeedev.bossesexpansion.api;

public class BossesExpansionAPI {
    private static BossesExpansion plugin;

    public static void set(BossesExpansion plugin1) {
        plugin = plugin1;
    }

    public static BossesExpansion getPlugin() {
        return plugin;
    }
}
