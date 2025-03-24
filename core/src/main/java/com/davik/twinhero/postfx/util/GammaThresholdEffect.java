package com.davik.twinhero.postfx.util;

import static com.davik.twinhero.postfx.util.ConstKt.BASE_PATH;
import static com.davik.twinhero.postfx.util.ConstKt.U_TEXTURE0;
import static com.davik.twinhero.postfx.util.ConstKt.COMMON_VERTEX_PATH;

import com.badlogic.gdx.Gdx;
import com.davik.twinhero.postfx.core.VfxRenderContext;
import com.davik.twinhero.postfx.core.base.ChainVfxEffect;
import com.davik.twinhero.postfx.core.base.ShaderVfxEffect;
import com.davik.twinhero.postfx.core.framebuffer.VfxFrameBuffer;
import com.davik.twinhero.postfx.core.framebuffer.VfxPingPongWrapper;
import com.davik.twinhero.postfx.core.gl.VfxGLUtils;

/**
 * Keeps only values brighter than the specified gamma.
 */
public class GammaThresholdEffect extends ShaderVfxEffect implements ChainVfxEffect {

    private static final String U_THRESHOLD = "u_threshold";
    private static final String U_THRESHOLD_INV = "u_thresholdInv";

    private float gamma;

    public GammaThresholdEffect(Type type) {
        super(VfxGLUtils.compileShader(
                Gdx.files.classpath(COMMON_VERTEX_PATH),
                Gdx.files.classpath(BASE_PATH + "gamma-threshold.frag"),
                "#define THRESHOLD_TYPE " + type.name()));
        rebind();
    }

    @Override
    public void rebind() {
        super.rebind();
        program.begin();
        program.setUniformi(U_TEXTURE0, TEXTURE_HANDLE0);
        program.setUniformf(U_THRESHOLD, gamma);
        program.setUniformf(U_THRESHOLD_INV, 1f / (1f - gamma));
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

    public void setGamma(float gamma) {
        this.gamma = gamma;
        setUniform(U_THRESHOLD, gamma);
        setUniform(U_THRESHOLD_INV, 1f / (1f - gamma));
    }

    public float getGamma() {
        return gamma;
    }

    public enum Type {
        RGBA,
        RGB,
        ALPHA_PREMULTIPLIED,
    }
}
