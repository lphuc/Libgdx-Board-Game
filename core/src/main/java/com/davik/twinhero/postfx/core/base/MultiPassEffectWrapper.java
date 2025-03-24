package com.davik.twinhero.postfx.core.base;


import com.davik.twinhero.postfx.core.VfxRenderContext;
import com.davik.twinhero.postfx.core.framebuffer.VfxPingPongWrapper;

public class MultiPassEffectWrapper extends AbstractVfxEffect implements ChainVfxEffect {

    private final ChainVfxEffect effect;
    private int passes = 1;

    public MultiPassEffectWrapper(ChainVfxEffect effect) {
        this.effect = effect;
    }

    @Override
    public void resize(int width, int height) {
        effect.resize(width, height);
    }

    @Override
    public void update(float delta) {
        effect.update(delta);
    }

    @Override
    public void rebind() {
        effect.rebind();
    }

    @Override
    public void dispose() {
        effect.dispose();
    }

    @Override
    public void render(VfxRenderContext context, VfxPingPongWrapper buffers) {
        // Simply swap buffers to simulate render skip.
        if (passes == 0) {
            buffers.swap();
            return;
        }

        final int finalPasses = this.passes;
        for (int i = 0; i < finalPasses; i++) {
            effect.render(context, buffers);
            if (i < finalPasses - 1) {
                buffers.swap();
            }
        }
    }

    public int getPasses() {
        return passes;
    }

    public void setPasses(int passes) {
        if (passes < 0) {
            throw new IllegalArgumentException("Passes value cannot be a negative number.");
        }
        this.passes = passes;
    }
}
