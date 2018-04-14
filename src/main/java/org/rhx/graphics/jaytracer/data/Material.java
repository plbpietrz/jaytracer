package org.rhx.graphics.jaytracer.data;

import org.rhx.graphics.jaytracer.Out;

/**
 * Material definition.
 */
public interface Material {

    boolean scatter(Ray rayIn, HitRecord rec, Out<Vec3> attenuation, Out<Ray> scattered);

}
