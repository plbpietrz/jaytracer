package org.rhx.graphics.jaytracer.core;

import org.rhx.graphics.jaytracer.bvh.AABB;
import org.rhx.graphics.jaytracer.material.Material;
import org.rhx.graphics.jaytracer.util.HitRecord;

import java.util.Comparator;

/**
 * Basic geometry object type that can interact with {@link Ray}.
 */
public interface Hitable {

    /**
     * Check if the given {@link Ray} interacts with this object within the given (tMin, tMax) range.
     * @param ray {@link Ray} interacting with object
     * @param tMin minimum distance at we consider the interaction
     * @param tMax maximum distance at we consider the interaction
     * @return {@link HitRecord} if a hit has occurred or null if it did not
     */
    HitRecord hit(Ray ray, float tMin, float tMax);

    AABB boundingBox(float t0, float t1);

    /**
     * Get the {@link Material} definition for this hittable object
     * @return {@link Material}
     */
    Material getMaterial();

    Comparator<Hitable> BBOX_X_CMP = Comparator.comparing(h -> h.boundingBox(0, 0).getMin().x());
    Comparator<Hitable> BBOX_Y_CMP = Comparator.comparing(h -> h.boundingBox(0, 0).getMin().y());
    Comparator<Hitable> BBOX_Z_CMP = Comparator.comparing(h -> h.boundingBox(0, 0).getMin().z());

}

