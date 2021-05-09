package org.rhx.graphics.jaytracer;

import org.rhx.graphics.jaytracer.model.HitableList;
import org.rhx.graphics.jaytracer.model.Ray;
import org.rhx.graphics.jaytracer.model.Vec3;
import org.rhx.graphics.jaytracer.model.scene.SceneDescription;
import org.rhx.graphics.jaytracer.model.util.HitRecord;
import org.rhx.window.Stats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.sqrt;
import static org.rhx.graphics.jaytracer.model.Vec3.*;

/**
 * Jaytracer main class.
 */
public class Jaytracer {

    private static final Logger LOG = LoggerFactory.getLogger(Jaytracer.class);

    private final BufferedImage drawable;
    private final int scrWidth;
    private final int scrHeight;
    private final Random rand;

    private HitableList world;
    private Camera camera;

    private volatile int nrOfPixelDone = 0;

    private final int nrOfSamplesPerPixel;
    private int currRaysDone;
    private int lastRaysDone;
    private Runnable onFinish;

    public Jaytracer(int nrOfSamplesPerPixel, BufferedImage image) {
        this.nrOfSamplesPerPixel = nrOfSamplesPerPixel;
        this.rand = ThreadLocalRandom.current();
        this.drawable = image;
        this.scrWidth = image.getWidth();
        this.scrHeight = image.getHeight();
    }

    public void draw(SceneDescription sceneDescription) {
        this.world = sceneDescription.getSceneDescription();
        this.camera = sceneDescription.getCamera(scrWidth, scrHeight);

        long start = System.currentTimeMillis();

        for (int j = scrHeight - 1; j >= 0; j--) {

            for (int i = 0; i < scrWidth; i++) {
                renderPoint(i, j, false);
                nrOfPixelDone = (scrHeight - j) * scrWidth + i;
            }

        }

        LOG.info("Frame time is {}[ms]", System.currentTimeMillis() - start);
        if (onFinish != null)
            onFinish.run();
    }

    public Stats getStats() {
        if (scrWidth == 0 || scrHeight == 0) {
            return new Stats(0, 1, 0);
        } else {
            int rays = currRaysDone - lastRaysDone;
            lastRaysDone = currRaysDone;
            return new Stats(nrOfPixelDone + 1, scrWidth * scrHeight, rays);
        }
    }

    private void renderPoint(int i, int j, boolean debug) {
        Vec3 color = Vec3.ZERO;
        for (int s = 0; s < nrOfSamplesPerPixel; ++s) {
            float u = (i + rand.nextFloat())/(float)scrWidth;
            float v = (j + rand.nextFloat())/(float)scrHeight;
            Ray r = camera.getRay(u, v);
            Ray.pap(2f, r);
            color = Vec3.add(color, trace(r, 0));
            currRaysDone += 1;
        }
        color = Vec3.div(color, (float) nrOfSamplesPerPixel);

        color = Vec3.of(sqrt(color.r()),sqrt(color.g()),sqrt(color.b()));

        if (debug)
            LOG.info("Point: ({}, {}) | Color:{}", i, j, color);
        int ic = 0;
        ic |= ((int) (255.99f * color.r())) << 16;
        ic |= ((int) (255.99f * color.g())) << 8;
        ic |= ((int) (255.99f * color.b()));
        drawable.setRGB(i, scrHeight - j - 1, ic);
    }

    public void drawPixelOn(int i, int j) {
        long start = System.currentTimeMillis();

        renderPoint(i, j, true);

        LOG.info("Point time is {}[ms]", System.currentTimeMillis() - start);
    }

    private Vec3 trace(Ray ray, int depth) {
        HitRecord rec = world.hit(ray, .001f, Float.MAX_VALUE);
        if (rec != null) {
            Ref<Ray> scattered = Ref.empty();
            Ref<Vec3> attenuation = Ref.empty();

            if (depth < 50 && rec.mat.scatter(ray, rec, attenuation, scattered)) {
                return Vec3.mul(attenuation.get(), trace(scattered.get(), depth + 1));
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

    public void onRenderFinish(Runnable onFinish) {
        this.onFinish = onFinish;
    }

}
