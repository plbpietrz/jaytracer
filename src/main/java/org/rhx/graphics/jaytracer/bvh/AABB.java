package org.rhx.graphics.jaytracer.bvh;

import org.rhx.graphics.jaytracer.core.Ray;
import org.rhx.graphics.jaytracer.core.Vec3;

import static org.rhx.graphics.jaytracer.util.FMath.fmax;
import static org.rhx.graphics.jaytracer.util.FMath.fmin;

public class AABB {

    private final Vec3 min;
    private final Vec3 max;

    private AABB(Vec3 min, Vec3 max) {
        this.min = min;
        this.max = max;
    }

    public static AABB of(Vec3 min, Vec3 max) {
        return new AABB(min, max);
    }

    public static AABB of(AABB b0, AABB b1) {
        if (b0 == null) return b1;
        if (b1 == null) return b0;

        Vec3 p0 = Vec3.of(
                fmin(b0.min.x(), b1.min.x()),
                fmin(b0.min.y(), b1.min.y()),
                fmin(b0.min.z(), b1.min.z()));
        Vec3 p1 = Vec3.of(
                fmax(b0.max.x(), b1.max.x()),
                fmax(b0.max.y(), b1.max.y()),
                fmax(b0.max.z(), b1.max.z()));
        return AABB.of(p0, p1);
    }

    public boolean hit(Ray ray, float tMin, float tMax) {
        for (int i = 0; i < 3; i++) {
            float invD = 1.f / ray.dir.get(i);
            float t0 = (min.get(i) - ray.orig.get(i)) * invD;
            float t1 = (max.get(i) - ray.orig.get(i)) * invD;
            if (invD < 0.f) {
                float t = t0;
                t0 = t1;
                t1 = t;
            }
            tMin = fmax(t0, tMin);
            tMax = fmin(t1, tMax);
            if (tMax <= tMin)
                return false;
        }
        return true;
    }

    public Vec3 getMin() {
        return min;
    }

    public Vec3 getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "AABB{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }

    public static void main(String[] args) {
        AABB ab1 = AABB.of(Vec3.ZERO, Vec3.ONES);
        AABB ab2 = AABB.of(Vec3.of(3f, 3f, 3f), Vec3.of(4f, 4f, 4f));
        AABB ab3 = AABB.of(ab1, ab2);

        System.out.println(ab1);
        System.out.println(ab2);
        System.out.println(ab3);
        Ray ra1 = Ray.of(Vec3.of(2f, 2f, 2f), Vec3.of(-1f, -1f, -1f));
        Ray ra2 = Ray.of(Vec3.of(2f, 2f, 3f), Vec3.of(3f, 3f, 1f));
        System.out.println(ra1);
        System.out.printf("ab1 hit: %b%n", ab1.hit(ra1, 0f, 1f));
        System.out.printf("ab2 hit: %b%n", ab2.hit(ra1, 0f, 1f));
        System.out.printf("ab3 hit: %b%n", ab3.hit(ra1, 0f, 1f));

        System.out.printf("ab1 hit: %b%n", ab1.hit(ra2, 0f, 1f));
        System.out.printf("ab2 hit: %b%n", ab2.hit(ra2, 0f, 1f));
        System.out.printf("ab3 hit: %b%n", ab3.hit(ra2, 0f, 1f));
    }
}
