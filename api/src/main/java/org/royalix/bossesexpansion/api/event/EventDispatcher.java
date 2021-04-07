package org.royalix.bossesexpansion.api.event;

import java.util.function.Consumer;
import org.bukkit.plugin.java.JavaPlugin;

public interface EventDispatcher {
  <T extends BEEvent> void listen(
      JavaPlugin listeningPlugin, Class<T> eventClass, Consumer<T> consumer);

  <T extends BEEvent>void call(T event);
}
