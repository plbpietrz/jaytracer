package org.rhx.graphics.jaytracer.data;

public class HitRecord {

    /**
     * Distance from the screen
     */
    final float t;

    /**
     * Point of impact
     */
    final Vec3 pnt;

    /**
     * Surface normal vector at the impact point pnt and distance t
     */
    final Vec3 norm;

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

    @Override
    public String toString() {
        return String.format("![t:%f, pnt:%s, norm:%s]", t, pnt, norm);
    }

    public static HitRecord of(float t, Vec3 p, Vec3 normal, Material mat) {
        return new HitRecord(t, p, normal, mat);
    }

}
