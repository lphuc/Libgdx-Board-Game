package com.davik.baseboard.helpers

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.davik.baseboard.game.AssLoader
import java.text.DecimalFormat

fun sizeAbleLblStyleGiant(): Label.LabelStyle {
    return if (VIEWPORT_GUI_HEIGHT < 1080) AssLoader.INST().labelStyleBig else AssLoader.INST().labelStyleGiant
}

fun sizeAbleLblStyleBig(): Label.LabelStyle {
    return if (VIEWPORT_GUI_HEIGHT < 1080) AssLoader.INST().labelStyleMedium else if (VIEWPORT_GUI_HEIGHT < 1440) AssLoader.INST().labelStyleBig else AssLoader.INST().labelStyleGiant
}

fun sizeAbleLblStyleMedium(): Label.LabelStyle {
    return if (VIEWPORT_GUI_HEIGHT < 1080) AssLoader.INST().labelStyleSmall else if (VIEWPORT_GUI_HEIGHT < 1440) AssLoader.INST().genericTinyStyle else AssLoader.INST().genericLabelStyle
}

fun sizeAbleLblStyleSmall(): Label.LabelStyle {
    return if (VIEWPORT_GUI_HEIGHT < 1080) AssLoader.INST().labelStyleTiny else if (VIEWPORT_GUI_HEIGHT < 1440) AssLoader.INST().genericTinyStyle else AssLoader.INST().genericLabelStyle
}

fun sizeAbleLblStyleTiny(): Label.LabelStyle {
    return if (VIEWPORT_GUI_HEIGHT < 1080) AssLoader.INST().labelStyleSuperTiny else if (VIEWPORT_GUI_HEIGHT < 1440) AssLoader.INST().labelStyleTiny else AssLoader.INST().labelStyleSmall
}

fun sizeAbleBitmap(): BitmapFont {
    return if (VIEWPORT_GUI_HEIGHT < 1080) AssLoader.INST().tinyBitmap else if (VIEWPORT_GUI_HEIGHT < 1440) AssLoader.INST().mediumBitmap else AssLoader.INST().bigBitmap
}

fun findIngredientById(id: Int): ItemType {
    val map = ItemType.values().associateBy(ItemType::id)
    return map[id] ?: ItemType.EMPTY
}

fun Long?.safeLong(defaultValue: Long = 0) = this ?: defaultValue

@JvmOverloads
fun Float?.safeFloat(defaultValue: Float = 0f) = this ?: defaultValue

@JvmOverloads
fun Int?.safeInt(defaultValue: Int = 0) = this ?: defaultValue

@JvmOverloads
fun String?.safeString(defaultValue: String = ""): String = this ?: defaultValue

@JvmOverloads
fun Boolean?.safeBoolean(defaultValue: Boolean = false) = this ?: defaultValue

/**
 * manage all division between 2 number in game to avoid divided by zero (cause lag)
 */
fun Float.safeDiv(b: Float): Float {
    return if (b != 0f) {
        this / b
    } else {
        throw Throwable("Divided by zero")
    }
}

/**
 * avoid crash when number is NaN (very rare)
 */
fun Float.safeRoundToInt(): Int = when {
    isNaN() -> 1
    this > Int.MAX_VALUE -> Int.MAX_VALUE
    this < Int.MIN_VALUE -> Int.MIN_VALUE
    else -> Math.round(this)
}

fun Double.safeRoundToInt(): Int = when {
    isNaN() -> 1
    this > Int.MAX_VALUE -> Int.MAX_VALUE
    this < Int.MIN_VALUE -> Int.MIN_VALUE
    else -> Math.round(this).toInt()
}

fun Body.setTransformSafe(x: Float, y: Float, angel: Float) {
    if (!world.isLocked) {
        setTransform(x, y, angel)
    }
}

fun formatNumber(number: Float): String? {
    val df = DecimalFormat()
    df.maximumFractionDigits = 0
    return df.format(number.toDouble())
}

fun Float?.formatFloat(): String = run {
    val df = DecimalFormat("#.#")
    df.minimumFractionDigits = 1
    //    df.roundingMode = RoundingMode.CEILING
    df.format(this)
}

fun Float?.formatFloat2(): String = run {
    val df = DecimalFormat("#.##")
    //    df.roundingMode = RoundingMode.CEILING
    df.format(this)
}

fun String.extractNum(): String {
    return this.filter { it.isDigit() }.safeString("")
}

fun String.noEmptySpace() = replace(" ", "", true)

fun Float.lerp(target: Float, alpha: Float): Float {
    val invAlpha = 1.0f - alpha
    return this * target * invAlpha + target * alpha
}

fun Float.interpolate(target: Float, alpha: Float, interpolation: Interpolation): Float {
    return lerp(target, interpolation.apply(alpha))
}


fun getActorScreenPos(actor: Actor?): Vector2 {
    if (actor == null) return Vector2()
    return actor.localToStageCoordinates(Vector2(0f, 0f))
}

fun getAttributeColor(attrType: AttrType): Color {
    if (attrType == AttrType.PLUS_TIME) {
        return ColorNew.ORANGE4
    } else if (attrType == AttrType.PLUS_HP) {
        return ColorNew.RED4
    } else {
        return ColorNew.GREEN7
    }
}