package org.rhx.graphics.jaytracer.material;

import org.rhx.graphics.jaytracer.util.Ref;
import org.rhx.graphics.jaytracer.core.Ray;
import org.rhx.graphics.jaytracer.core.Vec3;
import org.rhx.graphics.jaytracer.util.HitRecord;

import static org.rhx.graphics.jaytracer.core.Vec3.*;
import static org.rhx.graphics.jaytracer.util.FMath.fmin;

/**
 * Metal material definition. Will bounce the {@link Ray} in a defined way, with a small variance based on the
 * fuzz parameter.
 */
public class Metal implements Material {

    private final Vec3 albedo;
    private final float fuzz;

    private Metal(Vec3 albedo, float fuzz) {
        this.albedo = albedo;
        this.fuzz  = fmin(fuzz, 1f);
    }

    @Override
    public boolean scatter(Ray rayIn, HitRecord rec, Ref<Vec3> attenuation, Ref<Ray> scattered) {
        Vec3 reflected = reflect(unit(rayIn.dir), rec.norm);
        scattered.set(Ray.of(rec.pnt, add(reflected, mul(fuzz, rvius()))));
        attenuation.set(albedo);
        return dot(scattered.get().dir, rec.norm) > 0;
    }

    private static Vec3 reflect(Vec3 v, Vec3 n) {
        return sub(v, mul(2f * dot(v, n), n));
    }

    public static Metal of(Vec3 albedo, float fuzz) {
        return new Metal(albedo, fuzz);
    }

}
