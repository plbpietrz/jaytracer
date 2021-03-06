package org.rhx.graphics.jaytracer.model.material;

import org.rhx.graphics.jaytracer.Ref;
import org.rhx.graphics.jaytracer.model.Ray;
import org.rhx.graphics.jaytracer.model.Vec3;
import org.rhx.graphics.jaytracer.model.util.HitRecord;

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
        Vec3 target = Vec3.add(rec.pnt, rec.norm, Vec3.rvius());
        scattered.set(Ray.of(rec.pnt, Vec3.sub(target, rec.pnt), rayIn.time));
        attenuation.set(albedo);
        return true;
    }

    public static Lambertian of(Vec3 albedo) {
        return new Lambertian(albedo);
    }

    @Override
    public String toString() {
        return String.format("#L%s", albedo);
    }
}
