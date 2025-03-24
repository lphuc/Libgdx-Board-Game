package com.davik.twinhero.game

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3

/**
 * only use this camera for in-game world, shouldn't use it for GUI
 */
open class WorldCamera(val mainScreen: GameScreen) : OrthographicCamera() {
    var zoomSpeed: Float = 0f
    private val defaultZoom = 2.2f //on mobile should be 2.2, on PC should be 2.4
    private val maxZoomIn = 2f
    private val maxZoomOut = 2.6f
    private val cameraSpeed = 3f
    var target: Entity? = null //must set null and remove all components of this entities
    var movingUpWard = false
    var movingDownWard = false
    var movingRight = false
    var movingLeft = false

    companion object {
        const val CAM_LOWEST_Y_POS = 8f //default is 7
        const val LERP_OFFSET = 0.05f // the acceptable offset value when use lerp() to reduce value to 0
    }

    fun switchTarget(newTarget: Entity?) {
        if (newTarget == null) {
            target = null
            return
        }
        target = newTarget
    }

    fun update(delta: Float) {
        zoom = defaultZoom
        position.lerp(Vector3(position.x, CAM_LOWEST_Y_POS, 0f), delta * 2f)
        super.update()
    }

    fun moveXMayQuay(x: Float) {
        position.x += x
    }
}