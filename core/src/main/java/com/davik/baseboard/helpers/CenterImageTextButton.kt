package com.davik.baseboard.helpers

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Null
import com.badlogic.gdx.utils.Scaling


class CenterImageTextButton(@Null text: String?, private var style: ImageTextButton.ImageTextButtonStyle) : Button(style) {
    val stack = Stack()
    var image: Image?
    private var label: Label?

    constructor(@Null text: String?, skin: Skin) : this(text, skin[ImageTextButton.ImageTextButtonStyle::class.java]) {
        setSkin(skin)
    }

    constructor(@Null text: String?, skin: Skin, styleName: String?) : this(
        text, skin[styleName, ImageTextButton.ImageTextButtonStyle::class.java]
    ) {
        setSkin(skin)
    }

    init {
        defaults().space(3f)
        image = newImage()
        label = newLabel(text, LabelStyle(style.font, style.fontColor))
        label?.setAlignment(Align.bottomRight)
        stack.add(image)
        stack.add(label)
        this.add(stack)
        setStyle(style)
        setSize(prefWidth, prefHeight)
    }

    private fun newImage(): Image {
        return Image(null as Drawable?, Scaling.fit)
    }

    private fun newLabel(text: String?, style: LabelStyle?): Label {
        return Label(text, style)
    }

    override fun setStyle(style: ButtonStyle) {
        require(style is ImageTextButton.ImageTextButtonStyle) { "style must be a ImageTextButtonStyle." }
        this.style = style
        super.setStyle(style)
        if (image != null) updateImage()
        if (label != null) {
            val labelStyle = label?.style
            labelStyle?.font = style.font
            labelStyle?.fontColor = fontColor
            label?.style = labelStyle
        }
    }

    override fun getStyle(): ImageTextButton.ImageTextButtonStyle {
        return style
    }

    private val imageDrawable: Drawable?
        get() {
            if (isDisabled && style.imageDisabled != null) return style.imageDisabled
            if (isPressed) {
                if (isChecked && style.imageCheckedDown != null) return style.imageCheckedDown
                if (style.imageDown != null) return style.imageDown
            }
            if (isOver) {
                if (isChecked) {
                    if (style.imageCheckedOver != null) return style.imageCheckedOver
                } else {
                    if (style.imageOver != null) return style.imageOver
                }
            }
            if (isChecked) {
                if (style.imageChecked != null) return style.imageChecked
                if (isOver && style.imageOver != null) return style.imageOver
            }
            return style.imageUp
        }

    /** Sets the image drawable based on the current button state. The default implementation sets the image drawable using*/
    private fun updateImage() {
        image?.drawable = imageDrawable
    }

    /** Returns the appropriate label font color from the style based on the current button state.  */
    private val fontColor: Color
        get() {
            if (isDisabled && style.disabledFontColor != null) return style.disabledFontColor
            if (isPressed) {
                if (isChecked && style.checkedDownFontColor != null) return style.checkedDownFontColor
                if (style.downFontColor != null) return style.downFontColor
            }
            if (isOver) {
                if (isChecked) {
                    if (style.checkedOverFontColor != null) return style.checkedOverFontColor
                } else {
                    if (style.overFontColor != null) return style.overFontColor
                }
            }
            val focused = hasKeyboardFocus()
            if (isChecked) {
                if (focused && style.checkedFocusedFontColor != null) return style.checkedFocusedFontColor
                if (style.checkedFontColor != null) return style.checkedFontColor
                if (isOver && style.overFontColor != null) return style.overFontColor
            }
            return if (focused && style.focusedFontColor != null) style.focusedFontColor else Color.WHITE
        }

    override fun draw(batch: Batch, parentAlpha: Float) {
        updateImage()
        label?.style?.fontColor = fontColor
        super.draw(batch, parentAlpha)
    }

    fun setLabel(label: Label?) {
        this.label = label
    }

    var text: CharSequence?
        get() = label?.text
        set(text) {
            label?.setText(text)
        }

    override fun toString(): String {
        val name = name
        if (name != null) return name
        var className = javaClass.name
        val dotIndex = className.lastIndexOf('.')
        if (dotIndex != -1) className = className.substring(dotIndex + 1)
        return ((if (className.indexOf('$') != -1) "ImageTextButton " else "") + className + ": " + image?.drawable + " " + label?.text)
    }
}
