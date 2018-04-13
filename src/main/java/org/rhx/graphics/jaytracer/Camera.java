package org.rhx.graphics.jaytracer;

import org.rhx.graphics.jaytracer.data.Ray;
import org.rhx.graphics.jaytracer.data.Vec3;

public class Camera {

    public Vec3 origin;
    public Vec3 lowerLeftCorner;
    public Vec3 horizontal;
    public Vec3 vertical;

    public Camera() {
        lowerLeftCorner = Vec3.of(-2f, -1f, -1f);
        horizontal      = Vec3.of(4f, 0f, 0f);
        vertical        = Vec3.of(0f, 2f, 0f);
        origin          = Vec3.of(0f, 0f, 0f);
    }

    public Ray getRay(float u, float v) {
        return Ray.of(
                origin,
                Vec3.add(
                        lowerLeftCorner,
                        Vec3.mul(u, horizontal),
                        Vec3.mul(v, vertical),
                        Vec3.neg(origin)));
    }
}
