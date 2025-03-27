package com.davik.baseboard.helpers

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.davik.baseboard.game.dragndrop.SlotActor

interface IItemType

enum class ItemType(val textureId: String, val id: Int) : IItemType {
    WHITE_CARROT("food1", 1),
    APPLE("food2", 2),
    COCONUT("food3", 3),
    BANANA("food4", 4),
    TOMATO("food5", 5),
    STRAWBERRY("food6", 6),
    BLACK_GRAPE("food7", 7),
    GREEN_GRAPE("food8", 8),
    RED_GRAPE("food9", 9),
    PURPLE_GRAPE("food10", 10),
    BREAD("food11", 11),
    FISH("food12", 12),
    SALAD("food13", 13),
    CHERRY("food14", 14),
    LEMON("food15", 15),
    CHICKEN("food16", 16),
    EMPTY("", -1) //empty slot
}


enum class SpecialType(val textureId: String, val id: Int) : IItemType {
    SPECIAL_ITEM1("", 1)
}

enum class DrawOrder(val order: Int) {
    BACKGROUND(0),
    GROUND(1),
    MID_TREE(2),
    MAIN_TREE(2),
    GRASS(2),
    CHARACTER(3),
    FORE_GRASS(4)
}

data class Attribute(
    val type: AttrType,
    var value: Float,
)

enum class AttrType(val title: String) {
    PLUS_TIME("Plus Time"),
    PLUS_HP("Plus HP"),
    PLUS_XP("Plus XP"),
    UNKNOWN(""),
}


class DropItem(
    var itemImg: Image,
    var slotActor: SlotActor,
    val alpha: Vector1 = Vector1(1f),
    val size: Vector1,
    var yPos: Vector1,
    var originY: Float
)

//spine
interface AnimState {
    val animName: String
}