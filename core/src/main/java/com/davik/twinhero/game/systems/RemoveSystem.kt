package com.davik.twinhero.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.davik.twinhero.game.GameScreen
import com.davik.twinhero.game.components.RemoveComp
import ktx.ashley.allOf

class RemoveSystem(val mainScreen: GameScreen) : IteratingSystem(allOf(RemoveComp::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        engine.removeEntity(entity)
    }
}