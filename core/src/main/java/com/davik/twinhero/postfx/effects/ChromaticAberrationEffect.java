package com.davik.twinhero.postfx.effects;

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

public class ChromaticAberrationEffect extends ShaderVfxEffect implements ChainVfxEffect {
    private static final String U_MAX_DISTORTION = "u_maxDistortion";

    private float maxDistortion = 1.2f;

    public ChromaticAberrationEffect(int passes) {
        super(VfxGLUtils.compileShader(
                Gdx.files.classpath(COMMON_VERTEX_PATH),
                Gdx.files.classpath(BASE_PATH + "chromatic-aberration.frag"),
                "#define PASSES " + passes));
        rebind();
    }

    @Override
    public void rebind() {
        super.rebind();
        program.begin();
        program.setUniformi(U_TEXTURE0, TEXTURE_HANDLE0);
        program.setUniformf(U_MAX_DISTORTION, maxDistortion);
        program.end();
    }

    public float getMaxDistortion() {
        return maxDistortion;
    }

    public void setMaxDistortion(float maxDistortion) {
        this.maxDistortion = maxDistortion;
        setUniform(U_MAX_DISTORTION, maxDistortion);
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
}
