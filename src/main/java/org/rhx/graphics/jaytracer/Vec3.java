package org.rhx.graphics.jaytracer;

import static java.lang.Math.sqrt;

/**
 * This object represents a value in R3. It could be a Cartesian vector or RGB color value.
 */
public class Vec3 {

    private final float e0;
    private final float e1;
    private final float e2;

    public Vec3() {
        e0 = 0.0f;
        e1 = 0.0f;
        e2 = 0.0f;
    }

    public Vec3(final float e0, final float e1, final float e2) {
        this.e0 = e0;
        this.e1 = e1;
        this.e2 = e2;
    }

    public float x() {
        return e0;
    }

    public float y() {
        return e1;
    }

    public float z() {
        return e2;
    }

    public float r() {
        return e0;
    }

    public float g() {
        return e1;
    }

    public float b() {
        return e2;
    }

    public static Vec3 of(final float e0, final float e1, final float e2) {
        return new Vec3(e0, e1, e2);
    }

    public static Vec3 add(final Vec3 v0, final Vec3 v1) {
        return new Vec3(v0.e0 + v1.e0, v0.e1 + v1.e1, v0.e2 + v1.e2);
    }

    public static Vec3 add(final Vec3 ... vecs) {
        float e0 = 0.0f, e1 = 0.0f, e2 = 0.0f;
        for (Vec3 v : vecs) {
            e0 += v.e0;
            e1 += v.e1;
            e2 += v.e2;
        }
        return new Vec3(e0, e1, e2);
    }

    public static Vec3 sub(final Vec3 v0, final Vec3 v1) {
        return new Vec3(v0.e0 - v1.e0, v0.e1 - v1.e1, v0.e2 - v1.e2);
    }

    public static Vec3 mul(final Vec3 v0, final Vec3 v1) {
        return new Vec3(v0.e0 * v1.e0, v0.e1 * v1.e1, v0.e2 * v1.e2);
    }

    public static Vec3 div(final Vec3 v0, final Vec3 v1) {
        return new Vec3(v0.e0 / v1.e0, v0.e1 / v1.e1, v0.e2 / v1.e2);
    }

    public static Vec3 mul(final float t, final Vec3 v0) {
        return new Vec3(v0.e0 * t, v0.e1 * t, v0.e2 * t);
    }

    public static Vec3 div(final Vec3 v0, final float v1) {
        return new Vec3(v0.e0 / v1, v0.e1 / v1, v0.e2 / v1);
    }

    public static float len(final Vec3 v0) {
        return (float) sqrt(v0.e0 * v0.e0 + v0.e1 * v0.e1 + v0.e2 * v0.e2);
    }

    public static float len_sq(final Vec3 v0) {
        return v0.e0 * v0.e0 + v0.e1 * v0.e1 + v0.e2 * v0.e2;
    }

    public static Vec3 unit(final Vec3 v0) {
        return div(v0, len(v0));
    }

    public static float dot(final Vec3 v0, final Vec3 o) {
        return v0.e0*o.e0 + v0.e1*o.e1 + v0.e2*o.e2;
    }

    public static Vec3 cross(final Vec3 v0, final Vec3 v1) {
        throw new UnsupportedOperationException();
    }

    public static Vec3 neg(Vec3 v) {
        return Vec3.of(-v.e0, -v.e1, -v.e2);
    }

    public static Vec3 ZERO = Vec3.of(0.0f, 0.0f, 0.0f);
}
