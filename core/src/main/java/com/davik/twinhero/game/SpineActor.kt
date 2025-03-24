package com.davik.twinhero.game

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.davik.twinhero.helpers.Vector1
import com.esotericsoftware.spine.AnimationState
import com.esotericsoftware.spine.AnimationStateData
import com.esotericsoftware.spine.Skeleton
import com.esotericsoftware.spine.SkeletonData
import com.esotericsoftware.spine.SkeletonRenderer
import com.esotericsoftware.spine.attachments.RegionAttachment


class SpineActor(val skeletonData: SkeletonData, val animStateData: AnimationStateData, val type: Int) : Actor() {
    var animationState: AnimationState? = null
    var skeleton: Skeleton? = null
    private var skeletonRenderer: SkeletonRenderer? = null
    var position = Vector2()
    var alpha = Vector1(0f)
    var mWidth = Vector1(width)
    var mHeight = Vector1(height)

    var frameAttachment: RegionAttachment? = null
    var line1Attachment: RegionAttachment? = null
    var line2Attachment: RegionAttachment? = null
    var line3Attachment: RegionAttachment? = null
    var line4Attachment: RegionAttachment? = null

    init {
        loadAnimation()
        frameAttachment = skeleton?.findSlot("frame")?.attachment as RegionAttachment
        line1Attachment = skeleton?.findSlot("tiny_line")?.attachment as RegionAttachment
        line2Attachment = skeleton?.findSlot("tiny_line2")?.attachment as RegionAttachment
        line3Attachment = skeleton?.findSlot("tiny_line3")?.attachment as RegionAttachment
        line4Attachment = skeleton?.findSlot("tiny_line4")?.attachment as RegionAttachment
    }

    private fun loadAnimation() {
        animationState = AnimationState(animStateData)
        skeleton = Skeleton(skeletonData)
        skeleton?.setToSetupPose()
        skeleton?.updateWorldTransform()
        skeletonRenderer = SkeletonRenderer()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (type == 1) {
            frameAttachment?.color?.set(0.196f, 0.8f, 0.196f, 1f)
            line1Attachment?.color?.set(0.196f, 0.8f, 0.196f, 1f)
            line2Attachment?.color?.set(0.196f, 0.8f, 0.196f, 1f)
            line3Attachment?.color?.set(0.196f, 0.8f, 0.196f, 1f)
            line4Attachment?.color?.set(0.196f, 0.8f, 0.196f, 1f)
        } else if (type == 2) {
            frameAttachment?.color?.set(0f, 0.541f, 1f, 1f)
            line1Attachment?.color?.set(0f, 0.541f, 1f, 1f)
            line2Attachment?.color?.set(0f, 0.541f, 1f, 1f)
            line3Attachment?.color?.set(0f, 0.541f, 1f, 1f)
            line4Attachment?.color?.set(0f, 0.541f, 1f, 1f)
        } else if (type == 3) {
            frameAttachment?.color?.set(1f, 0.78f, 0f, 1f)
            line1Attachment?.color?.set(1f, 0.78f, 0f, 1f)
            line2Attachment?.color?.set(1f, 0.78f, 0f, 1f)
            line3Attachment?.color?.set(1f, 0.78f, 0f, 1f)
            line4Attachment?.color?.set(1f, 0.78f, 0f, 1f)
        } else {
            frameAttachment?.color?.set(0.61f, 0f, 1f, 1f)
            line1Attachment?.color?.set(0.61f, 0f, 1f, 1f)
            line2Attachment?.color?.set(0.61f, 0f, 1f, 1f)
            line3Attachment?.color?.set(0.61f, 0f, 1f, 1f)
            line4Attachment?.color?.set(0.61f, 0f, 1f, 1f)
        }

        skeletonRenderer?.draw(batch, skeleton)
        batch?.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        //reset color to white for next frame
        frameAttachment?.color?.set(1f, 1f, 1f, 1f)
        line1Attachment?.color?.set(1f, 1f, 1f, 1f)
        line2Attachment?.color?.set(1f, 1f, 1f, 1f)
        line3Attachment?.color?.set(1f, 1f, 1f, 1f)
        line4Attachment?.color?.set(1f, 1f, 1f, 1f)
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
