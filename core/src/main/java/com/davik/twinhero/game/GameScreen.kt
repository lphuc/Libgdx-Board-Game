package com.davik.twinhero.game

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.davik.twinhero.TwinHero
import com.davik.twinhero.game.LocalDataManager.CHARACTER_LV
import com.davik.twinhero.game.LocalDataManager.CHARACTER_XP
import com.davik.twinhero.game.dragndrop.SLOT_WIDTH
import com.davik.twinhero.game.popup.ResultPopup
import com.davik.twinhero.game.popup.ToastMessage
import com.davik.twinhero.game.systems.RemoveSystem
import com.davik.twinhero.game.systems.RenderSystem
import com.davik.twinhero.helpers.CenterImageTextButton
import com.davik.twinhero.helpers.VIEWPORT_GUI_HEIGHT
import com.davik.twinhero.helpers.VIEWPORT_GUI_WIDTH
import com.davik.twinhero.helpers.Vector1
import com.davik.twinhero.helpers.initEnvironment
import com.davik.twinhero.helpers.safeFloat
import com.davik.twinhero.helpers.safeRoundToInt
import com.davik.twinhero.tween.ActorAccessor
import com.davik.twinhero.tween.engine.Tween
import com.davik.twinhero.tween.engine.TweenManager
import ktx.app.KtxScreen
import ktx.async.newSingleThreadAsyncContext
import kotlin.math.abs


class GameScreen(val twinHero: TwinHero, val batch: PolygonSpriteBatch) : KtxScreen {
    val workerThread = newSingleThreadAsyncContext() //all background job should use this in KtxAsync
    var engine = PooledEngine()
    val multiplexer = InputMultiplexer()
    var sceneCamera = OrthographicCamera()
    private var guiCamera = OrthographicCamera()
    private var viewport: Viewport = ScreenViewport(guiCamera)
    private var stage: Stage = Stage(viewport)
    lateinit var boardWindow: BoardWindow
    private var resultPopup: ResultPopup? = null
    private var listToastMsg = mutableListOf<ToastMessage>()

    //flying off item from board
    var movingItemImg: Image? = null
    var movingItem: Entity? = null
    var movingItemAlpha = Vector1(0f)
    var movingItemSize = Vector1(SLOT_WIDTH) // max size = SLOT_WIDTH * 2f
    var movingItemPos = Vector2()
    var originYPos = 0f

    var characterLv = LocalDataManager.INST().prefs.getInteger(CHARACTER_LV, 1)
    var characterXp = LocalDataManager.INST().prefs.getInteger(CHARACTER_XP, 1)

    private val XP_LEVEL_RATIO = 20f //default is 20f, multiply with current level to get the needed level up xp
    private var xpBarRatio = XP_LEVEL_RATIO / characterLv * XP_LEVEL_RATIO
    private val maxXpBarWidth = Vector2(VIEWPORT_GUI_WIDTH / 1.4f, 0f)
    private var fullXpBarImg: Image? = null
    private val xpBgHeight = VIEWPORT_GUI_HEIGHT / 24f
    private val xpBarHeight = VIEWPORT_GUI_HEIGHT / 76f
    private var xpRatio = 1f
    private var xpBarWidth = Vector2(0f, 0f)
    private var currentXp = Vector2(0f, 0f)
    private var xpLabel: Label = Label("0/10", AssLoader.INST().genericBoldStyle).apply {
        setEllipsis(false)
    }
    private var tweenManager: TweenManager = TweenManager().apply {
        Tween.registerAccessor(Window::class.java, ActorAccessor())
        Tween.registerAccessor(Stack::class.java, ActorAccessor())
        Tween.registerAccessor(CenterImageTextButton::class.java, ActorAccessor())
    }

    init {
        sceneCamera.setToOrtho(false, VIEWPORT_GUI_WIDTH, VIEWPORT_GUI_HEIGHT)
        sceneCamera.translate(0f, 0f)
        sceneCamera.update()

        guiCamera.setToOrtho(false, VIEWPORT_GUI_WIDTH, VIEWPORT_GUI_HEIGHT)
        guiCamera.translate(0f, 0f)
        guiCamera.update()
    }

