package com.honeybeedev.bossesexpansion.plugin.config.main

import com.honeybeedev.bossesexpansion.plugin.config.message.BEMessage
import com.oop.orangeengine.message.YamlMessage
import com.oop.orangeengine.message.impl.OChatMessage
import com.oop.orangeengine.yaml.ConfigSection

class SpawnMessage(section: ConfigSection?) {
    var spawnMessage: BEMessage? = null
    var teleportMessage: OChatMessage? = null
    var teleportLimit: Int = Int.MIN_VALUE

    init {
        if (section != null) {
            section.ifValuePresent("teleport limit", Int::class.java) {
                teleportLimit = it
            }

            section.ifSectionPresent("message") {
                spawnMessage = BEMessage(it)
            }

            section.ifSectionPresent("teleport message") {
                teleportMessage = YamlMessage.load(it) as OChatMessage
            }
        }
    }

    fun cloneWithMerge(merge: SpawnMessage): SpawnMessage {
        val spawnMessage = SpawnMessage(null)

        spawnMessage.teleportLimit = merge.teleportLimit

        spawnMessage.spawnMessage = this.spawnMessage?.clone()
        spawnMessage.teleportMessage = teleportMessage?.clone()
        if (teleportLimit != Int.MIN_VALUE)
            spawnMessage.teleportLimit = teleportLimit

        if (spawnMessage.spawnMessage == null && merge.spawnMessage != null)
            spawnMessage.spawnMessage = merge.spawnMessage!!.clone()

        if (spawnMessage.teleportMessage == null && merge.teleportMessage != null)
            spawnMessage.teleportMessage = merge.teleportMessage!!.clone()

        return spawnMessage
    }
}