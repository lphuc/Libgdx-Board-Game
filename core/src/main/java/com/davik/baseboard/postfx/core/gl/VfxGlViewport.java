
package com.davik.baseboard.postfx.core.gl;

public class VfxGlViewport {
    public int x, y, width, height;

    public VfxGlViewport set(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    public VfxGlViewport set(VfxGlViewport viewport) {
        this.x = viewport.x;
        this.y = viewport.y;
        this.width = viewport.width;
        this.height = viewport.height;
        return this;
    }

    @Override
    public String toString() {
        return "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height;
    }
}
