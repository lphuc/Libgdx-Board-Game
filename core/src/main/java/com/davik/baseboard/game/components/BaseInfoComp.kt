package com.davik.baseboard.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor

open class BaseInfoComp : Component, Pool.Poolable {
    companion object {
        val mapper = mapperFor<BaseInfoComp>()
    }

    var tag: EntityTag = EntityTag.OTHER
    var id: String = ""
    var name: String = ""
    var description: String = ""

    override fun reset() {
        tag = EntityTag.OTHER
        id = ""
        name = ""
        description = ""
    }
}

val Entity.baseInfoCmp: BaseInfoComp
    get() = this[BaseInfoComp.mapper] ?: throw KotlinNullPointerException("Trying to access a null BaseInfoComponent")

enum class EntityTag {
    CHARACTER, EQUIPMENT, FOOD, BACKGROUND, GROUND, FOREST, OTHER
}
