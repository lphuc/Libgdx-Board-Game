package com.davik.baseboard.postfx.core.base;

public abstract class AbstractVfxEffect implements VfxEffect {

    private boolean disabled;

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
