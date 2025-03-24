package com.davik.baseboard.postfx.util;

import static com.davik.baseboard.postfx.util.ConstKt.BASE_PATH;
import static com.davik.baseboard.postfx.util.ConstKt.U_TEXTURE0;
import static com.davik.baseboard.postfx.util.ConstKt.U_TEXTURE1;
import static com.davik.baseboard.postfx.util.ConstKt.COMMON_VERTEX_PATH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.davik.baseboard.postfx.core.VfxRenderContext;
import com.davik.baseboard.postfx.core.base.ShaderVfxEffect;
import com.davik.baseboard.postfx.core.framebuffer.VfxFrameBuffer;
import com.davik.baseboard.postfx.core.gl.VfxGLUtils;

/**
 * Simply mixes two frames with a factor of {@link #mixFactor}.
 * <p>
 * Depends on {@link Method} the result will be combined with either:
 * <br><code>max(src0, src1 * mixFactor)</code>
 * <br> or
 * <br><code>mix(src0, src1, mixFactor)</code>
 */
public class MixEffect extends ShaderVfxEffect {

    private static final String U_MIX = "u_mix";

    private float mixFactor = 0.5f;

    public MixEffect(Method method) {
        super(VfxGLUtils.compileShader(
                Gdx.files.classpath(COMMON_VERTEX_PATH),
                Gdx.files.classpath(BASE_PATH + "mix.frag"),
                "#define METHOD " + method.name()));
        rebind();
    }

    @Override
    public void rebind() {
        super.rebind();
        program.begin();
        program.setUniformi(U_TEXTURE0, TEXTURE_HANDLE0);
        program.setUniformi(U_TEXTURE1, TEXTURE_HANDLE1);
        program.setUniformf(U_MIX, mixFactor);
        program.end();
    }

    public void render(VfxRenderContext context, VfxFrameBuffer src0, VfxFrameBuffer src1, VfxFrameBuffer dst) {
        src0.getTexture().bind(TEXTURE_HANDLE0);
        src1.getTexture().bind(TEXTURE_HANDLE1);
        renderShader(context, dst);
    }

    public float getMixFactor() {
        return mixFactor;
    }

    public void setMixFactor(float mixFactor) {
        this.mixFactor = MathUtils.clamp(0f, 1f, mixFactor);
        setUniform(U_MIX, mixFactor);
    }

    /**
     * Defines which function will be used to combine mix the two frames.
     */
    public enum Method {
        MAX,
        MIX;
    }
}
