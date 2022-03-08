package org.rhx.graphics.jaytracer;

import org.rhx.graphics.jaytracer.bvh.AABB;
import org.rhx.graphics.jaytracer.core.Hitable;
import org.rhx.graphics.jaytracer.core.Ray;
import org.rhx.graphics.jaytracer.core.Vec3;
import org.rhx.graphics.jaytracer.material.Material;
import org.rhx.graphics.jaytracer.util.HitRecord;

import static java.lang.Math.sqrt;
import static org.rhx.graphics.jaytracer.core.Vec3.*;

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
        float halfb = dot(oc, r.dir);
        float c = dot(oc, oc) - radius*radius;

        float delta = halfb * halfb - a * c;
        if (delta > 0) {
            float temp = (-halfb - (float) sqrt(delta)) / a;
            if (tMin < temp && temp < tMax) {
                Vec3 pap = Ray.pap(temp, r);
                return HitRecord.of(temp, pap, div(sub(pap, center), radius), material);
            }
            temp = (-halfb + (float) sqrt(delta)) / a;
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
    public AABB boundingBox(float t0, float t1) {
        return AABB.of(
                Vec3.sub(center, Vec3.of(radius, radius, radius)),
                Vec3.add(center, Vec3.of(radius, radius, radius)));
    }

}
