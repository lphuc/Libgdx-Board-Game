package com.davik.twinhero.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.davik.twinhero.game.GameScreen
import com.davik.twinhero.game.components.BaseInfoComp
import com.davik.twinhero.game.components.EntityTag
import com.davik.twinhero.game.components.RemoveComp
import com.davik.twinhero.game.components.SpineComp
import com.davik.twinhero.game.components.TextureComp
import com.davik.twinhero.game.components.TransformComp
import com.davik.twinhero.game.components.transformCmp
import ktx.ashley.allOf
import ktx.ashley.get

class RenderSystem(
    private val batch: PolygonSpriteBatch, private val camera: OrthographicCamera, private val mainGame: GameScreen
) : SortedIteratingSystem(allOf(TransformComp::class).exclude(RemoveComp::class.java).get(), compareByDescending { entity -> entity.transformCmp }) {
    override fun update(deltaTime: Float) {
        // always sort entities in case their drawOrder or position.y was modified
        forceSort()
        batch.projectionMatrix = camera.combined
        batch.color = Color.WHITE
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA) //not set this will cause white screen after close bagOpenScreen
        batch.begin()
        for (i in 0 until entities.size()) {
            processEntity(entities[i], deltaTime)
        }
        batch.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val baseInfoCmp = entity[BaseInfoComp.mapper] ?: return
        val transformCmp = entity[TransformComp.mapper] ?: return
        val textTureCmp = entity[TextureComp.mapper]
        val spineCmp = entity[SpineComp.mapper]

        if (spineCmp != null) {
            spineCmp.skeleton?.run {
                setPosition(transformCmp.position.x, transformCmp.position.y)
                spineCmp.animationState?.update(deltaTime)
                spineCmp.animationState?.apply(this)
                spineCmp.skeleton?.updateWorldTransform()

                spineCmp.skeletonRenderer.draw(batch, spineCmp.skeleton)
                batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
            }

            spineCmp.delta = deltaTime

        } else if (textTureCmp != null) {
            if (baseInfoCmp.tag == EntityTag.GROUND) {
                var xRel = 0f
                for (i in 0 until 2) {
                    batch.draw(
                        textTureCmp.region,
                        transformCmp.position.x + xRel,
                        transformCmp.position.y,
                        transformCmp.dimension.x / 2f,
                        transformCmp.dimension.y / 2f,
                        transformCmp.dimension.x,
                        transformCmp.dimension.y,
                        transformCmp.scale,
                        transformCmp.scale,
                        transformCmp.rotation
                    )
                    xRel += transformCmp.dimension.x
                }

            } else if (baseInfoCmp.tag == EntityTag.FOREST) {
                var xRel = 0f
                for (i in 0 until 3) {
                    batch.draw(
                        textTureCmp.region,
                        transformCmp.position.x + xRel,
                        transformCmp.position.y,
                        transformCmp.dimension.x / 2f,
                        transformCmp.dimension.y / 2f,
                        transformCmp.dimension.x,
                        transformCmp.dimension.y,
                        transformCmp.scale,
                        transformCmp.scale,
                        transformCmp.rotation
                    )
                    xRel += transformCmp.dimension.x
                }

            } else {
                batch.draw(
                    textTureCmp.region,
                    transformCmp.position.x,
                    transformCmp.position.y,
                    transformCmp.dimension.x / 2f,
                    0f,
                    transformCmp.dimension.x,
                    transformCmp.dimension.y,
                    transformCmp.scale,
                    transformCmp.scale,
                    transformCmp.rotation
                )
            }
        }
    }

}