    override fun show() { // initialize entity engine
        engine.apply {
            addSystem(RemoveSystem(this@GameScreen)) //should be the first system to be called
            addSystem(RenderSystem(batch, sceneCamera, this@GameScreen))
        }


        // playerHUD.showInfoPopup("LOAD TIME = ${semideus.loadingTime}", "test speed")
        Gdx.input.inputProcessor = multiplexer
        Gdx.input.setCatchKey(Input.Keys.BACK, true)
        initEnvironment()
        boardWindow = BoardWindow(this)
        animateShowPopup(boardWindow)
        initXpBar()

        boardWindow.updateSlotSpinePosition()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        sceneCamera.update()
        guiCamera.update()
        engine.update(delta)
        tweenManager.update(delta)
        boardWindow.update(delta)
        updateToastMessages(delta)
        updateXpBar(delta)
        renderGUI(batch, delta)
    }

    private fun renderGUI(batch: PolygonSpriteBatch, delta: Float) {

        batch.projectionMatrix = guiCamera.combined
        batch.begin()
        stage.act()
        stage.draw()
        batch.end()
        // has to use another batch.begin() for GUI UI that doesn't belong to playerHUD otherwise it won't render correctly
        batch.color = Color.WHITE //reset color to white otherwise gui item color won't show correctly
    }

    private fun animateShowPopup(popup: Window, x: Float? = null, y: Float? = null, errorType: Boolean = false) {
        if (popup.isVisible) return
        twinHero.audioManager.playGUISound(AssLoader.INST().popupShowSound, 1f)
        val xPos = x ?: (VIEWPORT_GUI_WIDTH / 2 - popup.width / 2f)
        val yPos = y ?: (VIEWPORT_GUI_HEIGHT / 2 - popup.height / 2f)
        popup.toFront()
        popup.isVisible = true
        stage.addActor(popup)
        val startYPos = VIEWPORT_GUI_HEIGHT / 2f - popup.height / 2.25f
        popup.setPosition(xPos, yPos)
        Tween.set(popup, ActorAccessor.ALPHA).target(0f).start(tweenManager)
        Tween.to(popup, ActorAccessor.ALPHA, 0.3f).target(1f).setCallback { type, source ->
            popup.toFront()
        }.start(tweenManager)
        Tween.from(popup, ActorAccessor.Y, 0.4f).target(startYPos).start(tweenManager)
        // tweenManager?.update(Float.MIN_VALUE) // update once avoid short flash of splash before animation
        multiplexer.addProcessor(popup.stage)
    }

    fun animateHidePopup(popup: Window) {
        if (!popup.isVisible) return
        twinHero.audioManager.playGUISound(AssLoader.INST().hidePopupShow, 0.6f)
        val targetYPos = VIEWPORT_GUI_HEIGHT / 2f - popup.height / 1.7f
        Tween.to(popup, ActorAccessor.Y, 0.3f).target(targetYPos).start(tweenManager)
        Tween.to(popup, ActorAccessor.ALPHA, 0.3f).target(0f).setCallback { type, source ->
            popup.isVisible = false
            popup.addAction(Actions.removeActor())
        }.start(tweenManager)
        // tweenManager?.update(Float.MIN_VALUE) // update once avoid short flash of splash before animation
        multiplexer.removeProcessor(popup.stage)
    }

    fun showToastMessage(text: String, color: Color) {
        val toastMsg = ToastMessage(text.uppercase(), color)
        toastMsg.setPosition(VIEWPORT_GUI_WIDTH / 2f - toastMsg.width / 2f, VIEWPORT_GUI_HEIGHT / 40f)
        toastMsg.start()
        stage.addActor(toastMsg)
        listToastMsg.add(toastMsg)
        twinHero.audioManager.playGUISound(AssLoader.INST().menuTabSound, 0.5f)
    }

    private fun updateToastMessages(delta: Float) {
        listToastMsg.removeIf { !it.isVisible }
        listToastMsg.forEach { toastMsg ->
            if (toastMsg.isVisible) {
                if ((toastMsg.y < VIEWPORT_GUI_HEIGHT / 12.8f && toastMsg.mAlpha > 0f) || toastMsg.alphaUp) {
                    toastMsg.toastPosition.interpolate(Vector2(0f, VIEWPORT_GUI_HEIGHT / 12.8f), delta * 2f, Interpolation.pow2Out)
                    toastMsg.y = toastMsg.toastPosition.y
                    toastMsg.toFront()
                } else {
                    toastMsg.isVisible = false
                    toastMsg.remove()
                }

                if (toastMsg.y >= VIEWPORT_GUI_HEIGHT / 12.9f) {
                    toastMsg.alphaUp = false //decrease popup transparency
                }
            }
        }
    }

