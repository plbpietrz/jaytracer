package org.rhx.graphics.jaytracer.model;

import org.rhx.graphics.jaytracer.model.material.Material;
import org.rhx.graphics.jaytracer.model.util.HitRecord;

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

    /**
     * Get the {@link Material} definition for this hittable object
     * @return {@link Material}
     */
    Material getMaterial();

}

