package com.davik.twinhero.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import ktx.ashley.mapperFor

class PlayerInfoComp : Component {
    var playerId: String = ""
    var displayName: String = ""
    var age: Int = 0
    var country: String = ""
    var motto: String = ""
    var win: Float = 0f
    var lose: Int = 0
    var rank: Int = 0
    var learnedTutorial = false
    var noAds = false
    var comebackTime = 0
    var ratedGame = false

    //character customization info
    var skin = 1
    var hair = 1
    var head = 1
    var eyebrown = 1
    var eye = 1
    var nose = 1
    var mouth = 1

    companion object {
        val mapper = mapperFor<PlayerInfoComp>()
    }
}

val Entity.playerInfoCmp: PlayerInfoComp
    get() = this[PlayerInfoComp.mapper] ?: throw KotlinNullPointerException("Trying to access a null PlayerInfoComp")
