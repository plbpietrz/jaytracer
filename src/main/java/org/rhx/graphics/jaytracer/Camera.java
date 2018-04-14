package org.rhx.graphics.jaytracer;

import org.rhx.graphics.jaytracer.data.Ray;
import org.rhx.graphics.jaytracer.data.Vec3;

public class Camera {

    private final Vec3 origin;
    private final Vec3 lowerLeftCorner;
    private final Vec3 horizontal;
    private final Vec3 vertical;
    private final Vec3 u, v, w;
    private final float lensRadius;

    private Camera(Vec3 lookFrom, Vec3 lookAt, Vec3 vup, float vfov, float aspect, float aperture, float focusDist) {
        lensRadius = aperture / 2f;
        double theta = vfov * Math.PI / 180;
        float halfHeight = (float) Math.tan(theta / 2d);
        float halfWidth = aspect * halfHeight;

        w = Vec3.unit(Vec3.sub(lookFrom, lookAt));
        u = Vec3.unit(Vec3.cross(vup, w));
        v = Vec3.cross(w, u);

        origin          = lookFrom;
        lowerLeftCorner = Vec3.sub(origin, Vec3.mul(halfWidth * focusDist, u), Vec3.mul(halfHeight * focusDist, v), Vec3.mul(focusDist, w));
        horizontal      = Vec3.mul(2f * halfWidth * focusDist, u);
        vertical        = Vec3.mul(2f * halfHeight * focusDist, v);
    }

    public Ray getRay(float s, float t) {
        Vec3 rd = Vec3.mul(lensRadius, Vec3.randInUnitSph());
        Vec3 offset = Vec3.add(Vec3.mul(rd.x(), u), Vec3.mul(rd.y(), v));
        return Ray.of(
                Vec3.add(origin, offset),
                Vec3.add(
                        lowerLeftCorner,
                        Vec3.mul(s, horizontal),
                        Vec3.mul(t, vertical),
                        Vec3.neg(origin),
                        Vec3.neg(offset)));
    }

    public static Camera of(Vec3 lookFrom, Vec3 lookAt, Vec3 vup, float vfov, float aspect, float aperture, float focusDist) {
        return new Camera(
                lookFrom, lookAt, vup,
                vfov, aspect,
                aperture, focusDist);
    }
}
