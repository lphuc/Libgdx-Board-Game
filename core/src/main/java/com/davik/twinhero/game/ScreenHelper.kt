package com.davik.twinhero.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.davik.twinhero.game.AssLoader.scaleUnit
import com.davik.twinhero.game.components.removeFromEngine
import com.davik.twinhero.game.dragndrop.SLOT_WIDTH
import com.davik.twinhero.game.popup.ToastMessage
import com.davik.twinhero.helpers.VIEWPORT_GUI_HEIGHT
import com.davik.twinhero.helpers.VIEWPORT_GUI_WIDTH
import com.davik.twinhero.helpers.Vector1
import com.davik.twinhero.helpers.safeFloat
import com.davik.twinhero.tween.ActorAccessor
import com.davik.twinhero.tween.engine.Tween

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

fun GameScreen.animateShowPopup(popup: Window, x: Float? = null, y: Float? = null, errorType: Boolean = false) {
    if (popup.isVisible) return
    boardGame.audioManager.playGUISound(AssLoader.INST().popupShowSound, 1f)
    val xPos = x ?: (VIEWPORT_GUI_WIDTH / 2 - popup.width / 2f)
    val yPos = y ?: (VIEWPORT_GUI_HEIGHT / 2 - popup.height / 2f)
    popup.toFront()
    popup.isVisible = true
    stage.addActor(popup)
    val startYPos = VIEWPORT_GUI_HEIGHT / 2f - popup.height / 2.25f
    popup.setPosition(xPos, yPos)
    Tween.set(popup, ActorAccessor.ALPHA).target(0f).start(tweenManager)
    Tween.to(popup, ActorAccessor.ALPHA, 0.3f).target(1f).setCallback { type, source ->
        popup.toFront()
    }.start(tweenManager)
    Tween.from(popup, ActorAccessor.Y, 0.4f).target(startYPos).start(tweenManager)
    // tweenManager?.update(Float.MIN_VALUE) // update once avoid short flash of splash before animation
    multiplexer.addProcessor(popup.stage)
}

fun GameScreen.animateHidePopup(popup: Window) {
    if (!popup.isVisible) return
    boardGame.audioManager.playGUISound(AssLoader.INST().hidePopupShow, 0.6f)
    val targetYPos = VIEWPORT_GUI_HEIGHT / 2f - popup.height / 1.7f
    Tween.to(popup, ActorAccessor.Y, 0.3f).target(targetYPos).start(tweenManager)
    Tween.to(popup, ActorAccessor.ALPHA, 0.3f).target(0f).setCallback { type, source ->
        popup.isVisible = false
        popup.addAction(Actions.removeActor())
    }.start(tweenManager)
    // tweenManager?.update(Float.MIN_VALUE) // update once avoid short flash of splash before animation
    multiplexer.removeProcessor(popup.stage)
}

fun GameScreen.showToastMessage(text: String, color: Color) {
    val toastMsg = ToastMessage(text.uppercase(), color)
    toastMsg.setPosition(VIEWPORT_GUI_WIDTH / 2f - toastMsg.width / 2f, VIEWPORT_GUI_HEIGHT / 40f)
    toastMsg.start()
    stage.addActor(toastMsg)
    listToastMsg.add(toastMsg)
    boardGame.audioManager.playGUISound(AssLoader.INST().menuTabSound, 0.5f)
}

fun GameScreen.updateToastMessages(delta: Float) {
    listToastMsg.removeIf { !it.isVisible }
    listToastMsg.forEach { toastMsg ->
        if (toastMsg.isVisible) {
            if ((toastMsg.y < VIEWPORT_GUI_HEIGHT / 12.8f && toastMsg.mAlpha > 0f) || toastMsg.alphaUp) {
                toastMsg.toastPosition.interpolate(Vector2(0f, VIEWPORT_GUI_HEIGHT / 12.8f), delta * 2f, Interpolation.pow2Out)
                toastMsg.y = toastMsg.toastPosition.y
                toastMsg.toFront()
            } else {
                toastMsg.isVisible = false
                toastMsg.remove()
            }

            if (toastMsg.y >= VIEWPORT_GUI_HEIGHT / 12.9f) {
                toastMsg.alphaUp = false //decrease popup transparency
            }
        }
    }
}

