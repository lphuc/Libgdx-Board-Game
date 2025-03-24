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

public class RadialDistortionEffect extends ShaderVfxEffect implements ChainVfxEffect {

    private static final String U_DISTORTION = "distortion";
    private static final String U_ZOOM = "zoom";

    private float zoom = 1f;
    private float distortion = 0.3f;

    public RadialDistortionEffect() {
        super(VfxGLUtils.compileShader(
                Gdx.files.classpath(COMMON_VERTEX_PATH),
                Gdx.files.classpath(BASE_PATH + "radial-distortion.frag")));
        rebind();
    }

    @Override
    public void rebind() {
        super.rebind();
        program.begin();
        program.setUniformi(U_TEXTURE0, TEXTURE_HANDLE0);
        program.setUniformf(U_DISTORTION, distortion);
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

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
        setUniform(U_ZOOM, this.zoom);
    }

    public float getDistortion() {
        return distortion;
    }

    public void setDistortion(float distortion) {
        this.distortion = distortion;
        setUniform(U_DISTORTION, this.distortion);
    }
}
