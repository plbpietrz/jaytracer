package org.rhx.graphics.jaytracer.util;

import org.rhx.graphics.jaytracer.core.Vec3;
import org.rhx.graphics.jaytracer.material.Material;

public class HitRecord {

    /**
     * Distance from the screen
     */
    public final float t;

    /**
     * Point of impact
     */
    public final Vec3 pnt;

    /**
     * Surface normal vector at the impact point pnt and distance t
     */
    public final Vec3 norm;

    /**
     * Material definition at the impact point
     */
    public final Material mat;

    private HitRecord(float t, Vec3 pnt, Vec3 norm, Material mat) {
        this.t = t;
        this.pnt = pnt;
        this.norm = norm;
        this.mat = mat;
    }

    public static HitRecord of(float t, Vec3 p, Vec3 normal, Material mat) {
        return new HitRecord(t, p, normal, mat);
    }

}
