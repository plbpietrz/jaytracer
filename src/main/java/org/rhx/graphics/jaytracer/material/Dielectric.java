package org.rhx.graphics.jaytracer.material;

import org.rhx.graphics.jaytracer.core.Ray;
import org.rhx.graphics.jaytracer.core.Vec3;
import org.rhx.graphics.jaytracer.util.HitRecord;
import org.rhx.graphics.jaytracer.util.Ref;
import org.rhx.graphics.jaytracer.util.SimpleRNG;

import static org.rhx.graphics.jaytracer.core.Vec3.*;

/**
 * Dielectric material definition. Scatters and reflects light based on the refraction index and Schlicks approximation.
 */
public class Dielectric implements Material {

    private static final SimpleRNG rand = SimpleRNG.get();

    /** Refraction index */
    private final float refIdx;

    private Dielectric(float refIdx) {
        this.refIdx = refIdx;
    }

    @Override
    public boolean scatter(Ray rayIn, HitRecord rec, Ref<Vec3> attenuation, Ref<Ray> scattered) {
        float ni_over_nt, cosine, reflectProb;
        Vec3 outwardNormal;
        attenuation.set(Vec3.of(1f, 1f, 1f));

        if (dot(rayIn.dir, rec.norm) > 0f) {
            outwardNormal = neg(rec.norm);
            ni_over_nt = refIdx;
            cosine = refIdx + dot(rayIn.dir, rec.norm) / len(rayIn.dir);
        } else {
            outwardNormal = rec.norm;
            ni_over_nt = 1f / refIdx;
            cosine = -dot(rayIn.dir, rec.norm) / len(rayIn.dir);
        }

        Ref<Vec3> refracted = Ref.empty();
        if (refract(rayIn.dir, outwardNormal, ni_over_nt, refracted)) {
            reflectProb = schlick(cosine, refIdx);
        } else {
            reflectProb = 1f;
        }
        if (rand.nextFloat() < reflectProb) {
            Vec3 reflected = reflect(rayIn.dir, rec.norm);
            scattered.set(Ray.of(rec.pnt, reflected));
        } else {
            scattered.set(Ray.of(rec.pnt, refracted.get()));
        }
        return true;
    }

    private static Vec3 reflect(Vec3 v, Vec3 n) {
        return sub(v, mul(2f * dot(v, n), n));
    }

    private static boolean refract(Vec3 v, Vec3 n, float ni_over_nt, Ref<Vec3> refracted) {
        Vec3 uv = unit(v);
        float dt = dot(uv, n);
        float discriminant = 1f - ni_over_nt * ni_over_nt * (1 - dt * dt);
        if (discriminant > 0f) {
            refracted.set(sub(mul(ni_over_nt, sub(uv, mul(dt, n))), mul((float) Math.sqrt(discriminant), n)));
            return true;
        } else {
            return false;
        }
    }

    private static float schlick(float cosine, float refIdx) {
        float r0 = (1 - refIdx) / (1 + refIdx);
        r0 = r0 * r0;
        return (r0 + (1 - r0) * (float)Math.pow((1 - cosine), 5f));
    }

    public static Dielectric of(float refractionIdx) {
        return new Dielectric(refractionIdx);
    }

}
