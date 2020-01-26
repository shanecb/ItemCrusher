package com.spaghetti.item_crusher.common_tree_tasks.leaves


import treescript.LeafTask
import org.rspeer.runetek.api.commons.Time
import org.rspeer.runetek.api.commons.math.Random
import org.rspeer.runetek.api.component.Bank

class OpenBank: LeafTask() {

    override fun execute(): Int {
        Bank.open()
        Time.sleepUntil({Bank.isOpen()}, 650)
        return Random.nextInt(250, 450)
    }

}