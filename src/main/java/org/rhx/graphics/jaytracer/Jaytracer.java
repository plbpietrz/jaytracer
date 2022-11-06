package org.rhx.graphics.jaytracer;

import org.rhx.graphics.jaytracer.core.Hitable;
import org.rhx.graphics.jaytracer.core.Ray;
import org.rhx.graphics.jaytracer.core.Vec3;
import org.rhx.graphics.jaytracer.scene.SceneDescription;
import org.rhx.graphics.jaytracer.util.HitRecord;
import org.rhx.graphics.jaytracer.util.Ref;
import org.rhx.graphics.jaytracer.util.SimpleRNG;
import org.rhx.window.RenderStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.sqrt;
import static java.lang.System.currentTimeMillis;
import static org.rhx.graphics.jaytracer.core.Vec3.*;

/**
 * Jaytracer main class.
 */
public class Jaytracer {

    private static final Logger LOG = LoggerFactory.getLogger(Jaytracer.class);
    private static final SimpleRNG rand = SimpleRNG.get();

    private final BufferedImage drawable;
    private final int scrWidth;
    private final int scrHeight;
    private final int raysPerPixel;

    private Hitable world;
    private Camera camera;
    private Runnable onFinish;
    private RenderStatistics renderStatistics;

    private final AtomicInteger nrOfPixelDone;
    private final AtomicInteger currRaysDone;
    private final AtomicInteger lastRaysDone;
    private long renderTime;


    public Jaytracer(int raysPerPixel, BufferedImage image) {
        this.raysPerPixel = raysPerPixel;
        this.drawable = image;
        this.scrWidth = image.getWidth();
        this.scrHeight = image.getHeight();
        this.currRaysDone = new AtomicInteger(0);
        this.lastRaysDone = new AtomicInteger(0);
        this.nrOfPixelDone = new AtomicInteger(0);
        this.renderStatistics = new RenderStatistics(0, 1, 0, 0);
    }

    public void draw(SceneDescription sceneDescription) {
        world = sceneDescription.getSceneDescription();
//        world = BVHNode.of(sceneDescription.getSceneDescription(), 0.f, 1.f);
        camera = sceneDescription.getCamera(scrWidth, scrHeight);
        resetStatistics();

        long start = currentTimeMillis();
        for (int j = scrHeight - 1; j >= 0; j--) {
            for (int i = 0; i < scrWidth; i++) {
                renderPoint(i, j, false);
            }

        }
        renderTime = currentTimeMillis() - start;

        if (onFinish != null)
            onFinish.run();
    }

    public RenderStatistics getStats() {
        int lastRays = lastRaysDone.get();
        int currentRays = currRaysDone.get();
        if (currentRays != lastRays) {
            renderStatistics = new RenderStatistics(
                    nrOfPixelDone.get(),
                    scrWidth * scrHeight,
                    currentRays - lastRays,
                    renderTime);
            lastRaysDone.set(currentRays);
        }
        return renderStatistics;
    }

    private void renderPoint(int i, int j, boolean debug) {
        Vec3 color = ZERO;
        for (int s = 0; s < raysPerPixel; ++s) {
            float u = (i + rand.nextFloat())/(float)scrWidth;
            float v = (j + rand.nextFloat())/(float)scrHeight;
            Ray r = camera.getRay(u, v);
            Ray.pap(2f, r);
            color = add(color, trace(r, 0));
            currRaysDone.incrementAndGet();
        }
        color = div(color, (float) raysPerPixel);

        color = Vec3.of(sqrt(color.r()),sqrt(color.g()),sqrt(color.b()));

        if (debug)
            LOG.debug("Point: ({}, {}) | Color:{}", i, j, color);
        int ic = 0;
        ic |= ((int) (255.99f * color.r())) << 16;
        ic |= ((int) (255.99f * color.g())) << 8;
        ic |= ((int) (255.99f * color.b()));
        drawable.setRGB(i, scrHeight - j - 1, ic);
        nrOfPixelDone.incrementAndGet();
    }

    private Vec3 trace(Ray ray, int depth) {
        HitRecord rec = world.hit(ray, .001f, Float.MAX_VALUE);
        if (rec != null) {
            Ref<Ray> scattered = Ref.empty();
            Ref<Vec3> attenuation = Ref.empty();

            if (depth < 50 && rec.mat.scatter(ray, rec, attenuation, scattered)) {
                return mul(attenuation.get(), trace(scattered.get(), depth + 1));
            } else {
                return ZERO;
            }
        } else {
            Vec3 unitDir = unit(ray.dir);
            float t = .5f *( unitDir.y() + 1f);
            return add(
                    mul(1f - t, Vec3.ONES),
                    mul(t, Vec3.of(.5f, .7f, 1f)));
        }
    }

    private void resetStatistics() {
        renderTime = 0;
        currRaysDone.set(0);
        lastRaysDone.set(0);
        nrOfPixelDone.set(0);
    }

    public void drawPixelOn(int i, int j) {
        long start = currentTimeMillis();
        currRaysDone.set(0);
        lastRaysDone.set(0);

        renderPoint(i, j, true);

        LOG.info("Point time is {}[ms]", currentTimeMillis() - start);
    }

    public void onRenderFinish(Runnable onFinish) {
        this.onFinish = onFinish;
    }

    public BufferedImage getImage() {
        return drawable;
    }

}
