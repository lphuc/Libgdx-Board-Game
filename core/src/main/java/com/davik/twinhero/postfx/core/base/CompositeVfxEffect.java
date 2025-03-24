package com.davik.twinhero.postfx.core.base;

import com.badlogic.gdx.utils.Array;

/**
 * Base class for an effect that is a composition of some other {@link VfxEffect}s.
 * The class manages contained effects and delegates the lifecycle methods to the instances
 * (e.g. {@link VfxEffect#resize(int, int)}, {@link VfxEffect#rebind()}, {@link VfxEffect#update(float)}, {@link VfxEffect#dispose()}).
 * <p/>
 * To register an internal effect, call {@link #register(VfxEffect)}.
 */
public abstract class CompositeVfxEffect extends AbstractVfxEffect {

    protected final Array<VfxEffect> managedEffects = new Array<>();

    @Override
    public void resize(int width, int height) {
        for (int i = 0; i < managedEffects.size; i++) {
            managedEffects.get(i).resize(width, height);
        }
    }

    @Override
    public void rebind() {
        for (int i = 0; i < managedEffects.size; i++) {
            managedEffects.get(i).rebind();
        }
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < managedEffects.size; i++) {
            managedEffects.get(i).update(delta);
        }
    }

    @Override
    public void dispose() {
        for (int i = 0; i < managedEffects.size; i++) {
            managedEffects.get(i).dispose();
        }
    }

    protected <T extends VfxEffect> T register(T effect) {
        managedEffects.add(effect);
        return effect;
    }

    protected <T extends VfxEffect> T unregister(T effect) {
        managedEffects.removeValue(effect, true);
        return effect;
    }
}
