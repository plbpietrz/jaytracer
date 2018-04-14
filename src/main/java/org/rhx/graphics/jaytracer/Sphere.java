package org.rhx.graphics.jaytracer;

import org.rhx.graphics.jaytracer.data.*;

import static java.lang.Math.sqrt;
import static org.rhx.graphics.jaytracer.data.Vec3.*;

/**
 * Renderable sphere.
 */
public class Sphere implements Hitable {

    final Vec3 center;
    final float radius;
    final Material material;


    private Sphere(Vec3 center, float radius, final Material material) {
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    public static Sphere of(Vec3 center, float radius, final Material material) {
        return new Sphere(center, radius, material);
    }

    @Override
    public HitRecord hit(Ray r, float tMin, float tMax) {
        Vec3 oc = sub(r.orig, center);
        float a = dot(r.dir, r.dir);
        float b = dot(oc, r.dir);
        float c = dot(oc, oc) - radius*radius;

        float discriminant = b*b - a*c;
        if (discriminant > 0) {
            float temp = (-b - (float) sqrt(discriminant)) / a;
            if (tMin < temp && temp < tMax) {
                Vec3 pap = Ray.pap(temp, r);
                return HitRecord.of(temp, pap, div(sub(pap, center), radius), material);
            }
            temp = (-b + (float) sqrt(discriminant)) / a;
            if (tMin < temp && temp < tMax) {
                Vec3 pap = Ray.pap(temp, r);
                return HitRecord.of(temp, pap, div(sub(pap, center), radius), material);
            }
        }
        return null;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public String toString() {
        return String.format("O(%s, %.2f, %s)", center, radius, material);
    }
}
