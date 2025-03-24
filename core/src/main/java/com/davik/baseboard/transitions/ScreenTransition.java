package com.davik.baseboard.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;

public interface ScreenTransition {
     float getDuration();
     void render(PolygonSpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha);
}
