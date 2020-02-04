package com.spaghetti.item_crusher.leaves

import treescript.LeafTask
import org.rspeer.runetek.api.commons.Time
import org.rspeer.runetek.api.commons.math.Random
import org.rspeer.runetek.api.movement.Movement
import org.rspeer.runetek.api.movement.position.Area
import org.rspeer.runetek.api.movement.position.Position

class TraverseTo(destination: Position): LeafTask() {

    val destination = Area.surrounding(destination, 7)

    override fun execute(): Int {
        Movement.walkToRandomized(Random.nextElement(destination.tiles))
        Time.sleep(450, 1350)

        return 0
    }

}