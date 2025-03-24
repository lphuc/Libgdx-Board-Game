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

public class FilmGrainEffect extends ShaderVfxEffect implements ChainVfxEffect {
    private static final String U_SEED = "u_seed";
    private static final String U_NOISE_AMOUNT = "u_noiseAmount";

    private float seed = 0f;
    private float noiseAmount = 0.18f;

    public FilmGrainEffect() {
        super(VfxGLUtils.compileShader(
                Gdx.files.classpath(COMMON_VERTEX_PATH),
                Gdx.files.classpath(BASE_PATH + "film-grain.frag")));
        rebind();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        float newSeedValue = (this.seed + delta) % 1f;
        setSeed(newSeedValue);
    }

    @Override
    public void rebind() {
        super.rebind();
        program.begin();
        program.setUniformi(U_TEXTURE0, TEXTURE_HANDLE0);
        program.setUniformf(U_SEED, seed);
        program.setUniformf(U_NOISE_AMOUNT, noiseAmount);
        program.begin();
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

    public float getSeed() {
        return seed;
    }

    public void setSeed(float seed) {
        this.seed = seed;
        setUniform(U_SEED, seed);
    }

    public float getNoiseAmount() {
        return noiseAmount;
    }

    public void setNoiseAmount(float noiseAmount) {
        this.noiseAmount = noiseAmount;
        setUniform(U_NOISE_AMOUNT, noiseAmount);
    }
}
