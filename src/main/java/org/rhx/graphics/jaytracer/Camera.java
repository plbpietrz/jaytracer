package org.rhx.graphics.jaytracer;

import org.rhx.graphics.jaytracer.data.Ray;
import org.rhx.graphics.jaytracer.data.Vec3;

public class Camera {

    public final Vec3 origin;
    public final Vec3 lowerLeftCorner;
    public final Vec3 horizontal;
    public final Vec3 vertical;

    public Camera(Vec3 lookFrom, Vec3 lookAt, Vec3 vup, float vfov, float aspect) {
        double theta = vfov * Math.PI / 180;
        float halfHeight = (float) Math.tan(theta / 2d);
        float halfWidth = aspect * halfHeight;
        Vec3 w = Vec3.unit(Vec3.sub(lookFrom, lookAt));
        Vec3 u = Vec3.unit(Vec3.cross(vup, w));
        Vec3 v = Vec3.cross(w, u);

        origin = lookFrom;
        lowerLeftCorner = Vec3.sub(origin, Vec3.mul(halfWidth, u), Vec3.mul(halfHeight, v), w);
        horizontal      = Vec3.mul(2f * halfWidth, u);
        vertical        = Vec3.mul(2f * halfHeight, v);
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
