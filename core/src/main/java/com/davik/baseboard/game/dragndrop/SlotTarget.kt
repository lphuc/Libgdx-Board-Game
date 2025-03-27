package com.davik.baseboard.game.dragndrop

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload

class SlotTarget(actor: SlotActor) : DragAndDrop.Target(actor) {
    private val targetSlot: EscSlot

    init {
        targetSlot = actor.slot
        getActor().color = Color.LIGHT_GRAY
    }

    override fun drag(source: DragAndDrop.Source, payload: Payload, x: Float, y: Float, pointer: Int): Boolean {
        val payloadSlot = payload.getObject() as EscSlot
        actor.color = Color.WHITE
        return true
    }

    override fun drop(source: DragAndDrop.Source, payload: Payload, x: Float, y: Float, pointer: Int) {}
    override fun reset(source: DragAndDrop.Source, payload: Payload) {
        actor.color = Color.LIGHT_GRAY
    }
}