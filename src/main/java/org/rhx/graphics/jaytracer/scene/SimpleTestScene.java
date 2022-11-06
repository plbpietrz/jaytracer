package org.rhx.graphics.jaytracer.scene;

import org.rhx.graphics.jaytracer.Camera;
import org.rhx.graphics.jaytracer.MovingSphere;
import org.rhx.graphics.jaytracer.Sphere;
import org.rhx.graphics.jaytracer.core.Hitable;
import org.rhx.graphics.jaytracer.core.HitableList;
import org.rhx.graphics.jaytracer.core.Vec3;
import org.rhx.graphics.jaytracer.material.Lambertian;

import java.util.ArrayList;
import java.util.List;

import static org.rhx.graphics.jaytracer.core.Vec3.len;
import static org.rhx.graphics.jaytracer.core.Vec3.sub;

public class SimpleTestScene implements SceneDescription {


    @Override
    public HitableList getSceneDescription() {
        List<Hitable> hitables = new ArrayList<>();

        hitables.add(Sphere.of(Vec3.of(0, -100.5f, -1), 100f, Lambertian.of(Vec3.of(0.8f, 0.8f, 0.8f))));
        hitables.add(MovingSphere.of(Vec3.of(2, 0, -1), Vec3.of(2, 1, -1), 0.f, 1.f, 0.5f, Lambertian.of(Vec3.of(0.8f, 0.4f, 0.4f))));
        hitables.add(Sphere.of(Vec3.of(0, 0, -1), 0.5f, Lambertian.of(Vec3.of(0.4f, 0.8f, 0.4f))));
        return HitableList.of(hitables);
    }

    @Override
    public Camera getCamera(int width, int height) {
        Vec3 lookFrom = Vec3.of(1f, 4f, 8f);
        Vec3 lookAt = Vec3.of(0f, .5f, 0f);
        float distToFocus = len(sub(lookFrom, lookAt));
        float aperture = .1f;

        return Camera.of(
                lookFrom, lookAt,
                Vec3.of(0f, 1f, 0f),
                25, (float) width / (float) height,
                aperture, distToFocus,
                0f, 1f
        );
    }
}
