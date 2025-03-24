package com.davik.baseboard.game

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.davik.baseboard.helpers.Vector1
import com.esotericsoftware.spine.AnimationState
import com.esotericsoftware.spine.AnimationStateData
import com.esotericsoftware.spine.Skeleton
import com.esotericsoftware.spine.SkeletonData
import com.esotericsoftware.spine.SkeletonRenderer


class SpineActor(val skeletonData: SkeletonData, val animStateData: AnimationStateData) : Actor() {
    var animationState: AnimationState? = null
    var skeleton: Skeleton? = null
    private var skeletonRenderer: SkeletonRenderer? = null
    var position = Vector2()
    var alpha = Vector1(0f)
    var mWidth = Vector1(width)
    var mHeight = Vector1(height)

    init {
        loadAnimation()
    }

    private fun loadAnimation() {
        animationState = AnimationState(animStateData)
        skeleton = Skeleton(skeletonData)
        skeleton?.setToSetupPose()
        skeleton?.updateWorldTransform()
        skeletonRenderer = SkeletonRenderer()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        skeletonRenderer?.draw(batch, skeleton)
        batch?.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
    }

    override fun act(delta: Float) {
        skeleton?.x = position.x
        skeleton?.y = position.y
        animationState?.update(delta)
        animationState?.apply(skeleton)
        skeleton?.updateWorldTransform()
        super.act(delta)
    }

    /**
     * for interpolation purpose
     */
    fun update() {
        x = position.x
        y = position.y
        width = mWidth.value
        height = mHeight.value
    }
}

/**
 * For interpolation purpose
 */
class AnimImage(texture: TextureRegion) : Image(texture) {
    var alpha = Vector1(0f)
    var mWidth = Vector1(0f)
    var mHeight = Vector1(0f)
    var position: Vector2 = Vector2(x, y)

    fun update() {
        x = position.x
        y = position.y
        width = mWidth.value
        height = mHeight.value
    }
}
