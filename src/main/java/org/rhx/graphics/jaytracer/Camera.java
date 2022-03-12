package org.rhx.graphics.jaytracer;

import org.rhx.graphics.jaytracer.core.Ray;
import org.rhx.graphics.jaytracer.core.Vec3;
import org.rhx.graphics.jaytracer.util.SimpleRNG;

/**
 * Camera object with adjustable position, filed of view and focus.
 */
public class Camera {

    private final Vec3 origin;
    private final Vec3 lowerLeftCorner;
    private final Vec3 horizontal;
    private final Vec3 vertical;
    private final Vec3 u, v;
    private final float lensRadius;
    private final float t0, t1;

    private static final SimpleRNG rand = SimpleRNG.get();

    private Camera(Vec3 lookFrom, Vec3 lookAt, Vec3 vup, float vFov, float aspect, float aperture, float focusDist, float startTime, float endTime) {
        double theta = vFov * Math.PI / 180;
        float halfHeight = (float) Math.tan(theta / 2d);
        float halfWidth = aspect * halfHeight;

        Vec3 w = Vec3.unit(Vec3.sub(lookFrom, lookAt));
        u = Vec3.unit(Vec3.cross(vup, w));
        v = Vec3.cross(w, u);

        origin          = lookFrom;
        lowerLeftCorner = Vec3.sub(
                origin,
                Vec3.mul(halfWidth * focusDist, u),
                Vec3.mul(halfHeight * focusDist, v),
                Vec3.mul(focusDist, w)
        );
        horizontal      = Vec3.mul(2f * halfWidth * focusDist, u);
        vertical        = Vec3.mul(2f * halfHeight * focusDist, v);
        lensRadius      = aperture / 2f;
        t0 = startTime;
        t1 = endTime;
    }

    /**
     * Get the ray at the given camera far plane coordinates.
     * @param s coordinate 1
     * @param t coordinate 1
     * @return ray perpendicular to the camera plane at coordinates (s, t)
     */
    public Ray getRay(float s, float t) {
        Vec3 rd = Vec3.mul(lensRadius, Vec3.rvius());
        Vec3 offset = Vec3.add(Vec3.mul(rd.x(), u), Vec3.mul(rd.y(), v));
        float time = t0 + rand.nextFloat()*(t1 - t0);
        return Ray.of(
                Vec3.add(origin, offset),
                Vec3.add(
                        lowerLeftCorner,
                        Vec3.mul(s, horizontal),
                        Vec3.mul(t, vertical),
                        Vec3.neg(origin),
                        Vec3.neg(offset)),
                time);
    }

    /**
     * Factory method for {@link Camera} object.
     * @param lookFrom camera origin position
     * @param lookAt camera target point
     * @param vup vertical up vector
     * @param vFov vertical field of view
     * @param aspect aspect ratio
     * @param aperture camera aperture (0 - absolute focus, > 0 selective focus)
     * @param focusDist focus distance
     * @param startTime ray generation time interval start
     * @param endTime ray generation time interval end
     * @return {@link Camera} instance
     */
    public static Camera of(Vec3 lookFrom, Vec3 lookAt, Vec3 vup, float vFov, float aspect, float aperture, float focusDist, float startTime, float endTime) {
        return new Camera(
                lookFrom, lookAt, vup,
                vFov, aspect,
                aperture, focusDist,
                startTime, endTime);
    }
}
