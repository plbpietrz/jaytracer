package org.rhx.graphics.jaytracer;

import static org.rhx.graphics.jaytracer.Vec3.add;
import static org.rhx.graphics.jaytracer.Vec3.mul;

/**
 * Ray instance object.
 */
public class Ray {

    private final Vec3 A;
    private final Vec3 B;

    public Ray(Vec3 a, Vec3 b) {
        A = a;
        B = b;
    }

    /**
     * Return the origin of the ray.
     * @param r {@link Ray}
     * @return rays origin {@link Vec3}
     */
    public static Vec3 org(final Ray r) {
        return r.A;
    }

    public static Vec3 dir(final Ray r) {
        return r.B;
    }

    public static Vec3 pap(final float t, final Ray r) {
        return add(r.A, mul(t, r.B));
    }
}
