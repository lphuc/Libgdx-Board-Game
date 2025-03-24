package com.davik.baseboard.postfx.core.base;


import com.davik.baseboard.postfx.core.VfxRenderContext;
import com.davik.baseboard.postfx.core.framebuffer.VfxPingPongWrapper;

/**
 * Any effect that is compatible with {@link VfxManager}'s render chain, should implement this interface.
 */
public interface ChainVfxEffect extends VfxEffect {
    void render(VfxRenderContext context, VfxPingPongWrapper buffers);
}
