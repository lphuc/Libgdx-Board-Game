package com.davik.twinhero.postfx.util;

import static com.davik.twinhero.postfx.util.ConstKt.BASE_PATH;
import static com.davik.twinhero.postfx.util.ConstKt.COMMON_VERTEX_PATH;

import com.badlogic.gdx.Gdx;
import com.davik.twinhero.postfx.core.VfxRenderContext;
import com.davik.twinhero.postfx.core.base.ChainVfxEffect;
import com.davik.twinhero.postfx.core.base.ShaderVfxEffect;
import com.davik.twinhero.postfx.core.framebuffer.VfxFrameBuffer;
import com.davik.twinhero.postfx.core.framebuffer.VfxPingPongWrapper;
import com.davik.twinhero.postfx.core.gl.VfxGLUtils;

public class CopyEffect extends ShaderVfxEffect implements ChainVfxEffect {

    private static final String U_TEXTURE0 = "u_texture0";

    public CopyEffect() {
        super(VfxGLUtils.compileShader(
                Gdx.files.classpath(COMMON_VERTEX_PATH),
                Gdx.files.classpath(BASE_PATH + "copy.frag")));
        rebind();
    }

    @Override
    public void rebind() {
        super.rebind();
        setUniform(U_TEXTURE0, TEXTURE_HANDLE0);
    }

    @Override
    public void render(VfxRenderContext context, VfxPingPongWrapper buffers) {
        render(context, buffers.getSrcBuffer(), buffers.getDstBuffer());
    }

    public void render(VfxRenderContext context, VfxFrameBuffer src, VfxFrameBuffer dst) {
        // Bind src buffer's texture as a primary one.
        src.getTexture().bind(TEXTURE_HANDLE0);
        // Apply shader effect.
        renderShader(context, dst);
    }
}
