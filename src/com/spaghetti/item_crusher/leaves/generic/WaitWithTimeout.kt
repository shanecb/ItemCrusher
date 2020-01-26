package com.spaghetti.item_crusher.leaves.generic

import treescript.LeafTask
import org.rspeer.runetek.api.commons.StopWatch
import org.rspeer.runetek.api.commons.math.Random
import org.rspeer.ui.Log
import java.time.Duration

class WaitWithTimeout(private val timeout: Long, private val timeoutMsg: String): LeafTask() {

    companion object {
        var timers: MutableMap<String, StopWatch> = mutableMapOf()
    }

    override fun execute(): Int {
        val timer = timers[taskID]
        if (timer == null) timers[taskID] = StopWatch.fixed(Duration.ofSeconds(timeout))
        else if (timer.remaining.toMillis() > -5) {
            Log.severe(timeoutMsg)
            Log.severe("WaitWithTimeout: Timeout exceeded. Terminating script.")
            return -1
        }

        return Random.nextInt(450, 750)
    }

}