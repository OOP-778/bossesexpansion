package com.honeybeedev.bossesexpansion.plugin.tasks

import com.honeybeedev.bossesexpansion.plugin.BossesExpansion
import com.oop.orangeengine.main.task.OTask
import java.util.concurrent.TimeUnit

class TimedSpawnerTask : OTask() {
    init {
        delay(TimeUnit.SECONDS, 1)
        sync(false)
        runnable {
            val timedSpawners = BossesExpansion.instance!!.configController.timedSpawners.values
            for (timedSpawner in timedSpawners) {
                timedSpawner.tick()
            }
        }
        execute()
    }
}