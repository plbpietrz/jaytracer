package org.rhx.graphics.raytracer;

import static org.rhx.graphics.raytracer.Vec3.add;
import static org.rhx.graphics.raytracer.Vec3.mul;

/**
 * Ray instance objcet
 */
public class Ray {

    private final Vec3 A;
    private final Vec3 B;

    public Ray(Vec3 a, Vec3 b) {
        A = a;
        B = b;
    }

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
