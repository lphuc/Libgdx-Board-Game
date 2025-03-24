package com.davik.twinhero.helpers

import com.badlogic.gdx.scenes.scene2d.ui.TextButton


class SizeAbleTextButton(text: String,
                         style: TextButtonStyle,
                         private val preWidth: Float,
                         private val preHeight: Float) : TextButton
    (text, style) {
    override fun getPrefHeight(): Float {
        return preHeight
    }

    override fun getPrefWidth(): Float {
        return preWidth
    }
}
