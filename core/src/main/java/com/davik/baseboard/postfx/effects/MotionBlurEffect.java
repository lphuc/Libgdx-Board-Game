package com.davik.baseboard.postfx.effects;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.davik.baseboard.postfx.core.VfxRenderContext;
import com.davik.baseboard.postfx.core.base.ChainVfxEffect;
import com.davik.baseboard.postfx.core.base.CompositeVfxEffect;
import com.davik.baseboard.postfx.core.framebuffer.VfxFrameBuffer;
import com.davik.baseboard.postfx.core.framebuffer.VfxFrameBufferQueue;
import com.davik.baseboard.postfx.core.framebuffer.VfxPingPongWrapper;
import com.davik.baseboard.postfx.util.CopyEffect;
import com.davik.baseboard.postfx.util.MixEffect;

/**
 * A motion blur effect which draws the last frame with a lower opacity.
 * The result is then stored as the next last frame to create the trail effect.
 */
public class MotionBlurEffect extends CompositeVfxEffect implements ChainVfxEffect {

    private final MixEffect mixFilter;
    private final CopyEffect copyFilter;

    private final VfxFrameBufferQueue localBuffer;

    private boolean firstFrameRendered = false;

    public MotionBlurEffect(Pixmap.Format pixelFormat, MixEffect.Method mixMethod, float blurFactor) {
        mixFilter = register(new MixEffect(mixMethod));
        mixFilter.setMixFactor(blurFactor);

        copyFilter = register(new CopyEffect());

        localBuffer = new VfxFrameBufferQueue(pixelFormat,
                // On WebGL (GWT) we cannot render from/into the same texture simultaneously.
                // Will use ping-pong approach to avoid "writing into itself".
                Gdx.app.getType() == Application.ApplicationType.WebGL ? 2 : 1
        );
    }

    @Override
    public void dispose() {
        super.dispose();
        localBuffer.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        localBuffer.resize(width, height);
        firstFrameRendered = false;
    }

    @Override
    public void rebind() {
        super.rebind();
        localBuffer.rebind();
    }

    @Override
    public void render(VfxRenderContext context, VfxPingPongWrapper buffers) {
        VfxFrameBuffer prevFrame = this.localBuffer.changeToNext();
        if (!firstFrameRendered) {
            // Mix filter requires two frames to render, so we gonna skip the first call.
            copyFilter.render(context, buffers.getSrcBuffer(), prevFrame);
            buffers.swap();
            firstFrameRendered = true;
            return;
        }

        mixFilter.render(context, buffers.getSrcBuffer(), prevFrame, buffers.getDstBuffer());
        copyFilter.render(context, buffers.getDstBuffer(), prevFrame);
    }
}
