package com.davik.baseboard.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Interpolation;

/**
 * Created by AGM on 10/02/2018.
 */

public class ScreenTransitionRotate implements ScreenTransition {
    private static final String TAG = ScreenTransitionRotate.class.getName();

    private static final ScreenTransitionRotate instance = new ScreenTransitionRotate();
    private float duration;
    private Interpolation interpolation;
    private float angle;
    private TransitionScaling scaling;

    public static ScreenTransitionRotate init(float duration, Interpolation interpolation, float angle, TransitionScaling scaling) {
        instance.duration = duration;
        instance.interpolation = interpolation;
        instance.angle = angle;
        instance.scaling = scaling;
        return instance;
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void render(PolygonSpriteBatch batch, Texture currentScreenTexture, Texture nextScreenTexture, float percent) {
        float width = currentScreenTexture.getWidth();
        float height = currentScreenTexture.getHeight();
        float x = 0;
        float y = 0;

        float scaleFactor;

        switch (scaling) {
            case IN:
                scaleFactor = percent;
                break;
            case OUT:
                scaleFactor = 1.0f - percent;
                break;
            case NONE:
            default:
                scaleFactor = 1.0f;
                break;
        }

        float rotation = 1;
        if (interpolation != null) {
            rotation = interpolation.apply(percent);
        }

        batch.begin();
        batch.draw(currentScreenTexture, 0, 0, width / 2, height / 2, width, height, 1, 1, 0, 0, 0, (int) width, (int) height, false,
                true);
        batch.draw(nextScreenTexture, 0, 0, width / 2, height / 2, nextScreenTexture.getWidth(), nextScreenTexture.getHeight(),
                scaleFactor, scaleFactor, rotation * angle, 0, 0, nextScreenTexture.getWidth(), nextScreenTexture.getHeight(), false,
                true);
        batch.end();

    }

    public enum TransitionScaling {
        NONE, IN, OUT
    }
}
