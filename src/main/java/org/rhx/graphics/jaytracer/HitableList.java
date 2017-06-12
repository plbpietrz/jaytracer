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
    public boolean hit(Ray ray, float tMin, float tMax, OutRef<HitRecord> rec) {
        OutRef<HitRecord> tempRec = OutRef.empty();
        boolean hitAnything = false;
        float closestSoFar = tMax;
        for (Hitable hitable : hitables) {
            if (hitable.hit(ray, tMin, closestSoFar, tempRec)) {
                hitAnything = true;
                closestSoFar = tempRec.get().t;
                rec.set(tempRec.get());
            }
        }
        return hitAnything;
    }
}
