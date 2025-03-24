package com.davik.twinhero.helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.davik.twinhero.game.AssLoader
import com.esotericsoftware.spine.AnimationStateData
import com.esotericsoftware.spine.SkeletonData


object SpineSlotCfg {
    val slotSkeletonData: SkeletonData = AssLoader.INST().slotJson.readSkeletonData(Gdx.files.internal("spine/spine_slot.json"))
    val slotAnimData = AnimationStateData(slotSkeletonData)
}

object GrassCfg {
    var skeletonData: SkeletonData = AssLoader.INST().grassJson.readSkeletonData(Gdx.files.internal("spine/grass.json"))
    var animStateData: AnimationStateData = AnimationStateData(skeletonData)

    enum class State(
        val animName: String,
        val trackIndex: Int
    ) {
        DEFAULT("animation", 0)
    }
}

object ForeGrassCfg {
    var skeletonData: SkeletonData = AssLoader.INST().foreGrassJson.readSkeletonData(Gdx.files.internal("spine/fore_grass.json"))
    var animStateData: AnimationStateData = AnimationStateData(skeletonData)

    enum class State(
        val animName: String,
        val trackIndex: Int
    ) {
        DEFAULT("animation", 0)
    }
}

object MidTreeCfg {
    val midTree1SklData: SkeletonData = AssLoader.INST().midTree1Json.readSkeletonData(Gdx.files.internal("spine/mid_tree1.json"))
    val midTree1AnimData: AnimationStateData = AnimationStateData(midTree1SklData)
    var midTree2SklData: SkeletonData = AssLoader.INST().midTree2Json.readSkeletonData(Gdx.files.internal("spine/mid_tree2.json"))
    var midTree2AnimData: AnimationStateData = AnimationStateData(midTree2SklData)
    var midTree3SklData: SkeletonData = AssLoader.INST().midTree3Json.readSkeletonData(Gdx.files.internal("spine/mid_tree3.json"))
    var midTree3AnimData: AnimationStateData = AnimationStateData(midTree3SklData)

    enum class State(
        val animName: String,
        val trackIndex: Int
    ) {
        DEFAULT("default", 0)
    }
}

object MainTreeCfg {
    var tree1SklData: SkeletonData = AssLoader.INST().mainTreeJson.readSkeletonData(Gdx.files.internal("spine/main_tree.json"))
    var tree1AnimData: AnimationStateData = AnimationStateData(tree1SklData)
    var tree2SklData: SkeletonData = AssLoader.INST().mainTree2Json.readSkeletonData(Gdx.files.internal("spine/main_tree2.json"))
    var tree2AnimData: AnimationStateData = AnimationStateData(tree2SklData)
    var tree3SklData: SkeletonData = AssLoader.INST().mainTree3Json.readSkeletonData(Gdx.files.internal("spine/main_tree3.json"))
    var tree3AnimData: AnimationStateData = AnimationStateData(tree3SklData)

    enum class State(
        val animName: String,
        val trackIndex: Int
    ) {
        DEFAULT("default", 0)
    }
}
