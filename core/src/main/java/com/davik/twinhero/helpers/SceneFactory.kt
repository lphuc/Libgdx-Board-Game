package com.davik.twinhero.helpers

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.davik.twinhero.game.AssLoader
import com.davik.twinhero.game.AssLoader.scaleUnit
import com.davik.twinhero.game.GameScreen
import com.davik.twinhero.game.components.BaseInfoComp
import com.davik.twinhero.game.components.EntityTag
import com.davik.twinhero.game.components.SpineComp
import com.davik.twinhero.game.components.TextureComp
import com.davik.twinhero.game.components.TransformComp
import com.esotericsoftware.spine.AnimationState
import com.esotericsoftware.spine.Skeleton
import ktx.ashley.entity
import ktx.ashley.with

fun GameScreen.initEnvironment() {
    engine.backGround()
    engine.mainGround()

    for (i in 0 until 10) {
        val xPos = if (i == 0) {
            MathUtils.random(-100f, 0f)
        } else {
            i * MathUtils.random(200f, 600f) * scaleUnit
        }
        engine.midTreeSpine(Vector2(xPos, 350f * scaleUnit))
    }
    for (i in 0 until 10) {
        val xPos = if (i == 0) {
            MathUtils.random(-100f, 0f)
        } else {
            i * MathUtils.random(100f, 500f) * scaleUnit
        }
        engine.mainGrass(Vector2(xPos, 300f * scaleUnit))
    }

    for (i in 0 until 5) {
        val xPos = if (i == 0) {
            MathUtils.random(-100f, 0f)
        } else {
            i * MathUtils.random(200f, 1000f) * scaleUnit
        }
        engine.mainTreeSpine(Vector2(xPos, MathUtils.random(260f * scaleUnit, 280f * scaleUnit)))
    }
    for (i in 0 until 10) {
        val xPos = if (i == 0) {
            MathUtils.random(-100f, 0f)
        } else {
            i * MathUtils.random(200f, 800f) * scaleUnit
        }
        engine.foreGrass(Vector2(xPos, MathUtils.random(-200f, -50f)))
    }
}

fun Engine.backGround(): Entity {
    val region = AssLoader.INST().atlasStatic.findRegion("background")
    return this.entity {
        with<TextureComp> {
            this.region = region
        }

        with<TransformComp> {
            scale = 1f
            dimension.x = region.regionWidth.toFloat() * 2f * scaleUnit
            dimension.y = region.regionHeight.toFloat() * 3.4f * scaleUnit
            position.set(0f, VIEWPORT_GUI_HEIGHT / 8f)
            drawOrder = DrawOrder.BACKGROUND.order
        }
        with<BaseInfoComp> {
            tag = EntityTag.BACKGROUND
        }
    }
}

fun Engine.mainGround(): Entity {
    val region = AssLoader.INST().atlasStatic.findRegion("mainground")
    return this.entity {
        with<TransformComp> {
            dimension.set(region.regionWidth.toFloat() * 1.2f * scaleUnit, region.regionHeight.toFloat() * 1.2f * scaleUnit)
            position.set(MathUtils.random(-100f, 0f), 0f)
            scale = 1f
            drawOrder = DrawOrder.GROUND.order
        }
        with<TextureComp> {
            this.region = region
        }
        with<BaseInfoComp> {
            tag = EntityTag.GROUND
        }
    }
}

fun Engine.midTreeSpine(spawnPos: Vector2): Entity {
    val mType = MathUtils.random(1, 3)
    val mScale = MathUtils.random(0.6f, 0.8f)
    spawnPos.y = (250f / mScale) * scaleUnit

    return this.entity {
        with<SpineComp> {
            skeleton = if (mType == 1) {
                Skeleton(MidTreeCfg.midTree1SklData).apply {
                    setPosition(spawnPos.x, spawnPos.y)
                    animationState = AnimationState(MidTreeCfg.midTree1AnimData)
                    animationState?.setAnimation(
                        MidTreeCfg.State.DEFAULT.trackIndex,
                        MidTreeCfg.State.DEFAULT.animName,
                        true
                    )?.apply {
                        timeScale = MathUtils.random(0.25f, 0.3f)
                    }
                }
            } else if (mType == 2) {
                Skeleton(MidTreeCfg.midTree2SklData).apply {
                    setPosition(spawnPos.x, spawnPos.y)
                    animationState = AnimationState(MidTreeCfg.midTree2AnimData)
                    animationState?.setAnimation(
                        MidTreeCfg.State.DEFAULT.trackIndex,
                        MidTreeCfg.State.DEFAULT.animName,
                        true
                    )?.apply {
                        timeScale = MathUtils.random(0.25f, 0.3f)
                    }
                }
            } else {
                Skeleton(MidTreeCfg.midTree3SklData).apply {
                    setPosition(spawnPos.x, spawnPos.y)
                    animationState = AnimationState(MidTreeCfg.midTree3AnimData)
                    animationState?.setAnimation(
                        MidTreeCfg.State.DEFAULT.trackIndex,
                        MidTreeCfg.State.DEFAULT.animName,
                        true
                    )?.apply {
                        timeScale = MathUtils.random(0.25f, 0.3f)
                    }
                }
            }
            skeleton?.scaleX = mScale * 1.2f * scaleUnit
            skeleton?.scaleY = mScale * scaleUnit
        }
        with<TransformComp> {
            position.set(spawnPos)
            this.scale = mScale * scaleUnit
            drawOrder = DrawOrder.MID_TREE.order
        }


        with<BaseInfoComp> {
            id = "midTree${System.currentTimeMillis().toString().takeLast(5)}"
            tag = EntityTag.OTHER
        }
    }
}

