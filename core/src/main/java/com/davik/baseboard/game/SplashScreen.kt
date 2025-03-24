package com.davik.baseboard.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.davik.baseboard.BoardGame
import com.davik.baseboard.helpers.VIEWPORT_GUI_HEIGHT
import com.davik.baseboard.helpers.VIEWPORT_GUI_WIDTH
import com.davik.baseboard.helpers.Vector1
import ktx.app.KtxScreen

class SplashScreen(
    private val game: BoardGame,
    private val batch: PolygonSpriteBatch,
) : KtxScreen {
    private var camera = OrthographicCamera()
    private var viewport: Viewport
    private var stage: Stage
    private var bgBlack: Image
    private var logoImg: Image
    private var loadingAtlas: TextureAtlas? = null
    private var sinceStart = 0L
    private var finished = false
    private var playedIntro = false
    private var logoYPos = Vector1(VIEWPORT_GUI_HEIGHT / 2.2f)
    private var logoScale = Vector1(1.8f)
    private var logoAlpha = Vector1(0f)
    private var presentYPos = Vector1(VIEWPORT_GUI_HEIGHT / 2.5f)
    private val introSound: Sound

    private val presentLbl = Label("PRESENTS", AssLoader.INST().labelStyleBig).apply {
        color = Color.WHITE
        setPosition(VIEWPORT_GUI_WIDTH / 2f - this.width / 2f, VIEWPORT_GUI_HEIGHT)
    }

    init {
        sinceStart = System.currentTimeMillis()
        introSound = Gdx.audio.newSound(Gdx.files.internal("sounds/intro3.mp3"))
        game.audioManager.playGUISound(introSound, 1f)
        loadingAtlas = TextureAtlas(Gdx.files.internal("static/loading.atlas"))

        camera.setToOrtho(false, VIEWPORT_GUI_WIDTH, VIEWPORT_GUI_HEIGHT)
        viewport = ScreenViewport(camera)
        camera.translate(VIEWPORT_GUI_WIDTH / 2f, VIEWPORT_GUI_HEIGHT / 2f)
        camera.update()
        stage = Stage(viewport)

        bgBlack = Image(loadingAtlas?.findRegion("bg_black")).apply {
            width = VIEWPORT_GUI_WIDTH
            height = VIEWPORT_GUI_HEIGHT
        }
        logoImg = Image(loadingAtlas?.findRegion("logo"))
        logoImg.setPosition(VIEWPORT_GUI_WIDTH / 2f - logoImg.width / 2f, 0f)
        logoImg.setScale(1f)
        stage.addActor(bgBlack)
        stage.addActor(logoImg)
        stage.addActor(presentLbl)
    }

    override fun render(delta: Float) {
        camera.update()
        batch.projectionMatrix = camera.combined
        presentYPos.lerp(Vector1(VIEWPORT_GUI_HEIGHT / 2.3f), 2f * delta)
        presentLbl.setPosition(VIEWPORT_GUI_WIDTH / 2f - presentLbl.width / 2f, presentYPos.value)

        logoImg.setPosition(VIEWPORT_GUI_WIDTH / 2f - logoImg.width, logoYPos.value)
        logoImg.setScale(logoScale.value)
        logoYPos.lerp(Vector1(VIEWPORT_GUI_HEIGHT / 2.2f), 14f * delta)
        logoScale.lerp(Vector1(2f), 1f * delta)
        logoAlpha.lerp(Vector1(1f), 1f * delta)

        if (System.currentTimeMillis() - sinceStart > 1000 && !playedIntro) {
            playedIntro = true
            game.audioManager.playGUISound(introSound, 1f)
        }

        if (System.currentTimeMillis() - sinceStart > 5000 && !finished) {
            finished = true
            game.removeScreen<SplashScreen>()
            dispose()
            game.setScreen<LoadingScreen>()
            introSound.dispose()
        }

        // stage.act(delta)
        batch.begin()
        presentLbl.draw(batch, logoAlpha.value)
        logoImg.draw(batch, logoAlpha.value)
        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun hide() {
        loadingAtlas?.dispose()
    }

    override fun dispose() {
        stage.dispose()
        loadingAtlas?.dispose()
        super.dispose()
    }

}