package org.rhx.graphics.jaytracer.data;

/**
 * Basic geometry object type that can interact with {@link Ray}.
 */
public interface Hitable {

    HitRecord hit(Ray ray, float tMin, float tMax);

    Material getMaterial();

}

