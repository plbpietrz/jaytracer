package org.rhx.graphics.jaytracer;

/**
 * Basic geometry object type that can interact with {@link Ray}.
 */
public interface Hitable {

    HitRecord hit(Ray ray, float tMin, float tMax);

    class HitRecord {

        public final float t;
        public final Vec3 p;
        public final Vec3 normal;

        private HitRecord(float t, Vec3 p, Vec3 normal) {
            this.t = t;
            this.p = p;
            this.normal = normal;
        }

        public static HitRecord of(float t, Vec3 p, Vec3 normal) {
            return new HitRecord(t, p, normal);
        }
    }
}

