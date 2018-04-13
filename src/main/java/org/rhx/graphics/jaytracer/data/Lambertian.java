package org.rhx.graphics.jaytracer.data;

import org.rhx.graphics.jaytracer.Out;

public class Lambertian implements Material {

    private final Vec3 albedo;

    private Lambertian(final Vec3 albedo) {
        this.albedo = albedo;
    }

    @Override
    public boolean scatter(Ray rayIn, HitRecord rec, Out<Vec3> attenuation, Out<Ray> scattered) {
        Vec3 target = Vec3.add(rec.pnt, rec.norm, Vec3.randInUnitSph());
        scattered.set(Ray.of(rec.pnt, Vec3.sub(target, rec.pnt)));
        attenuation.set(albedo);
        return true;
    }

    public static Lambertian of(final Vec3 albedo) {
        return new Lambertian(albedo);
    }

    @Override
    public String toString() {
        return String.format("#L%s", albedo);
    }
}
