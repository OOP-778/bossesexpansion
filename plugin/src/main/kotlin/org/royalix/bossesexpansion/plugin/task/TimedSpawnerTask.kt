package org.royalix.bossesexpansion.plugin.task

import com.oop.orangeengine.main.task.OTask
import java.util.concurrent.TimeUnit

class TimedSpawnerTask : OTask() {
    init {
        delay(TimeUnit.SECONDS, 1)
        sync(false)
        runnable {
            val timedSpawners =
                org.royalix.bossesexpansion.plugin.BossesExpansion.instance!!.configController.timedSpawners.values
            for (timedSpawner in timedSpawners) {
                timedSpawner.tick()
            }
        }
        execute()
    }
}
