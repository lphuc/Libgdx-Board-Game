package com.davik.twinhero.helpers;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector;

import java.io.Serializable;

/**
 * @author Davik
 * Utility class for apply interpolation on Float value
 */
public class Vector1 implements Serializable, Vector<Vector1> {
    public Float value = 0f;

    public Vector1() {
    }

    public Vector1(float value) {
        this.value = value;
    }

    @Override
    public Vector1 cpy() {
        return null;
    }

    @Override
    public float len() {
        return 0;
    }

    @Override
    public float len2() {
        return 0;
    }

    @Override
    public Vector1 limit(float limit) {
        return null;
    }

    @Override
    public Vector1 limit2(float limit2) {
        return null;
    }

    @Override
    public Vector1 setLength(float len) {
        return null;
    }

    @Override
    public Vector1 setLength2(float len2) {
        return null;
    }

    @Override
    public Vector1 clamp(float min, float max) {
        return null;
    }

    @Override
    public Vector1 set(Vector1 v) {
        return null;
    }

    @Override
    public Vector1 sub(Vector1 v) {
        return null;
    }

    @Override
    public Vector1 nor() {
        return null;
    }

    @Override
    public Vector1 add(Vector1 v) {
        return null;
    }

    @Override
    public float dot(Vector1 v) {
        return 0;
    }

    @Override
    public Vector1 scl(float scalar) {
        return null;
    }

    @Override
    public Vector1 scl(Vector1 v) {
        return null;
    }

    @Override
    public float dst(Vector1 v) {
        return 0;
    }

    @Override
    public float dst2(Vector1 v) {
        return 0;
    }

    @Override
    public Vector1 lerp(Vector1 target, float alpha) {
        final float invAlpha = 1.0f - alpha;
        this.value = (value * invAlpha) + (target.value * alpha);
        return this;
    }

    public Vector1 lerp(float target, float alpha) {
        final float invAlpha = 1.0f - alpha;
        this.value = (value * invAlpha) + (target * alpha);
        return this;
    }

    @Override
    public Vector1 interpolate(Vector1 target, float alpha, Interpolation interpolator) {
        return lerp(target, interpolator.apply(alpha));
    }

    @Override
    public Vector1 setToRandomDirection() {
        return null;
    }

    @Override
    public boolean isUnit() {
        return false;
    }

    @Override
    public boolean isUnit(float margin) {
        return false;
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean isZero(float margin) {
        return false;
    }

    @Override
    public boolean isOnLine(Vector1 other, float epsilon) {
        return false;
    }

    @Override
    public boolean isOnLine(Vector1 other) {
        return false;
    }

    @Override
    public boolean isCollinear(Vector1 other, float epsilon) {
        return false;
    }

    @Override
    public boolean isCollinear(Vector1 other) {
        return false;
    }

    @Override
    public boolean isCollinearOpposite(Vector1 other, float epsilon) {
        return false;
    }

    @Override
    public boolean isCollinearOpposite(Vector1 other) {
        return false;
    }

    @Override
    public boolean isPerpendicular(Vector1 other) {
        return false;
    }

    @Override
    public boolean isPerpendicular(Vector1 other, float epsilon) {
        return false;
    }

    @Override
    public boolean hasSameDirection(Vector1 other) {
        return false;
    }

    @Override
    public boolean hasOppositeDirection(Vector1 other) {
        return false;
    }

    @Override
    public boolean epsilonEquals(Vector1 other, float epsilon) {
        return false;
    }

    @Override
    public Vector1 mulAdd(Vector1 v, float scalar) {
        return null;
    }

    @Override
    public Vector1 mulAdd(Vector1 v, Vector1 mulVec) {
        return null;
    }

    @Override
    public Vector1 setZero() {
        return null;
    }
}
