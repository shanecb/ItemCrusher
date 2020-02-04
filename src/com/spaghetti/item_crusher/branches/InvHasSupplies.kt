package com.spaghetti.item_crusher.branches

import com.spaghetti.item_crusher.ItemCrusher
import com.spaghetti.item_crusher.common_tree_tasks.branches.BankIsOpen
import com.spaghetti.item_crusher.entities.Constants
import com.spaghetti.item_crusher.ItemCrusher.Progress
import org.rspeer.runetek.adapter.component.Item
import org.rspeer.runetek.api.commons.Time
import org.rspeer.runetek.api.commons.math.Random
import org.rspeer.runetek.api.component.Bank
import org.rspeer.runetek.api.component.tab.Inventory
import org.rspeer.runetek.api.input.Mouse
import org.rspeer.runetek.api.movement.Movement
import org.rspeer.runetek.api.scene.Players
import org.rspeer.ui.Log
import treescript.BranchTask
import treescript.LeafTask
import treescript.TreeTask

class InvHasSupplies: BranchTask() {

    companion object {
        var hasBanked = false
    }

    override fun validate(): Boolean {
        return Inventory.contains(Constants.crushingTool) && Inventory.contains(Constants.crushable)
    }

    override fun successTask(): TreeTask {
        return object: LeafTask() {
            override fun execute(): Int {
//                while (Inventory.contains(Constants.crushable)) {
////                    if (ItemCrusher.isPaused || ItemCrusher.isStopping) {
////                        Log.info("Script paused or stopped while crushing crushables in inventory.")
////                        break
////                    }
////
////                    val beginningCount = Inventory.getCount{it.name == Constants.crushable}
////                    val crushable = Inventory.getFirst{it.name == Constants.crushable}
////                    Inventory.use({it.name == Constants.crushingTool}, crushable)
////                    Time.sleepUntil({Inventory.getCount{it.name == Constants.crushable} < beginningCount}, 350)
//
//                    if (ItemCrusher.isPaused || ItemCrusher.isStopping) {
//                        Log.info("Script paused or stopped while crushing crushables in inventory.")
//                        break
//                    }
//
//                    val crushableCount = Inventory.getCount(Constants.crushable)
//                    for (i in 0..crushableCount) {
//                        Inventory.getItemAt(26)?.let { item ->
//                            var item = item
//                            if (item.name != Constants.crushable) {
//                                Log.severe("Items withdrawn in wrong order!")
//                                item = Inventory.getFirst(Constants.crushable)
//                            }
//                            Inventory.use({it.name == Constants.crushingTool}, item)
////                            Time.sleep(50, 150)
//                        }
//                    }
//                }
                Movement.toggleRun(false)

                fun adjustMouseIfNeeded() {
                    val x = Mouse.getX()
                    val y = Mouse.getY()

                    if (!(x < 562 || y < 167 || y > 438 || x == -1 || y == -1)) {
                        return
                    }

                    Log.fine("Adjusting mouse!")
                    Log.fine("old X: ${Mouse.getX()}, old Y: ${Mouse.getY()}")
                    val newX = Random.nextInt(715, 760)
                    val newY = Random.nextInt(376, 428)
                    Mouse.move(newX, newY)
                    Time.sleep(50)
                    Log.fine("new X: ${Mouse.getX()}, new Y: ${Mouse.getY()}")
                }



                adjustMouseIfNeeded()

                if (!Inventory.contains(Constants.crushable)) return Random.nextInt(150, 250)
                val item = Inventory.getItems{it.name == Constants.crushable}?.last() ?: return Random.nextInt(150, 250)

                val crushableCount = Inventory.getCount(Constants.crushable)
                for (i in 0..(crushableCount - 1)) {
                    adjustMouseIfNeeded()
                    if (ItemCrusher.isPaused || ItemCrusher.isStopping) {
                        Log.info("Script paused or stopped while crushing crushables in inventory.")
                        break
                    }

                    Inventory.use({it.name == Constants.crushingTool}, item)
                }

                return Random.nextInt(150, 250)
            }
        }
    }

    override fun failureTask(): TreeTask {
        return BankIsOpen(object: LeafTask() {
            override fun execute(): Int {
                if (!Progress.hasBanked) {
                    Bank.depositInventory()

                    Time.sleepUntil({Inventory.isEmpty()}, 650)

                    Progress.beginningCrushedCount = Bank.getCount(Constants.crushed)

                    Progress.hasBanked = true
                }
                else if (Inventory.containsAnyExcept(Constants.crushingTool, Constants.crushable, Constants.crushed)
                        || Inventory.getCount(Constants.crushingTool) > 1
                ) {
                    Bank.depositInventory()
                }
                else {
                    Bank.depositAllExcept(Constants.crushingTool)
                }

                Time.sleepUntil({Inventory.containsOnly(Constants.crushingTool) || Inventory.isEmpty()}, 650)

                if (!Bank.contains(Constants.crushable)) {
                    // hacky
                    if (!Time.sleepUntil({Bank.contains(Constants.crushable)}, 750)) {
                        Bank.close()
                        Time.sleepUntil(Bank::isClosed, 950)

                        Bank.open()
                        Time.sleepUntil(Bank::isOpen, 950)

                        if (!Bank.contains(Constants.crushable)) {
                            Log.severe("Could not find Chocolate bar in bank. Terminating script.")
                            return -1
                        }

                        Progress.currentCrushedCount = Bank.getCount(Constants.crushed)
                    }
                }

                Bank.withdraw(Constants.crushable, 27)
                Time.sleepUntil({Inventory.contains(Constants.crushable)}, 350)

                if (!Inventory.contains(Constants.crushingTool)) {
                    if (!Bank.contains(Constants.crushingTool)) {
                        // hacky
                        Bank.close()
                        Time.sleepUntil(Bank::isClosed, 950)

                        Bank.open()
                        Time.sleepUntil(Bank::isOpen, 950)

                        if (!Bank.contains(Constants.crushingTool) && !Inventory.contains(Constants.crushingTool)) {
                            Log.severe("Could not find crushing tool (" + Constants.crushingTool + ") in bank or inventory. Terminating script.")
                            return -1
                        }
                    }

                    Bank.withdraw(Constants.crushingTool, 1)
                    Time.sleepUntil({Inventory.contains(Constants.crushingTool)}, 350)
                }

                if (!Bank.contains(Constants.crushed)) {
                    // hacky
                    if (!Time.sleepUntil({Bank.contains(Constants.crushed)}, 750)) {
                        Bank.close()
                        Time.sleepUntil(Bank::isClosed, 950)

                        Bank.open()
                        Time.sleepUntil(Bank::isOpen, 950)
                    }
                }

                val crushedCount = Bank.getCount(Constants.crushed)
                if (crushedCount != 0) {
                    Progress.currentCrushedCount = crushedCount
                }

                Bank.close()
                Time.sleepUntil({Bank.isClosed()}, Random.nextLong(350, 500))

                return Random.nextInt(150, 350)
            }
        })
    }

}