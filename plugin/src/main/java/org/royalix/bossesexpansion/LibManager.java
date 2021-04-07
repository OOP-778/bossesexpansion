package org.royalix.bossesexpansion;

import com.oop.inteliframework.dependency.common.CommonLibraryManager;
import com.oop.inteliframework.dependency.logging.adapters.JDKLogAdapter;
import java.net.URLClassLoader;
import java.nio.file.Path;
import org.bukkit.plugin.java.JavaPlugin;

public class LibManager extends CommonLibraryManager {

  public LibManager(JavaPlugin plugin) {
    super(new JDKLogAdapter(plugin.getLogger()), (URLClassLoader) plugin.getClass().getClassLoader(), plugin.getDataFolder());
  }
}
