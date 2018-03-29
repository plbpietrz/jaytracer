package org.rhx.graphics.jaytracer;

import java.util.Collection;

public class HitableList implements Hitable {

    public final Collection<Hitable> hitables;

    private HitableList(Collection<Hitable> hitables) {
        this.hitables = hitables;
    }

    public static HitableList of(Collection<Hitable> hitables) {
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
}
