package com.davik.twinhero.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.get
import ktx.ashley.mapperFor

open class TextureComp(
    var region: TextureRegion? = null,
    var textureId: String = "",
) : Component, Poolable {
    companion object {
        val mapper = mapperFor<TextureComp>()
    }

    override fun reset() {
        region = null
        textureId = ""
    }
}

val Entity.textTureCmp: TextureComp
    get() = this[TextureComp.mapper] ?: throw KotlinNullPointerException("Trying to access a null TextureComponent")
