package com.davik.baseboard.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.MathUtils
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.newSingleThreadAsyncContext

open class AudioManager() {
    private var music: Music? = null
    private val workerThread = newSingleThreadAsyncContext()

    fun playGUISound(sound: Sound, volume: Float) {
        if (!LocalDataManager.INST().prefs.getBoolean(LocalDataManager.Settings.SOUND_ON, true)) return
        KtxAsync.launch(workerThread) {
            try {
                sound.play(1f)
            } catch (ignored: Throwable) {
            }
        }
    }

    fun playMenuMusic() {
        if (!LocalDataManager.INST().prefs.getBoolean(LocalDataManager.Settings.MUSIC_ON, false)) return
        try {
            if (music != null && music?.isPlaying == true) music?.stop()

            music = Gdx.audio.newMusic(Gdx.files.internal("sounds/menu_music.mp3"))
            music?.isLooping = true
            music?.volume = 0.5f
            music?.play()
        } catch (ignored: Exception) {
        }
    }

    fun playNewMusic() {
        if (!LocalDataManager.INST().prefs.getBoolean(LocalDataManager.Settings.MUSIC_ON, false)) return;
        try { // IMPORTANT: try catch must be put inside coroutineScope
            if (music != null && music?.isPlaying == true) music?.stop()
            val randMusic = MathUtils.random(0, 2)
            val path = if (randMusic == 0) {
                "sounds/music1.mp3"
            } else if (randMusic == 1) {
                "sounds/music2.mp3"
            } else {
                "sounds/music3.mp3"
            }
            music = Gdx.audio.newMusic(Gdx.files.internal(path))
            music?.isLooping = true
            music?.volume = 0.5f
            music?.play()
        } catch (ignored: Exception) {
        }
    }

    /**
     * only for playing music
     */
    fun stopMusic() {
        try {
            if (music != null && music?.isPlaying == true) {
                music?.stop()
            }
        } catch (ignored: Exception) {
        }
    }
}