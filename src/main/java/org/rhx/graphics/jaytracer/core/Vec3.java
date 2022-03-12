package org.rhx.graphics.jaytracer.core;

import org.rhx.graphics.jaytracer.util.SimpleRNG;

import static java.lang.Math.sqrt;

/**
 * This object represents a value in R3. It could be a cartesian vector or RGB color value.
 */
public class Vec3 implements Comparable<Vec3> {

    private static final SimpleRNG rand = SimpleRNG.get();

    public static final Vec3 ZERO = new Vec3(0.0f, 0.0f, 0.0f);

    public static final Vec3 ONES = new Vec3(1.0f, 1.0f, 1.0f);

    private final float e0;
    private final float e1;
    private final float e2;

    private Vec3(final float e0, final float e1, final float e2) {
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

    public float get(int i) {
        return switch(i) {
            case 0 -> e0;
            case 1 -> e1;
            case 2 -> e2;
            default -> throw new IllegalArgumentException("Out of bounds: " + i);
        };
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

    public static Vec3 add(final Vec3 v0, final Vec3 v1, final Vec3 v2) {
        return new Vec3(v0.e0 + v1.e0 + v2.e0, v0.e1 + v1.e1 + v2.e1, v0.e2 + v1.e2 + v2.e2);
    }

    public static Vec3 add(final Vec3 v0, final Vec3 v1, final Vec3 v2, final Vec3 v3) {
        return new Vec3(
                v0.e0 + v1.e0 + v2.e0 + v3.e0,
                v0.e1 + v1.e1 + v2.e1 + v3.e1,
                v0.e2 + v1.e2 + v2.e2 + v3.e2
        );
    }

    public static Vec3 add(final Vec3 v0, final Vec3 v1, final Vec3 v2, final Vec3 v3, final Vec3 v4) {
        return new Vec3(
                v0.e0 + v1.e0 + v2.e0 + v3.e0 + v4.e0,
                v0.e1 + v1.e1 + v2.e1 + v3.e1 + v4.e1,
                v0.e2 + v1.e2 + v2.e2 + v3.e2 + v4.e2
        );
    }

    public static Vec3 sub(final Vec3 v0, final Vec3 v1) {
        return new Vec3(v0.e0 - v1.e0, v0.e1 - v1.e1, v0.e2 - v1.e2);
    }

    public static Vec3 sub(final Vec3 v0, final Vec3 v1, final Vec3 v2) {
        return new Vec3(v0.e0 - v1.e0 - v2.e0, v0.e1 - v1.e1 - v2.e1, v0.e2 - v1.e2 - v2.e2);
    }

    public static Vec3 sub(final Vec3 v0, final Vec3 v1, final Vec3 v2, final Vec3 v3) {
        return new Vec3(
                v0.e0 - v1.e0 - v2.e0 - v3.e0,
                v0.e1 - v1.e1 - v2.e1 - v3.e1,
                v0.e2 - v1.e2 - v2.e2 - v3.e2
        );
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

    public static Vec3 div(final Vec3 v0, final float t) {
        float oneOverV = 1 / t;
        return new Vec3(v0.e0 * oneOverV, v0.e1 * oneOverV, v0.e2 * oneOverV);
    }

    public static float len(final Vec3 v0) {
        return (float) sqrt(v0.e0 * v0.e0 + v0.e1 * v0.e1 + v0.e2 * v0.e2);
    }

    public static Vec3 unit(final Vec3 v0) {
        return div(v0, len(v0));
    }

    public static float dot(final Vec3 v0, final Vec3 v1) {
        return v0.e0 * v1.e0 + v0.e1 * v1.e1 + v0.e2 * v1.e2;
    }

    public static Vec3 cross(final Vec3 v0, final Vec3 v1) {
        return new Vec3(
                v0.e1 * v1.e2 - v0.e2 * v1.e1,
                v0.e2 * v1.e0 - v0.e0 * v1.e2,
                v0.e0 * v1.e1 - v0.e1 * v1.e0
        );
    }

    public static Vec3 neg(final Vec3 v0) {
        return new Vec3(-v0.e0, -v0.e1, -v0.e2);
    }

    /**
     * Random vector in unit radius sphere. Generate sphere of radius 2 and shift it by -ONES vector.
     * @return {@link Vec3}
     */
    public static Vec3 rvius() {
        Vec3 p;
        do {
            p = new Vec3(2f * rand.nextFloat() - 1f, 2f * rand.nextFloat() - 1f, 2f * rand.nextFloat() - 1f);
        } while (Vec3.dot(p, p) >= 1f);
        return p;
    }

    @Override
    public String toString() {
        return "Vec3{" +
                "e0=" + e0 +
                ", e1=" + e1 +
                ", e2=" + e2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vec3 vec3 = (Vec3) o;

        if (Float.compare(vec3.e0, e0) != 0) return false;
        if (Float.compare(vec3.e1, e1) != 0) return false;
        return Float.compare(vec3.e2, e2) == 0;
    }

    @Override
    public int hashCode() {
        int result = (e0 != 0.0f ? Float.floatToIntBits(e0) : 0);
        result = 31 * result + (e1 != 0.0f ? Float.floatToIntBits(e1) : 0);
        result = 31 * result + (e2 != 0.0f ? Float.floatToIntBits(e2) : 0);
        return result;
    }

    @Override
    public int compareTo(Vec3 o) {
        return Float.compare(len(this), len(o));
    }
}
