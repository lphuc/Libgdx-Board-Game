package com.davik.baseboard.postfx.core.gl;

/**
 * Extra [platform specific] OpenGL functionality required for the library.
 * (methods not yet implemented/supported by the official LibGDX backends).
 */
public interface VfxGlExtension {
    int getBoundFboHandle();
}
