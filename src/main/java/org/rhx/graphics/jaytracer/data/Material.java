package org.rhx.graphics.jaytracer.data;

import org.rhx.graphics.jaytracer.Ref;

/**
 * Material definition.
 */
public interface Material {

    /**
     * Calculate the ray scattered by the interaction with this material.
     * @param rayIn {@link Ray} interacting with the material
     * @param rec {@linke HitRecord} {@link Ray} <-> {@link Hitable} collision registration
     * @param attenuation out parameter
     * @param scattered out parameter
     * @return indication if scattering has occured
     */
    boolean scatter(Ray rayIn, HitRecord rec, Ref<Vec3> attenuation, Ref<Ray> scattered);

}
