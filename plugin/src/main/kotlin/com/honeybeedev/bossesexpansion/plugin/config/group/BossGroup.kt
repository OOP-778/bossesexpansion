package com.honeybeedev.bossesexpansion.plugin.config.group

import com.honeybeedev.bossesexpansion.plugin.BossesExpansion
import com.honeybeedev.bossesexpansion.plugin.config.group.action.AbstractAction
import com.honeybeedev.bossesexpansion.plugin.config.group.action.GroupAction
import com.oop.orangeengine.file.OFile
import com.oop.orangeengine.yaml.Config
import java.io.File
import kotlin.reflect.KClass

data class BossGroup(
    val config: Config,
    override val id: String = config.file.file.nameWithoutExtension.toLowerCase(),
    override val filters: MutableMap<String, Pair<GroupFilter, StringStarSection>> = hashMapOf(),
    val actions: MutableMap<KClass<out AbstractAction>, AbstractAction?> = hashMapOf()
) : GroupFilterable {
    init {
        loadFiltersFrom(config)
        actions.putAll(GroupAction.load(config.createSection("actions")).map { Pair(it::class, it) })
    }

    constructor(id: String) : this({
        val groupsFolder = File(BossesExpansion.instance!!.dataFolder.toPath().toString() + "/groups")
        val createIfNotExists = OFile(File(groupsFolder, "$id.yml")).createIfNotExists()
        Config(createIfNotExists)
    }())

    inline fun <reified T : AbstractAction> action(insertIfAbsent: Boolean = false): T? {
        var action = actions[T::class]
        action?.let {
            if (!insertIfAbsent) return@let
            action = T::class.constructors.first().call()
            actions[T::class] = action!!
        }

        return action as T?
    }
}