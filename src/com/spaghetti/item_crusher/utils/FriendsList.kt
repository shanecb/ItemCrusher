package com.spaghetti.item_crusher.utils

import org.rspeer.runetek.adapter.component.InterfaceComponent
import org.rspeer.runetek.api.component.InterfaceComposite
import org.rspeer.runetek.api.component.Interfaces
import com.spaghetti.item_crusher.utils.InterfaceFinder.Companion.ADD_FRIEND_BUTTON_ADDRESS
import com.spaghetti.item_crusher.utils.InterfaceFinder.Companion.FRIENDS_LIST_BUTTON_ADDRESS
import org.rspeer.runetek.api.commons.Time
import org.rspeer.runetek.api.component.EnterInput
import org.rspeer.runetek.api.component.chatter.Chat
import org.rspeer.runetek.api.input.Keyboard
import org.rspeer.ui.Log

object FriendsList {

    private val friendsListInterfaceID = InterfaceComposite.FRIENDS_LIST.group

    class FriendsListEntry(private val nameComponent: InterfaceComponent, private val worldComponent: InterfaceComponent) {
        val name: String get() = nameComponent.text
        val world: Int get() {
            if (worldComponent.text == "Offline") return 0
            return worldComponent.text?.split("World ")?.get(1)?.toIntOrNull() ?: 0
        }
        val isOnline: Boolean get() = world != 0

        // TODO: find out why interacting with the component results in send message prompt with no recipient
        fun send(message: String): Boolean {
            Log.info("Interacting with InterfaceComponent to send message to ${nameComponent.text}\n " +
                "Friend is on world ${world}")
            if (!nameComponent.containsAction("Message")) {
                Log.severe("\"Message\" is not an available option"); return false
            }
            nameComponent.interact("Message")
            if (!nameComponent.interact("Message")) {
                Log.severe("Failed to use the \"Message\" interaction on friends list entry component"); return false
            }
            Time.sleep(850, 1250)
            Log.info("Initiating EnterInput::isOpen check")
            if (!Time.sleepUntil({EnterInput.isOpen()}, 3000)) {
                Log.severe("Text input interface did not open"); return false
            }
            Time.sleep(850, 1250)
            Log.info("Initiating message")
            if (!EnterInput.initiate(message)) {
                Log.severe("Could not enter input to send message"); return false
            }

            return true
        }
    }

    private val friendEntryComponents: Map<String, FriendsListEntry>
        get() {
            val labelComponents = Interfaces.get(
                friendsListInterfaceID,
                {it.type == InterfaceComponent.TYPE_LABEL && it.parentIndex != friendsListInterfaceID},
                true
            )
            val (nameComponents, worldComponents) = labelComponents.partition {
                it?.text != "Offline" && !(it?.text?.contains("World") ?: false)
            }
            return nameComponents.map { nameComponent ->
                val worldComponent = worldComponents.first{it.bounds == nameComponent.bounds}
                FriendsListEntry(nameComponent, worldComponent)
            }.associateBy{it.name}
        }

    private fun switchToFriendsListInterface(): Boolean {
        val friendsListButton = FRIENDS_LIST_BUTTON_ADDRESS.resolve()
        val addFriendButton = ADD_FRIEND_BUTTON_ADDRESS.resolve()
        if (friendsListButton == null || addFriendButton == null) return false

        if (!addFriendButton.isVisible) friendsListButton.click()
        return Time.sleepUntil({addFriendButton.isVisible()}, 1500)
    }

    /**
     * The list of names found on the friends list. NOTE: the friends list must be opened
     * (if it's not already open) in order for new friends to be listed here.
     */
    val friends: List<String>
        get() = friendEntryComponents.keys.toList()

    /**
     * Checks if a player is on the friends list. NOTE: this method will change the active
     * interface to the friends list.
     *
     * @param name the name to search for.
     * @return a `Boolean` indicating whether the player was found on the friends list.
     */
    fun playerIsFriend(name: String): Boolean {
        if (!switchToFriendsListInterface()) return false

        return friends.contains(name)
    }

    /**
     * Adds the player with the given name to the friends list. NOTE: this method
     * will change the active interface to the friends list.
     *
     * @param name the name of the player you wish to add to the friends list.
     * @return a `Boolean` indicating whether or not the operation was successful.
     */
    fun addFriend(name: String): Boolean {
        if (!switchToFriendsListInterface()) return false
        if (friends.contains(name)) return true

        val addFriendButton = ADD_FRIEND_BUTTON_ADDRESS.resolve()
        addFriendButton.click()
        Time.sleepUntil({EnterInput.isOpen()}, 1500)

        if (!EnterInput.initiate(name)) return false

        return Time.sleepUntil({playerIsFriend(name)}, 3000)
    }

    /**
     * Determines whether the player with the given name is online. NOTE: you must
     * have the player on your friends list and they must either have you on theirs
     * or have their private chat mode set to "All".
     *
     * @param name the name of the player you wish to check the online status of.
     * @return a `Boolean` indicating whether the player is online.
     */
    fun friendIsOnline(name: String): Boolean {
        return friendEntryComponents[name]?.isOnline ?: false
    }

    /**
     * Sends a message to the give player with the given name. The player must already be
     * on the friends list. NOTE: this method will change the active interface to the friends
     * list.
     *
     * @param name the name of the player to send a message to.
     * @param message the message you wish to send.
     * @return a `Boolean` indicating whether or not the operation was successful.
     */
    fun sendMessageTo(name: String, message: String): Boolean {
        Log.info("Trying to send message to $name")
        if (!switchToFriendsListInterface()) return false

        if (!friendIsOnline(name)) {
            Log.severe("$name is not online.")
            return false
        }

        if (!Chat.send(name, "")) Log.severe("Could not send message")
        Time.sleepUntil({EnterInput.isOpen()}, 5000)
        Keyboard.sendText(message)
        Time.sleep(600, 1450)
        Keyboard.pressEnter()

        return true
    }


}