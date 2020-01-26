package com.spaghetti.item_crusher

import treescript.*
import com.spaghetti.item_crusher.branches.*
import com.spaghetti.item_crusher.common_tree_tasks.branches.AtLocation
import com.spaghetti.item_crusher.entities.Constants
import org.rspeer.runetek.api.commons.BankLocation
import org.rspeer.runetek.api.commons.StopWatch
import org.rspeer.runetek.api.component.tab.Inventory
import org.rspeer.runetek.event.listeners.RenderListener
import org.rspeer.runetek.event.types.RenderEvent
import org.rspeer.script.ScriptMeta
import org.rspeer.ui.Log
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints

const val SCRIPT_AUTHOR = "spaghetti man"
const val SCRIPT_NAME = "Spaghetti Item Crusher"

@ScriptMeta(developer = SCRIPT_AUTHOR, name = SCRIPT_NAME, desc = "Crushes stuff")
class ItemCrusher: TreeScript(), RenderListener {

    companion object {
        var script: ItemCrusher? = null
        set(value) {
            if (field == null) field = value
        }

        val isPaused: Boolean
            get() {
                script?.let { return it.isPaused }
                return false
            }

        val isStopping: Boolean
            get() {
                script?.let { return it.isStopping }
                return false
            }
    }

    object Progress {
        private val timer: StopWatch = StopWatch.start()

        val runtime: String
            get() {
                return timer.toElapsedString()
            }

        var beginningCrushedCount = 0

        var currentCrushedCount = 0

        val itemsCrushedCount: Int
            get() {
                return currentCrushedCount - beginningCrushedCount
            }

        val perHour: Double
            get() {
                return timer.getHourlyRate(itemsCrushedCount.toLong())
            }
    }

    var defaultRoot: TreeTask = AtLocation(BankLocation.GRAND_EXCHANGE.position, InvHasSupplies())
    var temporaryRoot: TreeTask? = null

    //================================================================================
    // TreeScript Methods
    //================================================================================

    override fun onStart() {
        super.onStart()

        script = this
        Log.info("Script started.")
    }

    override fun onPause() {
        super.onPause()

        Log.info("Script paused.")
    }

    override fun onCreateRoot(): TreeTask {
        temporaryRoot?.let {
            temporaryRoot = null
            return it
        }
        return defaultRoot
    }

    override fun onStop() {
        super.onStop()

        Log.info("Script stopped.")
        Log.info("""Crushed ${Progress.itemsCrushedCount} items in ${Progress.runtime} (${Progress.perHour}/hr)""")
    }

    //================================================================================
    // RenderListener
    //================================================================================

    override fun notify(p0: RenderEvent?) {
        val g2 = p0?.source?.let { it as Graphics2D } ?: return
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        val x = 10
        val y = 35

        val itemsCrushedCount = Progress.itemsCrushedCount + Inventory.getCount(Constants.crushed)

        g2.color = Color.green
        g2.drawString("runtime: ${Progress.runtime}", x, y)
        g2.drawString("items crushed: $itemsCrushedCount", x, y+20)
        g2.drawString("crushed/hour: ${Progress.perHour}", x, y+40)
    }

}