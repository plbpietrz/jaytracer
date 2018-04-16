package org.rhx.graphics.jaytracer.model.material;

import org.rhx.graphics.jaytracer.Ref;
import org.rhx.graphics.jaytracer.model.Ray;
import org.rhx.graphics.jaytracer.model.Vec3;
import org.rhx.graphics.jaytracer.model.util.HitRecord;

/**
 * Metal material definition. Will bounce the {@link Ray} in a defined way, with a small variance base on the
 * fuzz parameter.
 */
public class Metal implements Material {

    private final Vec3 albedo;
    private final float fuzz;

    private Metal(Vec3 albedo, float fuzz) {
        this.albedo = albedo;
        this.fuzz  = fuzz < 1f ? fuzz : 1f;
    }

    @Override
    public boolean scatter(Ray rayIn, HitRecord rec, Ref<Vec3> attenuation, Ref<Ray> scattered) {
        Vec3 reflected = reflect(Vec3.unit(rayIn.dir), rec.norm);
        scattered.set(Ray.of(rec.pnt, Vec3.add(reflected, Vec3.mul(fuzz, Vec3.rvius()))));
        attenuation.set(albedo);
        return Vec3.dot(scattered.get().dir, rec.norm) > 0;
    }

    private Vec3 reflect(Vec3 v, Vec3 n) {
        return Vec3.sub(v, Vec3.mul(2f * Vec3.dot(v, n), n));
    }

    public static Metal of(Vec3 albedo, float fuzz) {
        return new Metal(albedo, fuzz);
    }

    @Override
    public String toString() {
        return String.format("#M[%s, %.2f]", albedo, fuzz);
    }
}
