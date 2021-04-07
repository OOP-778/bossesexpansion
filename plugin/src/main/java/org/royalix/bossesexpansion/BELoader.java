package org.royalix.bossesexpansion;

import com.oop.inteliframework.dependency.Library;
import com.oop.inteliframework.dependency.relocation.Relocation;
import com.oop.orangeengine.main.plugin.EnginePlugin;
import com.oop.orangeengine.main.task.ClassicTaskController;
import com.oop.orangeengine.main.task.TaskController;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import org.royalix.bossesexpansion.api.BossesExpansion;

public class BELoader extends EnginePlugin {

  private static final String KOTLIN_VERSION = "1.4.32";
  public final BossesExpansion plugin;

  public BELoader() {
    LibManager libManager = new LibManager(this);
    if (!getDataFolder().exists())
      getDataFolder().mkdirs();

    libManager.addRepository(
        "https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-reflect");
    libManager.addRepository(
        "https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib");

    libManager.appendLib(Library.builder().classesPath("kotlin").from("org.jetbrains.kotlin:kotlin-stdlib:" + KOTLIN_VERSION).build());
    libManager.appendLib(Library.builder().classesPath("kotlin").from("org.jetbrains.kotlin:kotlin-reflect:" + KOTLIN_VERSION).build());

    libManager.relocate(
        lib -> lib.getClassesPath().contains("kotlin"),
        lib -> {
          Relocation kotlin = new Relocation(String.valueOf(new char[]{'k', 'o', 't', 'l', 'i', 'n'}), "org.royalix.bossesexpansion.lib.kotlin");
          System.out.println(kotlin);
          return kotlin;
        }
    );

    libManager.relocate(
        lib -> !lib.getClassesPath().equalsIgnoreCase("kotlin"),
        lib -> new Relocation(lib.getClassesPath(), "org.royalix.bossesexpansion.lib." + lib.getArtifactId())
    );

    libManager.load();

    try {
      Class<?> aClass = Class.forName("org.royalix.bossesexpansion.plugin.BossesExpansion");
      Constructor<?> declaredConstructor = aClass.getDeclaredConstructor(BELoader.class);
      declaredConstructor.setAccessible(true);

      plugin = (BossesExpansion) declaredConstructor.newInstance(this);
    } catch (Throwable throwable) {
      throw new IllegalStateException("Failed to load BossesExpansion class", throwable);
    }
  }

  @Override
  public void enable() {
    plugin.onEnable();
  }

  @Override
  public void disable() {
    plugin.onDisable();
  }

  @Override
  public TaskController provideTaskController() {
    return new ClassicTaskController(this);
  }
}
