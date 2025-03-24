package com.davik.twinhero.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.davik.twinhero.TwinHero
import com.davik.twinhero.helpers.VIEWPORT_GUI_HEIGHT
import com.davik.twinhero.helpers.VIEWPORT_GUI_WIDTH
import com.davik.twinhero.helpers.safeFloat
import com.davik.twinhero.helpers.safeRoundToInt
import com.davik.twinhero.helpers.sizeAbleLblStyleMedium
import com.davik.twinhero.helpers.ColorNew
import ktx.app.KtxScreen

/*
 * Created by Davik on 6/16/2023.
 */
class LoadingScreen(
    private val game: TwinHero,
    private val batch: PolygonSpriteBatch,
    private val assLoader: AssLoader,
) : KtxScreen {
    private var camera = OrthographicCamera()
    private var viewport: Viewport
    private var stage: Stage
    private var bgImage: Image
    private val loadingBarGroup = WidgetGroup()
    private var loadingAtlas: TextureAtlas? = null
    private var fullBar: TextureRegion? = null
    private var fullBarImg: Image? = null
    private var maxBarWidth = VIEWPORT_GUI_WIDTH / 1.8f
    private val maxBarWidthVec = Vector2(maxBarWidth, 0f) //for interpolation purpose

    private val frameBgHeight = VIEWPORT_GUI_HEIGHT / 12f
    private val barHeight = VIEWPORT_GUI_HEIGHT / 40f
    private var barWidth = Vector2(0f, 0f)
    private val progressLabel = Label("0%", sizeAbleLblStyleMedium())
    private var copyRightLbl: Label = Label("Copyright © 2025 Davik Universe. All rights reserved.", AssLoader.INST().genericLabelStyle).apply {
        color = ColorNew.TAN3
        setPosition(VIEWPORT_GUI_WIDTH / 2 - width / 2f, 20f)
    }

    init {
        loadingAtlas = TextureAtlas(Gdx.files.internal("static/loading.atlas"))
        barWidth = Vector2(0f, 0f)
        camera.setToOrtho(false, VIEWPORT_GUI_WIDTH, VIEWPORT_GUI_HEIGHT)
        viewport = ScreenViewport(camera)
        camera.translate(VIEWPORT_GUI_WIDTH / 2f, VIEWPORT_GUI_HEIGHT / 2f)
        camera.update()
        stage = Stage(viewport)

        val barBg = loadingAtlas?.findRegion("xp_holder_bg")
        val emptyBar = loadingAtlas?.findRegion("xp_line_off2")
        fullBar = loadingAtlas?.findRegion("xp_line2")
        fullBarImg = Image(fullBar)

        val barBgImg = Image(barBg)
        val emptyBarImg = Image(emptyBar)
        val libgdxIcon = Image(loadingAtlas?.findRegion("ic_libgdx")).apply {
            width = maxBarWidth / 8f
            height = maxBarWidth / 24f
            setPosition(VIEWPORT_GUI_WIDTH - maxBarWidth / 7f, 20f)
        }

        bgImage = Image(loadingAtlas?.findRegion("bg_loading"))
        val ratio = bgImage.prefHeight / bgImage.prefWidth

        bgImage.width = VIEWPORT_GUI_WIDTH
        bgImage.height = bgImage.width * ratio
        bgImage.setPosition(0f, (VIEWPORT_GUI_HEIGHT - bgImage.height) / 2f)


        stage.addActor(bgImage)

        loadingBarGroup.setPosition(VIEWPORT_GUI_WIDTH / 2f - maxBarWidthVec.x / 2f, VIEWPORT_GUI_HEIGHT / 16f)
        barBgImg.width = maxBarWidthVec.x * 1.07f
        barBgImg.height = frameBgHeight

        emptyBarImg.width = maxBarWidthVec.x
        emptyBarImg.height = barHeight
        emptyBarImg.setPosition(maxBarWidthVec.x / 32f, barHeight + frameBgHeight / 30f)
        fullBarImg?.width = 0f
        fullBarImg?.height = barHeight * 0.96f
        fullBarImg?.setPosition(maxBarWidthVec.x / 32f, barHeight + frameBgHeight / 30f)

        progressLabel.setPosition(maxBarWidthVec.x / 2f - progressLabel.width.safeFloat() / 3f, barHeight * 1.1f)
        loadingBarGroup.addActor(barBgImg)
        loadingBarGroup.addActor(emptyBarImg)
        loadingBarGroup.addActor(fullBarImg)
        loadingBarGroup.addActor(progressLabel)
        stage.addActor(loadingBarGroup)
        stage.addActor(copyRightLbl)
        stage.addActor(libgdxIcon)
    }

    override fun render(delta: Float) {
        camera.update()
        batch.projectionMatrix = camera.combined
        if (AssLoader.INST().assetManager.update() && !AssLoader.INST().resourceLoaded) {
            AssLoader.INST().assignResources() //IMPORTANT -> need to call before open game screen
        }
        if (assLoader.assetManager.progress >= 1f && barWidth.x >= getBarWidth() - 1f) {
            game.removeScreen<LoadingScreen>()
            dispose()
            game.setScreen<GameScreen>()
        }
        updateBar(delta)
        stage.act(delta)
        stage.draw()
    }

    private var loadingProgressVec = Vector2(0f, 0f) //for interpolation purpose

    private fun getBarWidth(): Float {
        return maxBarWidth * AssLoader.INST().assetManager.progress
    }

    private fun updateBar(delta: Float) {
        loadingProgressVec.lerp(Vector2(AssLoader.INST().assetManager.progress * 100f, 0f), 1f)
        barWidth.lerp(Vector2(getBarWidth(), 0f), 1f)
        fullBarImg?.width = barWidth.x
        progressLabel.setText("${loadingProgressVec.x.safeRoundToInt()}%")
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun hide() {
    }

    override fun dispose() {
        loadingAtlas?.dispose()
        stage.dispose()
        super.dispose()
    }

}