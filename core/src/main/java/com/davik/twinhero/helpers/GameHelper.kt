package com.davik.twinhero.helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.davik.twinhero.game.AssLoader.scaleUnit
import com.davik.twinhero.game.BoardWindow
import com.davik.twinhero.game.GameScreen
import com.davik.twinhero.game.components.removeFromEngine
import com.davik.twinhero.game.dragndrop.SLOT_WIDTH

fun BoardWindow.updateDroppingActors() {
    toDropSlotActors = toDropSlotActors.filter { it.position.y >= it.initPos?.y.safeFloat() - SLOT_WIDTH / 4f * scaleUnit }.toMutableList()
    toDropSlotActors.forEach { movingActor ->
        if (movingActor.initPos == null) {
            movingActor.initPos = Vector2(movingActor.actor.x, movingActor.actor.y)
        }
        movingActor.position.lerp(Vector2(movingActor.position.x, movingActor.initPos?.y.safeFloat() - SLOT_WIDTH / 3.9f * scaleUnit),
            2f * Gdx.graphics.deltaTime)
        movingActor.alpha.lerp(Vector1(0f), 2f * Gdx.graphics.deltaTime)
        movingActor.actor.setPosition(movingActor.position.x, movingActor.position.y)

        if (movingActor.position.y <= movingActor.initPos?.y.safeFloat() - SLOT_WIDTH / 4f * scaleUnit) {
            // TODO: check here if crash
            movingActor.actor.slot.slotItem?.removeFromEngine(gameScreen.engine)
            slots.remove(movingActor.actor.slot)
            movingActor.actor.addAction(Actions.removeActor())
        }
    }
}

fun GameScreen.updateFlyingItem(delta: Float) {
}
