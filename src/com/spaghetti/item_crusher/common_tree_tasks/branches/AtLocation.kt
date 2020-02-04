package com.spaghetti.item_crusher.common_tree_tasks.branches

import treescript.BranchTask
import treescript.TreeTask
import com.spaghetti.item_crusher.leaves.TraverseTo
import org.rspeer.runetek.api.movement.position.Position
import org.rspeer.ui.Log

open class AtLocation(private val location: Position, private val successTask: TreeTask): BranchTask() {

    override fun validate(): Boolean {
        return location.distance() <= 20
    }

    override fun successTask(): TreeTask {
        return successTask
    }

    override fun failureTask(): TreeTask {
        Log.severe("REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE\nEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE\nEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE")
        return TraverseTo(location)
    }

}