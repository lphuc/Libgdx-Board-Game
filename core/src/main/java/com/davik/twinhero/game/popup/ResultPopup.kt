package com.davik.twinhero.game.popup

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.davik.twinhero.game.AssLoader
import com.davik.twinhero.game.GameScreen
import com.davik.twinhero.game.animateHidePopup
import com.davik.twinhero.helpers.ColorNew
import com.davik.twinhero.helpers.SizeAbleTextButton
import com.davik.twinhero.helpers.VIEWPORT_GUI_HEIGHT
import com.davik.twinhero.helpers.VIEWPORT_GUI_WIDTH

class ResultPopup(val gameScreen: GameScreen) : Window("", AssLoader.INST().guiSkin, "popup") {
    private var okButton: SizeAbleTextButton
    private val buttonWidth = VIEWPORT_GUI_WIDTH / 5f
    private val contentTable = Table()
    private var canStartAnimation = false


    private val titleLbl = Label("", AssLoader.INST().labelStyleGiant).apply {
        color = Color.TAN
        setEllipsis(false)
    }

    private val resultLbl = Label("GAME OVER", AssLoader.INST().genericBoldStyle).apply {
        color = ColorNew.TAN0
        setEllipsis(false)
    }


    init {
        this.add(titleLbl).padTop(VIEWPORT_GUI_HEIGHT / 80f)
        this.row()
        contentTable.add(resultLbl)
        contentTable.center()
        this.add(contentTable).padTop(VIEWPORT_GUI_HEIGHT / 20f)
        this.row()

        val buttonStyle = TextButton.TextButtonStyle()
        buttonStyle.up = TextureRegionDrawable(AssLoader.INST().guiSkinAtlas.findRegion("upgrade_button_off"))
        buttonStyle.down = TextureRegionDrawable(AssLoader.INST().guiSkinAtlas.findRegion("upgrade_button_on"))
        buttonStyle.disabled = TextureRegionDrawable(AssLoader.INST().guiImageAtlas.findRegion("button_disabled"))
        buttonStyle.disabledFontColor = Color.GRAY
        buttonStyle.font = AssLoader.INST().genericBoldBitmap
        buttonStyle.fontColor = Color.WHITE

        okButton = SizeAbleTextButton("ok", buttonStyle, buttonWidth, buttonWidth / 2.4f)

        this.add(okButton).padTop(VIEWPORT_GUI_WIDTH / 7f)
        okButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (okButton.isDisabled) return
                gameScreen.animateHidePopup(this@ResultPopup)
                // TODO: reset game and show ads here
                gameScreen.restartGame()
            }
        })
        pack()

        this.height = VIEWPORT_GUI_WIDTH / 2f
        this.width = VIEWPORT_GUI_WIDTH / 1.2f
        this.isVisible = false
        this.top()
    }

    fun updateResult(gameOver: Boolean) {
        if (gameOver) {
            resultLbl.setText("GAME OVER")
        } else {
            resultLbl.setText("NEXT LEVEL")
        }
    }

    fun update(delta: Float) {
        if (canStartAnimation) {
        }
    }
}
