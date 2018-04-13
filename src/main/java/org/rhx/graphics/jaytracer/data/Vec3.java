package org.rhx.graphics.jaytracer.data;

import java.util.Random;

import static java.lang.Math.sqrt;

/**
 * This object represents a value in R3. It could be a Cartesian vector or RGB color value.
 */
public class Vec3 {

    private static final Random rand = new Random(System.currentTimeMillis());

    private final float e0;
    private final float e1;
    private final float e2;

    private Vec3(final float e0, final float e1, final float e2) {
        this.e0 = e0;
        this.e1 = e1;
        this.e2 = e2;
    }

    public static class Color extends Vec3 {

        private Color(float e0, float e1, float e2) {
            super(e0, e1, e2);
        }
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

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", e0, e1, e2);
    }

    public static Vec3 of(final double e0, final double e1, final double e2) {
        return new Vec3((float)e0, (float)e1, (float)e2);
    }
    public static Vec3 of(final float e0, final float e1, final float e2) {
        return new Vec3(e0, e1, e2);
    }

    public static Vec3 add(final Vec3 v0, final Vec3 v1) {
        return new Vec3(v0.e0 + v1.e0, v0.e1 + v1.e1, v0.e2 + v1.e2);
    }

    public static Vec3 add(final Vec3 ... vs) {
        float e0 = 0.0f, e1 = 0.0f, e2 = 0.0f;
        for (Vec3 v : vs) {
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

    public static Vec3 mul(final Vec3 ... vs) {
        float e0 = 1f, e1 = 1f, e2 = 1f;
        for (Vec3 v : vs) {
            e0 *= v.e0;
            e1 *= v.e1;
            e2 *= v.e2;
        }
        return new Vec3(e0, e1, e2);
    }

    public static Vec3 div(final Vec3 v0, final Vec3 v1) {
        return new Vec3(v0.e0 / v1.e0, v0.e1 / v1.e1, v0.e2 / v1.e2);
    }

    public static Vec3 mul(final float t, final Vec3 v0) {
        return new Vec3(v0.e0 * t, v0.e1 * t, v0.e2 * t);
    }

    public static Vec3 mul(final float t, final Vec3 ... vs) {
        return mul(t, mul(vs));
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

    /**
     * Random vector in unit radius sphere.
     * @return {@link Vec3}
     */
    public static Vec3 randInUnitSph() {
        Vec3 p;
        do {
            p = Vec3.sub(Vec3.mul(2f, Vec3.of(rand.nextFloat(), rand.nextFloat(), rand.nextFloat())), Vec3.ONES);
        } while (Vec3.len_sq(p) >= 1f);
        return p;
    }

    public static Vec3 ZERO = Vec3.of(0.0f, 0.0f, 0.0f);

    public static Vec3 ONES = Vec3.of(1.0f, 1.0f, 1.0f);
}