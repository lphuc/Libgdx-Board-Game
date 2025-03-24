package com.davik.baseboard.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool.Poolable
import com.davik.baseboard.helpers.Attribute
import com.davik.baseboard.helpers.IItemType
import com.davik.baseboard.helpers.ItemType
import ktx.ashley.get
import ktx.ashley.mapperFor

class ItemAttrComp() : Component, Poolable {
    companion object {
        val mapper = mapperFor<ItemAttrComp>()
    }

    var itemId = ""
    var attributes = mutableListOf<Attribute>()
    var itemType: IItemType = ItemType.EMPTY
    var description = ""

    override fun reset() {
        itemType = ItemType.EMPTY
        attributes = mutableListOf()
        itemId = ""
        description = ""
    }
}

val Entity.itemAttrCmp: ItemAttrComp
    get() = this[ItemAttrComp.mapper] ?: throw KotlinNullPointerException("Trying to access an null ItemAttrComp")
