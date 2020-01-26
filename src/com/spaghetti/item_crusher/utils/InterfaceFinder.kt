package com.spaghetti.item_crusher.utils

import org.rspeer.runetek.adapter.component.InterfaceComponent
import org.rspeer.runetek.api.component.InterfaceAddress
import org.rspeer.runetek.api.component.InterfaceComposite
import org.rspeer.runetek.api.component.InterfaceOptions
import org.rspeer.runetek.api.component.Interfaces

class InterfaceFinder {

    companion object {

        // top level interface IDs
        private val FIXED_MODE_VIEWPORT_INTERFACE_ID   = InterfaceComposite.FIXED_MODE_VIEWPORT.group
        private val RESIZED_VIEWPORT_LINE_INTERFACE_ID = InterfaceComposite.RESIZED_VIEWPORT_LINE.group
        private val CHAT_BOX_INTERFACE_ID              = InterfaceComposite.CHATBOX.group
        private val FRIENDS_LIST_INTERFACE_ID          = InterfaceComposite.FRIENDS_LIST.group
        private val SWITCH_TO_PVP_DIALOG_INTERFACE_ID  = InterfaceComposite.ITEM_DIALOG.group

        // on screen label text
        private val FRIENDS_LIST_BUTTON_ACTION_TEXT = "Friends List"
        private val SWITCH_CHAT_TAB_ACTION_TEXT     = "Switch tab"
        private val ALL_CHAT_TEXT                   = "All"
        private val PRIVATE_CHAT_TEXT               = "Private"
        private val ADD_FRIEND_ACTION_TEXT          = "Add Friend"
        private val PLAY_NOW_BUTTON_ACTION_TEXT     = "Play"
        private val SWITCH_TO_IT_BUTTON_ACTION_TEXT = "Switch to it"

        // interface addresses

        object FixedViewMode {
            val FRIENDS_LIST_BUTTON_ADDRESS: InterfaceAddress = InterfaceAddress {
                Interfaces.getFirst(FIXED_MODE_VIEWPORT_INTERFACE_ID) { it.actions.firstOrNull().equals(FRIENDS_LIST_BUTTON_ACTION_TEXT) }
            }
        }

        object ResizableViewMode {
            val FRIENDS_LIST_BUTTON_ADDRESS: InterfaceAddress = InterfaceAddress {
                Interfaces.getFirst(RESIZED_VIEWPORT_LINE_INTERFACE_ID) { it.actions.firstOrNull().equals(FRIENDS_LIST_BUTTON_ACTION_TEXT) }
            }
        }

        var FRIENDS_LIST_BUTTON_ADDRESS: InterfaceAddress
            get() {
                val groupID = if (InterfaceOptions.getViewMode() == InterfaceOptions.ViewMode.FIXED_MODE)
                    FIXED_MODE_VIEWPORT_INTERFACE_ID else RESIZED_VIEWPORT_LINE_INTERFACE_ID

                return InterfaceAddress {
                    Interfaces.getFirst(groupID) { it.actions.firstOrNull().equals(FRIENDS_LIST_BUTTON_ACTION_TEXT) }
                }
            }
            set(value) {}

        val PLAY_NOW_BUTTON_ADDRESS: InterfaceAddress = InterfaceAddress {
            Interfaces.getFirst(InterfaceComposite.WELCOME_SCREEN.group) { it.actions.firstOrNull().equals(PLAY_NOW_BUTTON_ACTION_TEXT) }
        }

        val ALL_CHAT_BUTTON_ADDRESS: InterfaceAddress = InterfaceAddress {
            val label: InterfaceComponent = Interfaces.getFirst(CHAT_BOX_INTERFACE_ID) { it.text == ALL_CHAT_TEXT }
            Interfaces.getFirst(CHAT_BOX_INTERFACE_ID) {
                it.actions.firstOrNull().equals(SWITCH_CHAT_TAB_ACTION_TEXT)
                        && it.bounds.x == label.bounds.x
                        && it.bounds.y == label.bounds.y
            }
        }
        val privateChatButtonLabelAddress: InterfaceAddress = InterfaceAddress {
            Interfaces.getFirst(CHAT_BOX_INTERFACE_ID) { it.text == PRIVATE_CHAT_TEXT }
        }
        val PRIVATE_CHAT_BUTTON_ADDRESS: InterfaceAddress = InterfaceAddress {
            Interfaces.getFirst(CHAT_BOX_INTERFACE_ID) {
                it.actions.firstOrNull().equals(SWITCH_CHAT_TAB_ACTION_TEXT)
                        && it.bounds.x == privateChatButtonLabelAddress.resolve().bounds.x
                        && it.bounds.y == privateChatButtonLabelAddress.resolve().bounds.y
            }
        }
        val PRIVATE_CHAT_STATUS_LABEL_ADDRESS: InterfaceAddress = InterfaceAddress {
            Interfaces.getFirst(CHAT_BOX_INTERFACE_ID) {
                arrayOf("On", "Friends", "Off").fold(false) {res, nextStr -> res || it.text.contains(nextStr) }
                        && it.bounds.x == privateChatButtonLabelAddress.resolve().bounds.x }
        }

        val ADD_FRIEND_BUTTON_ADDRESS: InterfaceAddress = InterfaceAddress {
            Interfaces.getFirst(FRIENDS_LIST_INTERFACE_ID) { it.actions.firstOrNull().equals(ADD_FRIEND_ACTION_TEXT) }
        }

        val SWITCH_TO_IT_BUTTON_ADDRESS: InterfaceAddress = InterfaceAddress {
            Interfaces.getFirst(
                SWITCH_TO_PVP_DIALOG_INTERFACE_ID,
                {it.actions.contains(SWITCH_TO_IT_BUTTON_ACTION_TEXT)},
                true
            )
        }
    }

}
