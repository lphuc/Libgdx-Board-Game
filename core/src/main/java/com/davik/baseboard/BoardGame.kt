package com.davik.baseboard

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.davik.baseboard.game.AssLoader
import com.davik.baseboard.game.AudioManager
import com.davik.baseboard.game.GameScreen
import com.davik.baseboard.game.LoadingScreen
import com.davik.baseboard.game.SplashScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync.initiate
import ktx.inject.Context
import ktx.inject.register

class BoardGame(val iCommonService: ICommonService?) : KtxGame<KtxScreen>() {
    lateinit var context: Context
    lateinit var audioManager: AudioManager
    var gameScreen: GameScreen? = null

    override fun create() {
        initiate()
        AssLoader.INST().initResources()
        context = Context()
        audioManager = AudioManager()
        context.register {
            bindSingleton(PolygonSpriteBatch())
            bindSingleton(BitmapFont())
            bindSingleton(PooledEngine())
            bindSingleton(AssLoader.INST())
            gameScreen = GameScreen(this@BoardGame, inject()).apply {
                addScreen(this)
            }
            addScreen(LoadingScreen(this@BoardGame, inject(), inject()))
            addScreen(SplashScreen(this@BoardGame, inject()))
        }
        setScreen<SplashScreen>()
        super.create()
    }


    override fun dispose() {

    }
}
