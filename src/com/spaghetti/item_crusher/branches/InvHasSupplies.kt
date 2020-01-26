package com.spaghetti.item_crusher.branches

import com.spaghetti.item_crusher.ItemCrusher
import com.spaghetti.item_crusher.common_tree_tasks.branches.BankIsOpen
import com.spaghetti.item_crusher.entities.Constants
import com.spaghetti.item_crusher.ItemCrusher.Progress
import org.rspeer.runetek.api.commons.Time
import org.rspeer.runetek.api.commons.math.Random
import org.rspeer.runetek.api.component.Bank
import org.rspeer.runetek.api.component.tab.Inventory
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
                while (Inventory.contains(Constants.crushable)) {
                    if (ItemCrusher.isPaused || ItemCrusher.isStopping) {
                        Log.info("Script paused or stopped while crushing crushables in inventory.")
                        break
                    }

                    val beginningCount = Inventory.getCount{it.name == Constants.crushable}
                    val crushable = Inventory.getFirst{it.name == Constants.crushable}
                    Inventory.use({it.name == Constants.crushingTool}, crushable)
                    Time.sleepUntil({Inventory.getCount{it.name == Constants.crushable} < beginningCount}, 350)
                }

                return Random.nextInt(250, 450)
            }
        }
    }

    override fun failureTask(): TreeTask {
        return BankIsOpen(object: LeafTask() {
            override fun execute(): Int {
                if (Progress.currentCrushedCount == 0) {
                    Bank.depositInventory()

                    Time.sleepUntil({Inventory.isEmpty()}, 650)

                    Progress.beginningCrushedCount = Bank.getCount(Constants.crushed)
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
                    }
                }

                Bank.withdraw(Constants.crushable, 27)
                Time.sleepUntil({Inventory.contains(Constants.crushable)}, 600)

                if (!Inventory.contains(Constants.crushingTool)) {
                    if (!Bank.contains(Constants.crushingTool)) {
                        Log.severe("Could not find crushing tool (" + Constants.crushingTool + ") in bank or inventory. Terminating script.")
                        return -1
                    }

                    Bank.withdraw(Constants.crushingTool, 1)
                    Time.sleepUntil({Inventory.contains(Constants.crushingTool)}, 650)
                }

                Progress.currentCrushedCount = Bank.getCount(Constants.crushed)

                Bank.close()
                Time.sleepUntil({Bank.isClosed()}, Random.nextLong(350, 1000))

                return Random.nextInt(250, 450)
            }
        })
    }

}