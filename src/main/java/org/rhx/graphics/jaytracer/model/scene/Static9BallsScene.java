package org.rhx.graphics.jaytracer.model.scene;

import org.rhx.graphics.jaytracer.Camera;
import org.rhx.graphics.jaytracer.MovingSphere;
import org.rhx.graphics.jaytracer.Sphere;
import org.rhx.graphics.jaytracer.model.Hitable;
import org.rhx.graphics.jaytracer.model.HitableList;
import org.rhx.graphics.jaytracer.model.Vec3;
import org.rhx.graphics.jaytracer.model.material.Dielectric;
import org.rhx.graphics.jaytracer.model.material.Lambertian;
import org.rhx.graphics.jaytracer.model.material.Metal;

import java.util.ArrayList;
import java.util.List;

public class Static9BallsScene implements SceneDescription {

    private static final Static9BallsScene SCENE;

    private final HitableList sceneDescription;

    static {
        List<Hitable> hitables = new ArrayList<>();

        hitables.add(Sphere.of(Vec3.of(0, -100.5f, -1), 100f, Lambertian.of(Vec3.of(0.8f, 0.8f, 0.8f))));
        hitables.add(MovingSphere.of(Vec3.of(2, 0, -1), Vec3.of(2, 1, -1), 0.f, 1.f, 0.5f, Lambertian.of(Vec3.of(0.8f, 0.4f, 0.4f))));
        hitables.add(Sphere.of(Vec3.of(0, 0, -1), 0.5f, Lambertian.of(Vec3.of(0.4f, 0.8f, 0.4f))));

        hitables.add(Sphere.of(Vec3.of(-2, 0, -1), 0.5f, Metal.of(Vec3.of(0.4f, 0.4f, 0.8f), 0f)));
        hitables.add(Sphere.of(Vec3.of(2, 0, 1), 0.5f, Metal.of(Vec3.of(0.4f, 0.8f, 0.4f), 0f)));
        hitables.add(Sphere.of(Vec3.of(0, 0, 1), 0.5f, Metal.of(Vec3.of(0.4f, 0.8f, 0.4f), 0.2f)));
        hitables.add(Sphere.of(Vec3.of(-2, 0, 1), 0.5f, Metal.of(Vec3.of(0.4f, 0.8f, 0.4f), 0.6f)));

        hitables.add(Sphere.of(Vec3.of(0.5f, 1, 0.5f), 0.5f, Dielectric.of(1.5f)));
        hitables.add(Sphere.of(Vec3.of(-1.5f, 1.5f, 0f), 0.3f, Lambertian.of(Vec3.of(.3f, .25f, .15f))));
        SCENE = new Static9BallsScene(HitableList.of(hitables));
    }

    private Static9BallsScene(HitableList sceneDescription) {
        this.sceneDescription = sceneDescription;
    }

    public static Static9BallsScene get() {
        return SCENE;
    }

    @Override
    public HitableList getSceneDescription() {
        return sceneDescription;
    }

    @Override
    public Camera getCamera(int width, int height) {
        Vec3 lookFrom = Vec3.of(1f, 4f, 8f);
        Vec3 lookAt = Vec3.of(0f, .5f, 0f);
        float distToFocus = Vec3.len(Vec3.sub(lookFrom, lookAt));
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
