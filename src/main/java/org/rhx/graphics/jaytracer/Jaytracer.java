package org.rhx.graphics.jaytracer;

import org.rhx.graphics.jaytracer.model.Hitable;
import org.rhx.graphics.jaytracer.model.HitableList;
import org.rhx.graphics.jaytracer.model.Ray;
import org.rhx.graphics.jaytracer.model.Vec3;
import org.rhx.graphics.jaytracer.model.material.Dielectric;
import org.rhx.graphics.jaytracer.model.material.Lambertian;
import org.rhx.graphics.jaytracer.model.material.Metal;
import org.rhx.graphics.jaytracer.model.util.HitRecord;
import org.rhx.window.Drawable;
import org.rhx.window.Renderer;
import org.rhx.window.Stats;

import java.awt.*;
import java.awt.image.DataBufferInt;
import java.util.List;
import java.util.Random;

import static java.lang.Math.sqrt;
import static org.rhx.graphics.jaytracer.model.Vec3.*;

/**
 * Jaytracer main class.
 */
public class Jaytracer implements Renderer {


    private final HitableList sceneDescription;
    private Drawable drawable;
    private int scrWidth, scrHeight;

    private Random rand;
    private Camera camera;

    private volatile int nrOfPixelDone = 0;

    private int nrOfSamplesPerPixel;
    private int currRaysDone;
    private int lastRaysDone;

    public Jaytracer(int nrOfSamplesPerPixel, Camera camera, HitableList sceneDescription) {
        this.nrOfSamplesPerPixel = nrOfSamplesPerPixel;
        this.rand = new Random(System.currentTimeMillis());
        this.sceneDescription = sceneDescription;
        this.camera = camera;
    }

    @Override
    public void init(Drawable drawable) {
        this.drawable = drawable;
        Dimension dimension = drawable.getDimension();
        scrWidth = dimension.width;
        scrHeight = dimension.height;
    }

    @Override
    public void draw() {
        long start = System.currentTimeMillis();

        DataBufferInt dataBuffer = (DataBufferInt) drawable.getDrawableRaster().getDataBuffer();
        int[] offScreenRaster = dataBuffer.getData();


        HitableList world = getSceneDescription();

        for (int j = scrHeight - 1; j >= 0; j--) {
            for (int i = 0; i < scrWidth; i++) {
                renderPoint(scrWidth, scrHeight, offScreenRaster, world, i, j, false);
                nrOfPixelDone = (scrHeight - j) * scrWidth + i;
            }
        }

        System.out.println(String.format("Frame time is %d[ms]", System.currentTimeMillis() - start));
    }

    @Override
    public Stats getStats() {
        if (scrHeight == 0 || scrHeight == 0) {
            return new Stats(0, 1, 0);
        } else {
            int rays = currRaysDone - lastRaysDone;
            lastRaysDone = currRaysDone;
            return new Stats(nrOfPixelDone + 1, scrWidth * scrHeight, rays);
        }
    }

    private void renderPoint(int nx, int ny, int[] offScreenRaster, HitableList world, int i, int j, boolean debug) {
        Vec3 color = Vec3.ZERO;
        for (int s = 0; s < nrOfSamplesPerPixel; ++s) {
            float u = (i + rand.nextFloat())/(float)nx;
            float v = (j + rand.nextFloat())/(float)ny;
            Ray r = camera.getRay(u, v);
            Ray.pap(2f, r);
            color = Vec3.add(color, trace(r, world, 0));
            currRaysDone += 1;
        }
        color = Vec3.div(color, (float) nrOfSamplesPerPixel);

        color = Vec3.of(sqrt(color.r()),sqrt(color.g()),sqrt(color.b()));

        if (debug)
            System.out.println(String.format("Point: (%d, %d) | Color:%s", i, j, color));
        int ic = 0;
        ic |= ((int) (255.99f * color.r())) << 16;
        ic |= ((int) (255.99f * color.g())) << 8;
        ic |= ((int) (255.99f * color.b()));
        offScreenRaster[(ny - j -1) * nx + i] = ic;
    }

    public void drawPixelOn(int i, int j) {
        long start = System.currentTimeMillis();

        DataBufferInt dataBuffer = (DataBufferInt) drawable.getDrawableRaster().getDataBuffer();
        int[] offScreenRaster = dataBuffer.getData();


        HitableList world = getSceneDescription();

        renderPoint(scrWidth, scrHeight, offScreenRaster, world, i, j, true);

        System.out.println(String.format("Point time is %d[ms]", System.currentTimeMillis() - start));
    }

    private HitableList getSceneDescription() {
        return sceneDescription;
    }

    private void getRandomSpheres(List<Hitable> hitables) {
        hitables.add(Sphere.of(Vec3.of(0f, -1000f, 0f),1000f, Lambertian.of(Vec3.of(.5f, .5f, .5f))));
        for (int a = -11; a < 11; ++a) {
            for (int b = -11; b < 11; ++b) {
                float chooseMat = rand.nextFloat();
                Vec3 center = Vec3.of(a + .9f * rand.nextFloat(), .2f, b + .9f * rand.nextFloat());
                if (Vec3.len(Vec3.sub(center, Vec3.of(4f, .2f, 0f))) > .9f) {
                    if (chooseMat < .8f) {
                        hitables.add(
                                Sphere.of(
                                        center, .2f,
                                        Lambertian.of(
                                                Vec3.of(
                                                        rand.nextFloat() * rand.nextFloat(),
                                                        rand.nextFloat() * rand.nextFloat(),
                                                        rand.nextFloat() * rand.nextFloat()))));
                    } else if (chooseMat < .95f) {
                        hitables.add(
                                Sphere.of(
                                        center, .2f,
                                        Metal.of(
                                                Vec3.of(
                                                        .5f * (1 + rand.nextFloat()),
                                                        .5f * (1 + rand.nextFloat()),
                                                        .5f * (1 + rand.nextFloat())),
                                                .5f * (1 + rand.nextFloat()))));
                    } else {
                        hitables.add(Sphere.of(center, .2f, Dielectric.of(1.5f)));
                    }
                }
            }
        }
        hitables.add(Sphere.of(Vec3.of(4f, 1f, 0f),1f, Dielectric.of(1.5f)));
        hitables.add(Sphere.of(Vec3.of(-4f, 1f, 0f),1f, Lambertian.of(Vec3.of(.4f, .2f, .1f))));
        hitables.add(Sphere.of(Vec3.of(0f, 1f, 0f),1f, Metal.of(Vec3.of(.7f, .6f, .5f), 0f)));
    }

    private Vec3 trace(Ray ray, Hitable world, int depth) {
        HitRecord rec = world.hit(ray, .001f, Float.MAX_VALUE);
        if (rec != null) {
            Ref<Ray> scattered = Ref.empty();
            Ref<Vec3> attenuation = Ref.empty();

            if (depth < 50 && rec.mat.scatter(ray, rec, attenuation, scattered)) {
                return Vec3.mul(attenuation.get(), trace(scattered.get(), world, depth + 1));
            } else {
                return Vec3.ZERO;
            }
        } else {
            Vec3 unitDir = unit(ray.dir);
            float t = .5f *( unitDir.y() + 1f);
            return add(
                    mul(1f - t, Vec3.ONES),
                    mul(t, Vec3.of(.5f, .7f, 1f)));
        }
    }



}
