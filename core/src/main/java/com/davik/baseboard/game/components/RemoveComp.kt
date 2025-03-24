package com.davik.baseboard.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor

/**
 * Component to mark an [Entity] for removal. It is used by the [RemoveSystem] to
 * remove entities from the [Engine] at the beginning/end of a frame.
 */
class RemoveComp : Component, Pool.Poolable {

    companion object {
        val mapper = mapperFor<RemoveComp>()
    }

    override fun reset() {
    }
}

/**
 * Adds a [RemoveComp] to the [Entity] by using the [engine's][Engine]
 * [createComponent][Engine.createComponent] method.
 * The entity then gets removed by the [RemoveSystem].
 *
 * [WARNING] every components inside this entity will be removed -> other entities that also hold these components will become null components
 */
fun Entity.removeFromEngine(engine: Engine) {
    if (!isBeingRemoved) {
        this.add(engine.createComponent(RemoveComp::class.java))
    }
}

/**
 * Checks if an [Entity] gets removed by the engine or has a [RemoveComp].
 * Use this instead of [Entity.isRemoving] to check if an entity really gets removed.
 */
val Entity.isBeingRemoved: Boolean
    get() {
        return this[RemoveComp.mapper] != null || this.isRemoving
    }
