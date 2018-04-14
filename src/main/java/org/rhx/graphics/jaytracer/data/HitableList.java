package org.rhx.graphics.jaytracer.data;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Primitive container for the
 */
public class HitableList implements Hitable {

    private final Collection<Hitable> hitables;

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

    @Override
    public Material getMaterial() {
        return null;
    }

    @Override
    public String toString() {
        return String.format(
                "@(%s)",
                hitables.stream()
                        .collect(
                                Collectors.groupingBy(
                                        h -> h.getMaterial().getClass().getSimpleName(),
                                        Collectors.counting())));
    }
}
