package com.honeybeedev.bossesexpansion.api.event;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public interface EventDispatcher {
    <T extends BEEvent> void listen(JavaPlugin listeningPlugin, Class<T> eventClass, Consumer<T> consumer);
}
