package com.spaghetti.item_crusher.entities

/**
 * Data class used to store preferences.
 *
 * @property muleList the list of names that have previously been used as mules.
 * @property lastUsedMule the last account used as a mule.
 * @property itemNames the last list of items traded to a mule.
 */
data class Profile(
    val muleList: List<String> = listOf(),
    val lastUsedMule: String = "",
    val itemNames: List<String> = listOf("Coins")
)