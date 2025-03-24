package com.davik.baseboard.game.popup

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.davik.baseboard.game.AssLoader
import com.davik.baseboard.helpers.VIEWPORT_GUI_HEIGHT
import com.davik.baseboard.helpers.VIEWPORT_GUI_WIDTH

class ToastMessage(text: String, color: Color) : Window("", AssLoader.INST().guiSkin, "toast_msg") {
    var toastPosition = Vector2(0f, VIEWPORT_GUI_HEIGHT / 40f)
    var mAlpha = 0f
    var alphaUp = false
    private val msgLbl = Label(text, AssLoader.INST().genericBoldStyle).apply {
        this.color = color
        setEllipsis(false) // WORKAROUND: fix GlyphLayout random crash
    }

    init {
        this.add(msgLbl).center()
        touchable = Touchable.disabled
        isVisible = false
        pack()
        width = VIEWPORT_GUI_WIDTH / 1.8f
        height = width / 8f
        this.center()
    }

    fun start() {
        isVisible = true
        mAlpha = 0f
        alphaUp = true
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (alphaUp && mAlpha < 0.98f) {
            mAlpha += 2.5f * delta
        } else if (mAlpha > 0f) {
            mAlpha -= 2f * delta
        } else {
            mAlpha = 0f
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (isVisible) {
            super.draw(batch, mAlpha)
        }
    }

}
