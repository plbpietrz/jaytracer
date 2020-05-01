package org.rhx.graphics.jaytracer.model;

import static org.rhx.graphics.jaytracer.model.Vec3.add;
import static org.rhx.graphics.jaytracer.model.Vec3.mul;

/**
 * Ray object represents probing entity that is shot from the camera
 */
public class Ray {

    /**
     * Origin, start point.
     */
    public final Vec3 orig;

    /**
     * Ray direction from the origin point.
     */
    public final Vec3 dir;

    /**
     * Time
     */
    public final float time;

    private Ray(Vec3 a, Vec3 b, float t) {
        orig = a;
        dir = b;
        time = t;
    }

    @Override
    public String toString() {
        return String.format("[%s -> %s}", orig, dir);
    }

    public static Ray of(Vec3 a, Vec3 b) {
        return new Ray(a, b, 0f);
    }

    public static Ray of(Vec3 a, Vec3 b, float t) {
        return new Ray(a, b, t);
    }

    /**
     * Point at parameter t (0,1) along the ray direction.
     * @param t parameter along the ray
     * @param r ray
     * @return {@link Vec3}
     */
    public static Vec3 pap(float t, Ray r) {
        return add(r.orig, mul(t, r.dir));
    }
}
