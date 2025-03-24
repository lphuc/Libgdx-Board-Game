package com.davik.baseboard.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.davik.baseboard.game.GameScreen
import com.davik.baseboard.game.components.RemoveComp
import ktx.ashley.allOf

class RemoveSystem(val mainScreen: GameScreen) : IteratingSystem(allOf(RemoveComp::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        engine.removeEntity(entity)
    }
}