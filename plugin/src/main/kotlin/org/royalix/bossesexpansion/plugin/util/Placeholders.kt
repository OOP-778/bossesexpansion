package org.royalix.bossesexpansion.plugin.util

import com.google.common.collect.Maps
import com.google.common.collect.Sets
import com.oop.orangeengine.main.util.data.pair.OPair
import com.oop.orangeengine.message.Replaceable
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.royalix.bossesexpansion.plugin.boss.BossDamager
import org.royalix.bossesexpansion.plugin.config.timed.TimedSpawner
import kotlin.math.round
import kotlin.reflect.KClass

object Placeholders {
    private val placeholders: MutableMap<KClass<*>, MutableSet<OPair<String, (Any) -> String>>> =
        Maps.newHashMap()

    init {
        add<Player>("display_name") { it.displayName }
        add<org.royalix.bossesexpansion.plugin.boss.BBoss>("boss_name") { it.displayName }
        add<org.royalix.bossesexpansion.plugin.boss.BBoss>("boss_uuid") { it.entity.uniqueId }
        add<org.royalix.bossesexpansion.plugin.boss.BBoss>("boss_x") { it.location.blockX }
        add<org.royalix.bossesexpansion.plugin.boss.BBoss>("boss_y") { it.location.blockY }
        add<org.royalix.bossesexpansion.plugin.boss.BBoss>("boss_z") { it.location.blockZ }
        add<org.royalix.bossesexpansion.plugin.boss.BBoss>("boss_world") { it.location.world.name }
        add<org.royalix.bossesexpansion.plugin.boss.BBoss>("boss_region") {
            it.spawnRegion?.id ?: "None"
        }
        add<org.royalix.bossesexpansion.plugin.boss.BBoss>("boss_damage") { damageFormat.format(it.totalDamage) }
        add<org.royalix.bossesexpansion.plugin.boss.BBoss>("boss_damagers") { it.damageMap.size }
        add<org.royalix.bossesexpansion.plugin.boss.BBoss>("boss_health") { damageFormat.format((it.entity as LivingEntity).health) }
        add<org.royalix.bossesexpansion.plugin.boss.BBoss>("boss_with_children_health") {
            damageFormat.format(
                (it.entity as LivingEntity).health +
                        it.children
                            .map { (it as LivingEntity).health }
                            .sum()
            )
        }

        add<BossDamager>("damager_position") { it.position.toInt() }
        add<BossDamager>("damager_name") { it.offlinePlayer.name }
        add<BossDamager>("damager_damage") { damageFormat.format(it.damage) }
        add<BossDamager>("damager_damage_percentage") { round(it.damage / it.boss.totalDamage * 100).toInt() }

        add<TimedSpawner>("timedspawner_id") { it.config.file.file.nameWithoutExtension }
        add<TimedSpawner>("timedspawner_left_complex") { it.left() }
    }

    private inline fun <reified T> add(placeholder: String, noinline handler: (T) -> Any) {
        placeholders.computeIfAbsent(
            T::class
        ) { Sets.newHashSet() }.add(OPair(placeholder, { o -> handler(o as T).toString() }))
    }

    fun parsePlaceholders(vararg objs: Any): Set<OPair<String, String>> {
        val parsed: MutableSet<OPair<String, String>> = hashSetOf()
        for (obj in objs)
            parsed.addAll(
                findPlaceholdersFor(obj).map { OPair("%${it.first}%", it.second.invoke(obj)) }
            )
        return parsed
    }

    fun findPlaceholdersFor(`object`: Any): Set<OPair<String, (Any) -> String>> {
        return findPlaceholdersFor(`object`::class)
    }

    fun findPlaceholdersFor(clazz: KClass<*>): Set<OPair<String, (Any) -> String>> {
        val found: MutableSet<OPair<String, (Any) -> String>> = Sets.newHashSet()
        placeholders.forEach { (k, v) ->
            if (k.java.isAssignableFrom(clazz.java)) found.addAll(
                v.map { pair ->
                    OPair(
                        pair.first,
                        pair.second
                    )
                }
            )
        }
        return found
    }

    fun findPlaceholdersFor(vararg objects: Any): Map<KClass<*>, Set<OPair<String, (Any) -> String>>> {
        val found: MutableMap<KClass<*>, Set<OPair<String, (Any) -> String>>> = Maps.newHashMap()
        for (`object` in objects) found.computeIfAbsent(
            `object`::class
        ) {
            findPlaceholdersFor(
                it
            )
        }
        return found
    }

    fun String.parse(vararg objects: Any): String {
        var text = this
        objects.forEach {
            findPlaceholdersFor(it)
                .forEach { pl ->
                    text = text.replace("{${pl.key}}", pl.second!!(it))
                    text = text.replace("%${pl.key}%", pl.second!!(it))
                }
        }
        return text
    }

    fun <T : Replaceable<*>> replaceMessage(message: T, vararg objects: Any): T {
        objects.forEach {
            findPlaceholdersFor(it)
                .forEach { pl ->
                    message.replace("{${pl.key}}", pl.second!!(it))
                    message.replace("%${pl.key}%", pl.second!!(it))
                }
        }
        return message
    }
}
