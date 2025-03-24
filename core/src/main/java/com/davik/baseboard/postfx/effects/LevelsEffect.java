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

/**
 * Controls levels of brightness and contrast.
 */
public class LevelsEffect extends ShaderVfxEffect implements ChainVfxEffect {
    private static final String Brightness = "u_brightness";
    private static final String Contrast = "u_contrast";
    private static final String Saturation = "u_saturation";
    private static final String Hue = "u_hue";
    private static final String Gamma = "u_gamma";

    private float brightness = 0.0f;
    private float contrast = 1.0f;
    private float saturation = 1.0f;
    private float hue = 1.0f;
    private float gamma = 1.0f;

    public LevelsEffect() {
        super(VfxGLUtils.compileShader(
                Gdx.files.classpath(COMMON_VERTEX_PATH),
                Gdx.files.classpath(BASE_PATH + "levels.frag")));
        rebind();
    }

    @Override
    public void rebind() {
        super.rebind();
        program.begin();
        program.setUniformi(U_TEXTURE0, TEXTURE_HANDLE0);
        program.setUniformf(Brightness, brightness);
        program.setUniformf(Contrast, contrast);
        program.setUniformf(Saturation, saturation);
        program.setUniformf(Hue, hue);
        program.setUniformf(Gamma, gamma);
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

    public float getContrast() {
        return contrast;
    }

    /**
     * Sets the contrast level
     *
     * @param contrast The contrast value in [0..2]
     */
    public void setContrast(float contrast) {
        this.contrast = contrast;
        setUniform(Contrast, this.contrast);
    }

    public float getBrightness() {
        return brightness;
    }

    /**
     * Sets the brightness level
     *
     * @param brightness The brightness value in [-1..1]
     */
    public void setBrightness(float brightness) {
        this.brightness = brightness;
        setUniform(Brightness, this.brightness);
    }

    public float getSaturation() {
        return saturation;
    }

    /**
     * Sets the saturation
     *
     * @param saturation The saturation level in [0..2]
     */
    public void setSaturation(float saturation) {
        this.saturation = saturation;
        setUniform(Saturation, this.saturation);
    }

    public float getHue() {
        return hue;
    }

    /**
     * Sets the hue
     *
     * @param hue The hue level in [0..2]
     */
    public void setHue(float hue) {
        this.hue = hue;
        setUniform(Hue, this.hue);
    }

    public float getGamma() {
        return gamma;
    }

    /**
     * Sets the gamma correction value
     *
     * @param gamma Gamma value in [0..3]
     */
    public void setGamma(float gamma) {
        this.gamma = gamma;
        setUniform(Gamma, this.gamma);
    }
}
