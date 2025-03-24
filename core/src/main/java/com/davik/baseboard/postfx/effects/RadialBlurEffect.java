package com.davik.baseboard.postfx.effects;

import static com.davik.baseboard.postfx.util.ConstKt.BASE_PATH;
import static com.davik.baseboard.postfx.util.ConstKt.U_TEXTURE0;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.davik.baseboard.postfx.core.VfxRenderContext;
import com.davik.baseboard.postfx.core.base.ChainVfxEffect;
import com.davik.baseboard.postfx.core.base.ShaderVfxEffect;
import com.davik.baseboard.postfx.core.framebuffer.VfxFrameBuffer;
import com.davik.baseboard.postfx.core.framebuffer.VfxPingPongWrapper;
import com.davik.baseboard.postfx.core.gl.VfxGLUtils;

public class RadialBlurEffect extends ShaderVfxEffect implements ChainVfxEffect {

    private static String U_BLUR_DIV = "u_blurDiv";
    private static String U_OFFSET_X = "u_offsetX";
    private static String U_OFFSET_Y = "u_offsetY";
    private static String U_ZOOM = "u_zoom";

    private final int passes;

    private float strength = 0.2f;
    private float originX = 0.5f;
    private float originY = 0.5f;
    private float zoom = 1f;

    public RadialBlurEffect(int passes) {
        super(VfxGLUtils.compileShader(
            Gdx.files.classpath("assets/shaders/radial_blur.vert"),
            Gdx.files.classpath(BASE_PATH + "radial_blur.frag"),
                "#define PASSES " + passes));
        this.passes = passes;
        rebind();
    }

    @Override
    public void rebind() {
        super.rebind();
        program.begin();
        program.setUniformi(U_TEXTURE0, TEXTURE_HANDLE0);
        program.setUniformf(U_BLUR_DIV, this.strength / (float) passes);
        program.setUniformf(U_OFFSET_X, originX);
        program.setUniformf(U_OFFSET_Y, originY);
        program.setUniformf(U_ZOOM, zoom);
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

    public float getOriginX() {
        return originX;
    }

    public float getOriginY() {
        return originY;
    }

    /**
     * Specify the zoom origin in {@link Align} bits.
     *
     * @see Align
     */
    public void setOrigin(int align) {
        final float originX;
        final float originY;
        if ((align & Align.left) != 0) {
            originX = 0f;
        } else if ((align & Align.right) != 0) {
            originX = 1f;
        } else {
            originX = 0.5f;
        }
        if ((align & Align.bottom) != 0) {
            originY = 0f;
        } else if ((align & Align.top) != 0) {
            originY = 1f;
        } else {
            originY = 0.5f;
        }
        setOrigin(originX, originY);
    }

    /**
     * Specify the zoom origin in normalized screen coordinates.
     *
     * @param originX horizontal origin [0..1].
     * @param originY vertical origin [0..1].
     */
    public void setOrigin(float originX, float originY) {
        this.originX = originX;
        this.originY = originY;
        program.begin();
        program.setUniformf(U_OFFSET_X, this.originX);
        program.setUniformf(U_OFFSET_Y, this.originY);
        program.end();
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
        setUniform(U_BLUR_DIV, strength / (float) passes);
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
        setUniform(U_ZOOM, this.zoom);
    }
}