fun Engine.mainTreeSpine(spawnPos: Vector2): Entity {
    val mType = MathUtils.random(1, 3)
    val mScale = MathUtils.random(0.9f, 1.1f)
    return this.entity {
        with<SpineComp> {
            if (mType == 1) {
                skeleton = Skeleton(MainTreeCfg.tree1SklData).apply {
                    setPosition(spawnPos.x, spawnPos.y)
                    animationState = AnimationState(MainTreeCfg.tree1AnimData)
                    animationState?.setAnimation(
                        MainTreeCfg.State.DEFAULT.trackIndex,
                        MainTreeCfg.State.DEFAULT.animName,
                        true
                    )?.apply {
                        timeScale = MathUtils.random(0.25f, 0.3f)
                    }
                }
            } else if (mType == 2) {
                skeleton = Skeleton(MainTreeCfg.tree2SklData).apply {
                    setPosition(spawnPos.x, spawnPos.y)
                    animationState = AnimationState(MainTreeCfg.tree2AnimData)
                    animationState?.setAnimation(
                        MainTreeCfg.State.DEFAULT.trackIndex,
                        MainTreeCfg.State.DEFAULT.animName,
                        true
                    )?.apply {
                        timeScale = MathUtils.random(0.25f, 0.3f)
                    }
                }
            } else {
                skeleton = Skeleton(MainTreeCfg.tree3SklData).apply {
                    setPosition(spawnPos.x, spawnPos.y)
                    animationState = AnimationState(MainTreeCfg.tree3AnimData)
                    animationState?.setAnimation(
                        MainTreeCfg.State.DEFAULT.trackIndex,
                        MainTreeCfg.State.DEFAULT.animName,
                        true
                    )?.apply {
                        timeScale = MathUtils.random(0.25f, 0.3f)
                    }
                }
            }
            skeleton?.scaleX = mScale * scaleUnit
            skeleton?.scaleY = mScale * scaleUnit
        }
        with<TransformComp> {
            origin.set(spawnPos)
            position.set(spawnPos)
            this.scale = mScale * scaleUnit
            drawOrder = DrawOrder.MAIN_TREE.order
        }

        with<BaseInfoComp> {
            id = "mainTree${System.currentTimeMillis().toString().takeLast(5)}"
            tag = EntityTag.OTHER
        }
    }
}

fun Engine.mainGrass(spawnPos: Vector2): Entity {
    val mType = MathUtils.random(5, 8)
    val mScale = MathUtils.random(0.6f, 1.2f)
    spawnPos.y = (280f / mScale) * scaleUnit
    return this.entity {
        with<SpineComp> {
            skeleton = Skeleton(GrassCfg.skeletonData).apply {
                setPosition(spawnPos.x, spawnPos.y)
                animationState = AnimationState(GrassCfg.animStateData)
                setAttachment("grass", "grass${mType}")
            }
            val defaultAnim = GrassCfg.skeletonData.findAnimation(GrassCfg.State.DEFAULT.animName)
            animationState?.setAnimation(GrassCfg.State.DEFAULT.trackIndex, defaultAnim, true)
                ?.apply {
                    timeScale = MathUtils.random(0.15f, 0.2f) * 0.8f
                }
            skeleton?.scaleX = mScale * scaleUnit
            skeleton?.scaleY = mScale * scaleUnit
        }

        with<TransformComp> {
            origin.set(spawnPos)
            position.set(spawnPos)
            this.scale = mScale * scaleUnit
            drawOrder = DrawOrder.GRASS.order
        }

        with<BaseInfoComp> {
            id = "grass${System.currentTimeMillis().toString().takeLast(5)}"
            tag = EntityTag.OTHER
        }
    }
}


fun Engine.foreGrass(spawnPos: Vector2): Entity {
    val mType = MathUtils.random(5, 9)
    val mScale = MathUtils.random(0.8f, 1f)

    return this.entity {
        with<SpineComp> {
            skeleton = Skeleton(ForeGrassCfg.skeletonData).apply {
                setPosition(spawnPos.x, spawnPos.y)
                setAttachment("grass", "grass${mType}")
                animationState = AnimationState(ForeGrassCfg.animStateData)
                animationState?.setAnimation(
                    ForeGrassCfg.State.DEFAULT.trackIndex,
                    ForeGrassCfg.State.DEFAULT.animName,
                    true
                )?.apply {
                    timeScale = MathUtils.random(0.15f, 0.2f)
                }
            }
            skeleton?.scaleX = mScale * scaleUnit
            skeleton?.scaleY = mScale * scaleUnit
        }

        with<TransformComp> {
            origin.set(spawnPos)
            position.set(spawnPos)
            this.scale = mScale * scaleUnit
            drawOrder = DrawOrder.FORE_GRASS.order
        }

        with<BaseInfoComp> {
            id = "foreGrass${System.currentTimeMillis().toString().takeLast(5)}"
            tag = EntityTag.OTHER
        }
    }
}


