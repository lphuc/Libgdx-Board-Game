package com.davik.baseboard.postfx.effects;

import static com.davik.baseboard.postfx.util.ConstKt.BASE_PATH;
import static com.davik.baseboard.postfx.util.ConstKt.U_TEXTURE0;
import static com.davik.baseboard.postfx.util.ConstKt.COMMON_VERTEX_PATH;

import com.badlogic.gdx.Gdx;
import com.davik.baseboard.postfx.core.VfxRenderContext;
import com.davik.baseboard.postfx.core.base.ChainVfxEffect;
import com.davik.baseboard.postfx.core.base.ShaderVfxEffect;
import com.davik.baseboard.postfx.core.framebuffer.VfxFrameBuffer;
import com.davik.baseboard.postfx.core.framebuffer.VfxPingPongWrapper;
import com.davik.baseboard.postfx.core.gl.VfxGLUtils;

public class VignettingEffect extends ShaderVfxEffect implements ChainVfxEffect {
    private static final String VIGNETTE_INTENSITY = "u_vignetteIntensity";
    private static final String VIGNETTE_X = "u_vignetteX";
    private static final String VIGNETTE_Y = "u_vignetteY";
    private static final String CENTER_X = "u_centerX";
    private static final String CENTER_Y = "u_centerY";
    private static final String SATURATION = "u_saturation";
    private static final String SATURATION_MUL = "u_saturationMul";


    private float vignetteX = 0.8f;
    private float vignetteY = 0.25f;
    private float centerX = 0.5f;
    private float centerY = 0.5f;
    private float intensity = 1f;

    private final boolean saturationEnabled;
    private float saturation = 0f;
    private float saturationMul = 0f;

    public VignettingEffect(boolean controlSaturation) {
        super(VfxGLUtils.compileShader(
                Gdx.files.classpath(COMMON_VERTEX_PATH),
                Gdx.files.classpath(BASE_PATH + "vignetting.frag"),
                (controlSaturation ? "#define CONTROL_SATURATION" : "")));
        this.saturationEnabled = controlSaturation;
        rebind();
    }

    @Override
    public void rebind() {
        program.begin();
        program.setUniformi(U_TEXTURE0, TEXTURE_HANDLE0);

        if (saturationEnabled) {
            program.setUniformf(SATURATION, saturation);
            program.setUniformf(SATURATION_MUL, saturationMul);
        }

        program.setUniformf(VIGNETTE_INTENSITY, intensity);
        program.setUniformf(VIGNETTE_X, vignetteX);
        program.setUniformf(VIGNETTE_Y, vignetteY);
        program.setUniformf(CENTER_X, centerX);
        program.setUniformf(CENTER_Y, centerY);
        program.end();
    }

    @Override
    public void render(VfxRenderContext context, VfxPingPongWrapper buffers) {
        render(context, buffers.getSrcBuffer(), buffers.getDstBuffer());
    }

    public void render(VfxRenderContext context, VfxFrameBuffer src, VfxFrameBuffer dst) {
        // Bind src buffer's texture as a primary one.
        src.getTexture().bind(TEXTURE_HANDLE0);
        // Apply shader effect and render result to dst buffer.
        renderShader(context, dst);
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
        setUniform(VIGNETTE_INTENSITY, intensity);
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
        if (saturationEnabled) {
            setUniform(SATURATION, saturation);
        }
    }

    public void setSaturationMul(float saturationMul) {
        this.saturationMul = saturationMul;
        if (saturationEnabled) {
            setUniform(SATURATION_MUL, saturationMul);
        }
    }

    public void setCoords(float x, float y) {
        this.vignetteX = x;
        this.vignetteY = y;
        program.begin();
        program.setUniformf(VIGNETTE_X, x);
        program.setUniformf(VIGNETTE_Y, y);
        program.end();
    }

    public void setVignetteX(float x) {
        this.vignetteX = x;
        setUniform(VIGNETTE_X, x);
    }

    public void setVignetteY(float vignetteY) {
        this.vignetteY = vignetteY;
        setUniform(VIGNETTE_Y, vignetteY);
    }

    /**
     * Specify the center, in normalized screen coordinates.
     */
    public void setCenter(float x, float y) {
        this.centerX = x;
        this.centerY = y;

        program.begin();
        program.setUniformf(CENTER_X, centerX);
        program.setUniformf(CENTER_Y, centerY);
        program.end();
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getVignetteX() {
        return vignetteX;
    }

    public float getVignetteY() {
        return vignetteY;
    }

    public float getIntensity() {
        return intensity;
    }

    public float getSaturation() {
        return saturation;
    }

    public float getSaturationMul() {
        return saturationMul;
    }

    public boolean isSaturationControlEnabled() {
        return saturationEnabled;
    }
}
