package org.rhx.graphics.jaytracer.scene;

import org.rhx.graphics.jaytracer.Camera;
import org.rhx.graphics.jaytracer.Sphere;
import org.rhx.graphics.jaytracer.core.Hitable;
import org.rhx.graphics.jaytracer.core.HitableList;
import org.rhx.graphics.jaytracer.core.Vec3;
import org.rhx.graphics.jaytracer.material.Dielectric;
import org.rhx.graphics.jaytracer.material.Lambertian;
import org.rhx.graphics.jaytracer.material.Metal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBallsScene implements SceneDescription {

    private HitableList sceneDescription;

    private RandomBallsScene() {}

    public static RandomBallsScene get() {
        return new RandomBallsScene();
    }

    @Override
    public HitableList getSceneDescription() {
        if (sceneDescription == null)
            sceneDescription = HitableList.of(getRandomSpheres());
        return sceneDescription;
    }

    private List<Hitable> getRandomSpheres() {
        Random rand = ThreadLocalRandom.current();
        List<Hitable> hitable = new ArrayList<>();
        hitable.add(Sphere.of(Vec3.of(0f, -1000f, 0f), 1000f, Lambertian.of(Vec3.of(.5f, .5f, .5f))));
        for (int a = -11; a < 11; ++a) {
            for (int b = -11; b < 11; ++b) {
                float chooseMat = rand.nextFloat();
                Vec3 center = Vec3.of(a + .9f * rand.nextFloat(), .2f, b + .9f * rand.nextFloat());
                if (Vec3.len(Vec3.sub(center, Vec3.of(4f, .2f, 0f))) > .9f) {
                    if (chooseMat < .8f) {
                        hitable.add(
                                Sphere.of(
                                        Vec3.add(center, Vec3.of(0f, .5f * rand.nextFloat(), 0f )),
                                        .2f,
                                        Lambertian.of(
                                                Vec3.of(
                                                        rand.nextFloat() * rand.nextFloat(),
                                                        rand.nextFloat() * rand.nextFloat(),
                                                        rand.nextFloat() * rand.nextFloat()))));
                    } else if (chooseMat < .95f) {
                        hitable.add(
                                Sphere.of(
                                        center, .2f,
                                        Metal.of(
                                                Vec3.of(
                                                        .5f * (1 + rand.nextFloat()),
                                                        .5f * (1 + rand.nextFloat()),
                                                        .5f * (1 + rand.nextFloat())),
                                                .5f * (1 + rand.nextFloat()))));
                    } else {
                        hitable.add(Sphere.of(center, .2f, Dielectric.of(1.5f)));
                    }
                }
            }
        }
        hitable.add(Sphere.of(Vec3.of(0f, 1f, 0f), 1f, Dielectric.of(1.5f)));
        hitable.add(Sphere.of(Vec3.of(-4f, 1f, 0f), 1f, Lambertian.of(Vec3.of(.4f, .2f, .1f))));
        hitable.add(Sphere.of(Vec3.of(4f, 1f, 0f), 1f, Metal.of(Vec3.of(.7f, .6f, .5f), 0f)));
        return hitable;
    }

    @Override
    public Camera getCamera(int width, int height) {
        Vec3 lookFrom = Vec3.of(13f, 2f, 3f);
        Vec3 lookAt = Vec3.ZERO;
        Vec3 vup = Vec3.of(0f, 1f, 0f);
        float distToFocus = 10;
        float aperture = .1f;

        return Camera.of(
                lookFrom, lookAt, vup,
                20, (float) width / (float) height,
                aperture, distToFocus,
                0f, 1f
        );
    }
}
