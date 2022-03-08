package org.rhx.graphics.jaytracer.material;

import org.rhx.graphics.jaytracer.core.Hitable;
import org.rhx.graphics.jaytracer.util.Ref;
import org.rhx.graphics.jaytracer.core.Ray;
import org.rhx.graphics.jaytracer.core.Vec3;
import org.rhx.graphics.jaytracer.util.HitRecord;

/**
 * Light interacting material definition.
 */
public interface Material {

    /**
     * Calculate the ray scattered by the interaction with this material.
     * @param rayIn {@link Ray} interacting with the material
     * @param rec {@link HitRecord} {@link Ray} <-> {@link Hitable} collision registration
     * @param attenuation out parameter
     * @param scattered out parameter
     * @return indication if scattering has occurred
     */
    boolean scatter(Ray rayIn, HitRecord rec, Ref<Vec3> attenuation, Ref<Ray> scattered);

}
