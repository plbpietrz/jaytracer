package org.rhx.graphics.jaytracer.material;

import org.rhx.graphics.jaytracer.util.Ref;
import org.rhx.graphics.jaytracer.core.Ray;
import org.rhx.graphics.jaytracer.core.Vec3;
import org.rhx.graphics.jaytracer.util.HitRecord;

import static org.rhx.graphics.jaytracer.core.Vec3.*;

/**
 * Lambertian material definition. Scatters {@link Ray}s in random directions modifying them with its diffuse color.
 */
public class Lambertian implements Material {

    private final Vec3 albedo;

    private Lambertian(Vec3 albedo) {
        this.albedo = albedo;
    }

    @Override
    public boolean scatter(Ray rayIn, HitRecord rec, Ref<Vec3> attenuation, Ref<Ray> scattered) {
        Vec3 target = add(rec.pnt, rec.norm, rvius());
        scattered.set(Ray.of(rec.pnt, sub(target, rec.pnt), rayIn.time));
        attenuation.set(albedo);
        return true;
    }

    public static Lambertian of(Vec3 albedo) {
        return new Lambertian(albedo);
    }

}
