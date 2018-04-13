package org.rhx.graphics.jaytracer.data;

import org.rhx.graphics.jaytracer.Out;

public class Metal implements Material {

    private final Vec3 albedo;
    private final float fuzz;

    private Metal(Vec3 albedo, float fuzz) {
        this.albedo = albedo;
        this.fuzz  = fuzz < 1f ? fuzz : 1f;
    }

    @Override
    public boolean scatter(Ray rayIn, HitRecord rec, Out<Vec3> attenuation, Out<Ray> scattered) {
        Vec3 reflected = reflect(Vec3.unit(rayIn.dir), rec.norm);
        scattered.set(Ray.of(rec.pnt, Vec3.add(reflected, Vec3.mul(fuzz, Vec3.randInUnitSph()))));
        attenuation.set(albedo);
        return Vec3.dot(scattered.get().dir, rec.norm) > 0;
    }

    private Vec3 reflect(Vec3 v, Vec3 n) {
        return Vec3.sub(v, Vec3.mul(2f * Vec3.dot(v, n), n));
    }

    public static Metal of(final Vec3 albedo, final float fuzz) {
        return new Metal(albedo, fuzz);
    }

    @Override
    public String toString() {
        return String.format("#M[%s, %.2f]", albedo, fuzz);
    }
}
