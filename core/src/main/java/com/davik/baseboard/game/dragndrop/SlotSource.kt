package com.davik.baseboard.game.dragndrop

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload
import com.davik.baseboard.game.AssLoader
import com.davik.baseboard.game.components.itemAttrCmp
import com.davik.baseboard.game.components.textTureCmp
import com.davik.baseboard.helpers.ItemType

class SlotSource(val actor: SlotActor) : DragAndDrop.Source(actor) {
    private val sourceSlot: EscSlot = actor.slot

    private val payload = Payload()
    override fun dragStart(event: InputEvent, x: Float, y: Float, pointer: Int): Payload? {
        val payloadSlot = EscSlot(sourceSlot.slotItem?.itemAttrCmp?.itemType ?: ItemType.EMPTY)
        payloadSlot.amount = sourceSlot.amount
        payloadSlot.slotItem = sourceSlot.slotItem
        sourceSlot.take(false)
        payload.setObject(payloadSlot)
        val icon = payloadSlot.slotItem?.textTureCmp?.region
        val dragActor: Actor = Image(icon) // dragActor.scaleBy(2f);
        payload.dragActor = dragActor
        val validDragActor: Actor = Image(icon) // validDragActor.setColor(0, 1, 0, 1);
        payload.validDragActor = validDragActor
        val invalidDragActor: Actor = Image(icon) // invalidDragActor.setColor(1, 0, 0, 1);
        payload.invalidDragActor = invalidDragActor

        actor.isDragging = true
        //        actor.boardWindow.slots.forEach { slot ->
        //            if (slot.isValidToInvade && slot.slotNumber > actor.boardWindow.currentRow * 5 - 10) {
        //                slot.isHighlight = true
        //                slot.notifyListeners(false)
        //            }
        //        }

        return payload
    }

    override fun dragStop(event: InputEvent, x: Float, y: Float, pointer: Int, payload: Payload?, target: DragAndDrop.Target?) {
        val payloadSlot = payload?.getObject() as EscSlot
        if (target != null) {
            val targetSlot = (target.actor as SlotActor).slot

            // targetSlot number must be lower currentSlotRow - 10
            //            if (targetSlot.slotNumber > actor.boardWindow.currentRow * 5 - 10) {
            //                if (targetSlot.slotItem == null) {
            //                    //can't swap
            //                    sourceSlot.add(payloadSlot.slotItem, true)
            //                    actor.boardWindow.gameScreen.twinHero.audioManager.playGUISound(AssLoader.INST().wrongSlotSound, 1f)
            //                } else {
            //                    sourceSlot.take(false)
            //                    targetSlot.slotItem?.let { item ->
            //                        val slotPosition = getActorScreenPos(target.actor)
            //                        actor.boardWindow.setConsumeItem(item, targetSlot.slotNumber, slotPosition)
            //                    }
            //                    targetSlot.take(false)
            //                    targetSlot.add(payloadSlot.slotItem, true)
            //                }
            //            } else { //can' swap
            //                actor.boardWindow.gameScreen.showToastMessage("too far", Color.RED)
            //                sourceSlot.add(payloadSlot.slotItem, true)
            //                actor.boardWindow.gameScreen.twinHero.audioManager.playGUISound(AssLoader.INST().wrongSlotSound, 1f)
            //            }

        } else { //can't swap
            sourceSlot.add(payloadSlot.slotItem, true)
            actor.boardWindow.gameScreen.boardGame.audioManager.playGUISound(AssLoader.INST().wrongSlotSound, 1f)
        }
        actor.isDragging = false
        //reset all highlight slots
        actor.boardWindow.slots.filter { it != actor.slot }.forEach { slot ->
            slot.isHighlight = false
            slot.notifyListeners(false)
        }
    }
}
