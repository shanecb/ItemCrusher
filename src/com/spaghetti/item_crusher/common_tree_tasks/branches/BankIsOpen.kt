package com.spaghetti.item_crusher.common_tree_tasks.branches

import treescript.BranchTask
import treescript.TreeTask
import com.spaghetti.item_crusher.common_tree_tasks.leaves.OpenBank
import org.rspeer.runetek.api.component.Bank

class BankIsOpen(private val successTask: TreeTask): BranchTask() {

    override fun validate(): Boolean {
        return Bank.isOpen()
    }

    override fun successTask(): TreeTask {
        return successTask
    }

    override fun failureTask(): TreeTask {
        return OpenBank()
    }

}