package com.spaghetti.item_crusher

interface UIInteractionDelegate {

    /**
     * Called when the user clicks the start button.
     */
    fun handleStartButtonClicked()

    /**
     * Called when the user switches from "Use this account as mule" to
     * "Do not use this account as mule".
     */
    fun handleUseAsMuleToggleChanged(value: Boolean)

}