package org.rhx.graphics.jaytracer.data;

import static org.rhx.graphics.jaytracer.data.Vec3.add;
import static org.rhx.graphics.jaytracer.data.Vec3.mul;

/**
 * Ray instance object.
 */
public class Ray {

    public final Vec3 orig;
    public final Vec3 dir;

    private Ray(Vec3 a, Vec3 b) {
        orig = a;
        dir = b;
    }

    @Override
    public String toString() {
        return String.format("[%s -> %s}", orig, dir);
    }

    public static Ray of(Vec3 a, Vec3 b) {
        return new Ray(a, b);
    }

    public static Vec3 pap(final float t, final Ray r) {
        return add(r.orig, mul(t, r.dir));
    }
}
