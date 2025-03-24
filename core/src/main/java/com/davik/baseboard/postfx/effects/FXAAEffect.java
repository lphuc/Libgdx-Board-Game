
package com.davik.baseboard.postfx.effects;

import static com.davik.baseboard.postfx.util.ConstKt.BASE_PATH;
import static com.davik.baseboard.postfx.util.ConstKt.U_TEXTURE0;
import static com.davik.baseboard.postfx.util.ConstKt.COMMON_VERTEX_PATH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.davik.baseboard.postfx.core.VfxRenderContext;
import com.davik.baseboard.postfx.core.base.ChainVfxEffect;
import com.davik.baseboard.postfx.core.base.ShaderVfxEffect;
import com.davik.baseboard.postfx.core.framebuffer.VfxFrameBuffer;
import com.davik.baseboard.postfx.core.framebuffer.VfxPingPongWrapper;
import com.davik.baseboard.postfx.core.gl.VfxGLUtils;

public class FXAAEffect extends ShaderVfxEffect implements ChainVfxEffect {
    private static final String U_VIEWPORT_INVERSE = "u_viewportInverse";
    private static final String U_FXAA_REDUCE_MIN = "u_fxaaReduceMin";
    private static final String U_FXAA_REDUCE_MUL = "u_fxaaReduceMul";
    private static final String U_FXAA_SPAN_MAX = "u_fxaaSpanMax";

    private final Vector2 viewportInverse = new Vector2();
    private float fxaaReduceMin;
    private float fxaaReduceMul;
    private float fxaaSpanMax;

    public FXAAEffect() {
        this(1f / 128f, 1f / 8f, 8f, true);
    }

    public FXAAEffect(float fxaaReduceMin, float fxaaReduceMul, float fxaaSpanMax, boolean supportAlpha) {
        super(VfxGLUtils.compileShader(
                Gdx.files.classpath(COMMON_VERTEX_PATH),
                Gdx.files.classpath(BASE_PATH + "fxaa.frag"),
                supportAlpha ? "#define SUPPORT_ALPHA" : ""));
        this.fxaaReduceMin = fxaaReduceMin;
        this.fxaaReduceMul = fxaaReduceMul;
        this.fxaaSpanMax = fxaaSpanMax;
        rebind();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.viewportInverse.set(1f / width, 1f / height);
        setUniform(U_VIEWPORT_INVERSE, this.viewportInverse);
    }

    @Override
    public void rebind() {
        super.rebind();
        program.begin();
        program.setUniformi(U_TEXTURE0, TEXTURE_HANDLE0);
        program.setUniformf(U_VIEWPORT_INVERSE, viewportInverse);
        program.setUniformf(U_FXAA_REDUCE_MIN, fxaaReduceMin);
        program.setUniformf(U_FXAA_REDUCE_MUL, fxaaReduceMul);
        program.setUniformf(U_FXAA_SPAN_MAX, fxaaSpanMax);
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

    /**
     * Sets the parameter. The default value is 1/128.
     *
     * @param value
     */
    public void setReduceMin(float value) {
        this.fxaaReduceMin = value;
        setUniform(U_FXAA_REDUCE_MIN, this.fxaaReduceMin);
    }

    /**
     * Sets the parameter. The default value is 1/8.
     *
     * @param value
     */
    public void setReduceMul(float value) {
        this.fxaaReduceMul = value;
        setUniform(U_FXAA_REDUCE_MUL, this.fxaaReduceMul);
    }

    /**
     * Sets the parameter. The default value is 8;
     *
     * @param value
     */
    public void setSpanMax(float value) {
        this.fxaaSpanMax = value;
        setUniform(U_FXAA_SPAN_MAX, this.fxaaSpanMax);
    }
}
