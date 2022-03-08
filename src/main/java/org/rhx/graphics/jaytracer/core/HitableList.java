package org.rhx.graphics.jaytracer.core;

import org.rhx.graphics.jaytracer.bvh.AABB;
import org.rhx.graphics.jaytracer.material.Material;
import org.rhx.graphics.jaytracer.util.HitRecord;

import java.util.List;

/**
 * Primitive container for the
 */
public class HitableList implements Hitable {

    private final List<Hitable> hitables;

    private HitableList(List<Hitable> hitables) {
        this.hitables = hitables;
    }

    public static HitableList of(List<Hitable> hitables) {
        return new HitableList(hitables);
    }

    @Override
    public HitRecord hit(Ray ray, float tMin, float tMax) {
        HitRecord rec = null;
        float closestSoFar = tMax;
        for (Hitable hitable : hitables) {
            HitRecord tempRec = hitable.hit(ray, tMin, closestSoFar);
            if (tempRec != null) {
                closestSoFar = tempRec.t;
                rec = tempRec;
            }
        }
        return rec;
    }

    @Override
    public Material getMaterial() {
        throw new UnsupportedOperationException("Y U getting material?");
    }

    @Override
    public AABB boundingBox(float t0, float t1) {
        if (hitables.isEmpty()) return null;

        AABB aabb = hitables.get(0).boundingBox(t0, t1);
        Vec3 min = aabb.getMin();
        Vec3 max = aabb.getMax();

        Vec3 aabbMin, aabbMax;
        for (Hitable hitable : hitables) {
            aabb = hitable.boundingBox(t0, t1);
            if (aabb == null) continue;
            aabbMin = aabb.getMin();
            aabbMax = aabb.getMax();
            min = min.compareTo(aabbMin) < 0 ? min : aabbMin;
            max = max.compareTo(aabbMax) < 0 ? aabbMax : max;
        }

        return AABB.of(min, max);
    }

    public List<Hitable> getHitables() {
        return hitables;
    }

}
