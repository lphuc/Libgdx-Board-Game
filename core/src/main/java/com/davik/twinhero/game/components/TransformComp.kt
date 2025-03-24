package com.davik.twinhero.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import com.davik.twinhero.helpers.Direction
import ktx.ashley.get
import ktx.ashley.mapperFor

open class TransformComp(
    var position: Vector2 = Vector2(0f, 0f),
    var origin: Vector2 = Vector2(0f, 0f),
    var rotation: Float = 0f, //in degree
    var dimension: Vector2 = Vector2(1f, 1f), //in meter
    var velocity: Vector2 = Vector2(),
    var scale: Float = 1f,
    var drawOrder: Int = 0, //the order of layer when drawing, bigger = further
    var direction: Direction = Direction.LEFT,
) : Component, Comparable<TransformComp>, Pool.Poolable {

    companion object {
        val mapper = mapperFor<TransformComp>()
    }

    override fun reset() {
        position = Vector2(0f, 0f)
        origin = Vector2(0f, 0f)
        rotation = 0f
        scale = 1f
        dimension = Vector2(1f, 1f)
        velocity = Vector2()
        drawOrder = 0
        direction = Direction.LEFT
    }

    /**
     * entities are sorted first by their drawOrder and then second by their y position
     */
    override fun compareTo(other: TransformComp): Int {
        val diff = other.drawOrder.compareTo(drawOrder)
        return if (diff == 0) {
            position.y.compareTo(other.position.y)
        } else {
            diff
        }
    }
}

val Entity.transformCmp: TransformComp
    get() = this[TransformComp.mapper] ?: throw KotlinNullPointerException("Trying to access a null TransformComp")
