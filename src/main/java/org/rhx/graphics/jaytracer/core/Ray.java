package org.rhx.graphics.jaytracer.core;

import static org.rhx.graphics.jaytracer.core.Vec3.add;
import static org.rhx.graphics.jaytracer.core.Vec3.mul;

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

    private Ray(Vec3 origin, Vec3 direction, float time) {
        orig = origin;
        dir = direction;
        this.time = time;
    }

    public static Ray of(Vec3 origin, Vec3 direction) {
        return new Ray(origin, direction, 0f);
    }

    public static Ray of(Vec3 origin, Vec3 direction, float time) {
        return new Ray(origin, direction, time);
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

    @Override
    public String toString() {
        return "Ray{" +
                "orig=" + orig +
                ", dir=" + dir +
                ", time=" + time +
                '}';
    }
}
