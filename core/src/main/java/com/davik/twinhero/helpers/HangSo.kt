package com.davik.twinhero.helpers

import com.badlogic.gdx.Gdx
import com.davik.twinhero.game.AssLoader.scaleUnit

// GUI Width
val VIEWPORT_GUI_WIDTH = Gdx.graphics.width.toFloat()

// GUI Height
val VIEWPORT_GUI_HEIGHT = Gdx.graphics.height.toFloat()

const val WORLD_GRAVITY = 9.8f

var CHARACTER_Y_POS = 200f * scaleUnit

const val LAST_HURT_DELAY = 100

const val SHOW_DEBUG_INFO = false