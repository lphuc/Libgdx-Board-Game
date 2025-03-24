package com.davik.twinhero.postfx.core.base;


import com.davik.twinhero.postfx.core.VfxRenderContext;
import com.davik.twinhero.postfx.core.framebuffer.VfxPingPongWrapper;

/**
 * Any effect that is compatible with {@link VfxManager}'s render chain, should implement this interface.
 */
public interface ChainVfxEffect extends VfxEffect {
    void render(VfxRenderContext context, VfxPingPongWrapper buffers);
}
