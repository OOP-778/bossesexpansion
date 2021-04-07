package org.royalix.bossesexpansion.plugin.config

import com.oop.orangeengine.file.OFile
import com.oop.orangeengine.main.util.JarUtil
import com.oop.orangeengine.yaml.Config
import org.royalix.bossesexpansion.plugin.BossesExpansion
import org.royalix.bossesexpansion.plugin.config.group.BossGroup
import org.royalix.bossesexpansion.plugin.config.main.MainConfig
import org.royalix.bossesexpansion.plugin.config.reward.BeReward
import org.royalix.bossesexpansion.plugin.config.timed.TimedSpawner
import org.royalix.bossesexpansion.plugin.util.PluginComponent
import java.io.File

class ConfigController : PluginComponent {
    val groups: MutableMap<String, BossGroup> = hashMapOf()
    val rewards: MutableMap<String, BeReward> = hashMapOf()
    val timedSpawners: MutableMap<String, TimedSpawner> = hashMapOf()
    lateinit var mainConfig: MainConfig

    override fun load(): Boolean {
        // Initialize files
        if (!plugin.dataFolder.exists())
            plugin.dataFolder.mkdirs()

        val mainConfigFile = OFile(plugin.dataFolder, "config.yml").createIfNotExists(true)
        mainConfig = MainConfig(Config(mainConfigFile))

        // Load groups
        groups.clear()
        loadFiles("groups") {
            try {
                groups[it.nameWithoutExtension.toLowerCase()] = BossGroup(Config(it))
            } catch (ex: Throwable) {
                IllegalStateException("Failed to load group in file ${it.name}", ex).printStackTrace()
            }
        }

        rewards.clear()
        loadFiles("rewards") {
            try {
                rewards[it.nameWithoutExtension.toLowerCase()] = BeReward(Config(it))
            } catch (ex: Throwable) {
                IllegalStateException("Failed to load reward in file ${it.name}", ex).printStackTrace()
            }
        }

        loadFiles("timedspawn") {
            try {
                val timedSpawnerConfig = Config(it)
                timedSpawners[it.nameWithoutExtension.toLowerCase()] =
                    TimedSpawner(timedSpawnerConfig)
                timedSpawnerConfig.save()
            } catch (ex: Throwable) {
                IllegalStateException("Failed to load timed spawner in file ${it.name}", ex).printStackTrace()
            }
        }

        print("Loaded ${groups.size} groups!")
        print("Loaded ${rewards.size} rewards!")
        print("Loaded ${timedSpawners.size} timed spawners!")
        return true
    }

    private fun loadFiles(folderName: String, c: (File) -> Unit) {
        JarUtil.copyFolderFromJar(
            folderName,
            plugin.dataFolder,
            JarUtil.CopyOption.COPY_IF_NOT_EXIST,
            BossesExpansion::class.java
        )

        val folder = File(plugin.dataFolder.toPath().toString() + "/$folderName")
        if (!folder.exists())
            folder.mkdir()

        for (file in folder.listFiles())
            c(file)
    }
}
