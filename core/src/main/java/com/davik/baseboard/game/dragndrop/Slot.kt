package com.davik.baseboard.game.dragndrop

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.davik.baseboard.helpers.IItemType
import com.davik.baseboard.helpers.VIEWPORT_GUI_WIDTH
import com.davik.baseboard.helpers.sizeAbleLblStyleMedium

val SLOT_WIDTH = VIEWPORT_GUI_WIDTH / 7.6f

class EscSlot(val itemType: IItemType) : Stack() {
    var amount = 0
    var slotNumber = 0
    private var label: Label = Label("some text", sizeAbleLblStyleMedium())

    // WARNING: entity that hold components can't be assigned to other Slot => must be clone
    var slotItem: Entity? = null // can be player, monster or other items
    var isSelected = false
    var isHighlight = false

    private val slotListeners = Array<SlotListener>()

    init {

        label.setAlignment(Align.bottomRight)
        label.isVisible = false

        this.name = "background"
        label.name = "numitems"

        add(label)
    }

    val isEmpty: Boolean get() = slotItem == null || amount <= 0

    fun addSlotListener(slotListener: SlotListener) {
        slotListeners.add(slotListener)
    }

    fun removeSlotListener(slotListener: SlotListener) {
        slotListeners.removeValue(slotListener, true)
    }


    fun add(toAddItem: Entity?, isDrag: Boolean): Boolean {
        slotItem = toAddItem
        notifyListeners(isDrag)
        return false
    }


    fun take(isDrag: Boolean): Boolean {
        slotItem = null
        notifyListeners(isDrag)
        return false
    }

    fun notifyListeners(isDrag: Boolean) {
        slotListeners.forEach {
            it.onSlotChanged(this, isDrag)
        }
    }

    override fun toString(): String {
        return "Slot[$slotItem:$amount]"
    }

    enum class SlotType {
        CHARACTER, ITEM, MONSTER
    }
}