    fun restartGame() {
        boardWindow.resetBoardTable()
        boardWindow.timeLbl.setText("00:00")
    }

    fun showResultPopup(gameOver: Boolean) {
        if (resultPopup == null) {
            resultPopup = ResultPopup(this)
        }
        resultPopup?.updateResult(gameOver)
        animateShowPopup(resultPopup!!)
    }

    private fun initXpBar() { //groups
        val emptyBar = AssLoader.INST().guiImageAtlas.findRegion("xp_line_off2")
        val xpBarBg = AssLoader.INST().guiImageAtlas.findRegion("xp_holder_bg")
        val fullXpBar = AssLoader.INST().guiImageAtlas.findRegion("xp_line2")
        val xpTopBar = AssLoader.INST().guiImageAtlas.findRegion("power_bar_empty")
        val xpBarGroup = WidgetGroup()
        val xpBarBgImg = Image(xpBarBg)
        val emptyBarImg = Image(emptyBar)
        fullXpBarImg = Image(fullXpBar)
        val xpTopBarImg = Image(xpTopBar)
        xpBarGroup.setPosition(VIEWPORT_GUI_WIDTH / 2f - maxXpBarWidth.x * 1.07f / 2f, VIEWPORT_GUI_HEIGHT - xpBgHeight)
        xpBarBgImg.width = maxXpBarWidth.x * 1.07f
        xpBarBgImg.height = xpBgHeight
        xpRatio = maxXpBarWidth.x / (characterLv * XP_LEVEL_RATIO * xpBarRatio)

        emptyBarImg.width = maxXpBarWidth.x
        emptyBarImg.height = xpBarHeight
        emptyBarImg.setPosition(maxXpBarWidth.x / 32f, xpBarHeight + xpBgHeight / 14f)
        fullXpBarImg?.width = maxXpBarWidth.x
        fullXpBarImg?.height = xpBarHeight * 0.96f
        fullXpBarImg?.setPosition(maxXpBarWidth.x / 32f, xpBarHeight + xpBgHeight / 14f)

        xpTopBarImg.width = maxXpBarWidth.x * 1.015f
        xpTopBarImg.height = xpBarHeight * 2f
        xpTopBarImg.setPosition(maxXpBarWidth.x / 42f, xpBarHeight + xpBgHeight / 30f)

        xpLabel.setPosition(maxXpBarWidth.x / 2f - xpLabel.width.safeFloat() / 3f, xpBarHeight)
        xpBarGroup.addActor(xpBarBgImg)
        xpBarGroup.addActor(emptyBarImg)
        xpBarGroup.addActor(fullXpBarImg)
        xpBarGroup.addActor(xpLabel)
        stage.addActor(xpBarGroup)
    }

    private fun updateXpBar(delta: Float) {
        if (xpBarWidth.x >= maxXpBarWidth.x - maxXpBarWidth.x / XP_LEVEL_RATIO && characterXp >= characterLv * XP_LEVEL_RATIO) {
            characterXp = abs(characterXp - (XP_LEVEL_RATIO * characterLv).safeRoundToInt())
            characterLv++
            xpBarRatio = XP_LEVEL_RATIO / characterLv * XP_LEVEL_RATIO
            xpBarWidth.x = 0f
            currentXp.x = 0f
            twinHero.audioManager.playGUISound(AssLoader.INST().levelUpSound, 1f)
            LocalDataManager.INST().prefs.putInteger(CHARACTER_LV, characterLv).flush()
        } else if (currentXp.x < characterXp || currentXp.x >= characterXp) {
            currentXp.lerp(Vector2(characterXp.toFloat(), 0f), 2f * delta)
            xpBarWidth.lerp(Vector2(getXpBarWidth(), 0f), 2f * delta)
        }

        fullXpBarImg?.width = xpBarWidth.x
        xpLabel.setText("${currentXp.x.safeRoundToInt()}/${(characterLv * XP_LEVEL_RATIO).safeRoundToInt()}")
    }

    private fun getXpBarWidth(): Float {
        return xpRatio * characterXp * xpBarRatio
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
    }

    override fun dispose() {
        super.dispose()
    }
}
