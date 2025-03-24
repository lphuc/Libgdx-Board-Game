package com.davik.twinhero.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool.Poolable
import com.davik.twinhero.helpers.AnimState
import com.esotericsoftware.spine.AnimationState
import com.esotericsoftware.spine.Skeleton
import com.esotericsoftware.spine.SkeletonRenderer
import ktx.ashley.get
import ktx.ashley.mapperFor

open class SpineComp(
    var skeleton: Skeleton? = null,
    var animationState: AnimationState? = null,
    var spineTag: SpineTag = SpineTag.NONE,
    var delta: Float = 0f, // workaround to avoid frame flashing due to a slight delay between addAnimation() & animationState.update()
) : Component, Poolable {
    var skeletonRenderer: SkeletonRenderer = SkeletonRenderer().apply { setPremultipliedAlpha(true) }
    var initAnimation = false
    var currentAnimState: AnimState? = null

    enum class SpineTag {
        CHARACTER, ORC_MONSTER, BIRD_MONSTER, WEAPON_EFFECT, NONE
    }

    companion object {
        val mapper = mapperFor<SpineComp>()
    }

    override fun reset() {
        skeleton = null
        animationState?.clearListeners()
        animationState?.setEmptyAnimations(0f)
        animationState = null
        spineTag = SpineTag.NONE
        initAnimation = false
        currentAnimState = null
        delta = 0f
    }

}

val Entity.spineCmp: SpineComp
    get() = this[SpineComp.mapper] ?: throw KotlinNullPointerException("Trying to access a null SpineComponent")
