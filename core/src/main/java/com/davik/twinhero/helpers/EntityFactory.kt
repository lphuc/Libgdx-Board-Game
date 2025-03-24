package com.davik.twinhero.helpers

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.davik.twinhero.game.AssLoader
import com.davik.twinhero.game.components.BaseInfoComp
import com.davik.twinhero.game.components.EntityTag
import com.davik.twinhero.game.components.ItemAttrComp
import com.davik.twinhero.game.components.TextureComp
import ktx.ashley.entity
import ktx.ashley.with

fun Engine.slotItem(id: String, slotNum: Int, textureId: String, itemType: IItemType, attributes: MutableList<Attribute>): Entity {
    return this.entity {
        with<BaseInfoComp> {
            this.id = id //if character this will be playerId
            tag = if (itemType is ItemType) {
                EntityTag.FOOD
            } else {
                EntityTag.EQUIPMENT
            }
        }
        with<ItemAttrComp> {
            this.itemId = id
            this.itemType = itemType
            this.attributes = attributes
        }

        with<TextureComp> {
            this.textureId = textureId
            this.region = AssLoader.INST().slotItemAtlas.findRegion(textureId)
        }
    }
}