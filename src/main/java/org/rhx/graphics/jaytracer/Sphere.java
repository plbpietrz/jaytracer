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
    public boolean hit(Ray r, float tMin, float tMax, OutRef<HitRecord> rec) {
        Vec3 oc = sub(Ray.org(r), center);
        float a = dot(dir(r), dir(r));
        float b = dot(oc, dir(r));
        float c = dot(oc, oc);

        float discriminant = b * b - a * c;
        if (discriminant > 0) {
            float temp = (-b - (float) sqrt(discriminant)) / a;
            if (tMin < temp && temp < tMax) {
//                rec.get().t = temp;
//                rec.get().p = pap(temp, r);
//                rec.get().normal = div(sub(rec.get().p, center), radius);
                rec.set(HitRecord.of(temp, pap(temp, r), div(sub(rec.get().p, center), radius)));
                return true;
            }
            temp = (-b + (float) sqrt(discriminant)) / a;
            if (tMin < temp && temp < tMax) {
//                rec.get().t = temp;
//                rec.get().p = pap(temp, r);
//                rec.get().normal = div(sub(rec.get().p, center), radius);
                rec.set(HitRecord.of(temp, pap(temp, r), div(sub(rec.get().p, center), radius)));
                return true;
            }
        }
        return false;
    }

}
