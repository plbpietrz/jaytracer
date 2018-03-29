package org.rhx.graphics.jaytracer;

import static java.lang.Math.sqrt;
import static org.rhx.graphics.jaytracer.Ray.dir;
import static org.rhx.graphics.jaytracer.Ray.pap;
import static org.rhx.graphics.jaytracer.Vec3.*;

/**
 * Renderable sphere.
 */
public class Sphere implements Hitable {

    public final Vec3 center;
    public final float radius;


    private Sphere(Vec3 center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public static Sphere of(Vec3 center, float radius) {
        return new Sphere(center, radius);
    }

    @Override
    public HitRecord hit(Ray r, float tMin, float tMax) {
        Vec3 oc = sub(Ray.org(r), center);
        float a = dot(dir(r), dir(r));
        float b = dot(oc, dir(r));
        float c = dot(oc, oc) - radius*radius;

        float discriminant = b * b - a * c;
        if (discriminant > 0) {
            float temp = (-b - (float) sqrt(discriminant)) / a;
            Vec3 pap = pap(temp, r);
            if (tMin < temp && temp < tMax) {
                return HitRecord.of(temp, pap, div(sub(pap, center), radius));
            }
            temp = (-b + (float) sqrt(discriminant)) / a;
            if (tMin < temp && temp < tMax) {
                return HitRecord.of(temp, pap, div(sub(pap, center), radius));
            }
        }
        return null;
    }

}
