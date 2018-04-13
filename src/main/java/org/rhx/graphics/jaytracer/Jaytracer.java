package org.rhx.graphics.jaytracer;

import org.rhx.graphics.jaytracer.data.*;
import org.rhx.window.Drawable;
import org.rhx.window.Renderer;

import java.awt.*;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.sqrt;
import static org.rhx.graphics.jaytracer.data.Vec3.*;

/**
 * Jaytracer main class.
 */
public class Jaytracer implements Renderer {

    private int scrWidth, scrHeight;
    private Random rand = new Random(System.currentTimeMillis());
    private Camera camera;
    private int ns = 100;

    @Override
    public void init(Drawable drawable) {
        Dimension dimension = drawable.getDimension();
        scrWidth = dimension.width;
        scrHeight = dimension.height;
        camera = new Camera();
    }

    @Override
    public void drawOn(Drawable drawable) {
        long start = System.currentTimeMillis();

        int nx = scrWidth;
        int ny = scrHeight;

        DataBufferInt dataBuffer = (DataBufferInt) drawable.getDrawableRaster().getDataBuffer();
        int[] offScreenRaster = dataBuffer.getData();


        HitableList world = getSceneData();

        for (int j = ny - 1; j >= 0; j--) {
            for (int i = 0; i < nx; i++) {
                renderPoint(nx, ny, offScreenRaster, world, i, j, false);
            }
        }

        System.out.println(String.format("Frame time is %d[ms]", System.currentTimeMillis() - start));
    }

    private void renderPoint(int nx, int ny, int[] offScreenRaster, HitableList world, int i, int j, boolean debug) {
        Vec3 color = Vec3.ZERO;
        for (int s = 0; s < ns; ++s) {
            float u = (i + rand.nextFloat())/(float)nx;
            float v = (j + rand.nextFloat())/(float)ny;
            Ray r = camera.getRay(u, v);
            Ray.pap(2f, r);
            color = Vec3.add(color, color(r, world, 0));
        }
        color = Vec3.div(color, (float)ns);

        color = Vec3.of(sqrt(color.r()),sqrt(color.g()),sqrt(color.b()));

        if (debug)
            System.out.println(String.format("Point: (%d, %d) | Color:%s", i, j, color));
        int ic = 0;
        ic |= ((int) (255.99f * color.r())) << 16;
        ic |= ((int) (255.99f * color.g())) << 8;
        ic |= ((int) (255.99f * color.b()));
        offScreenRaster[(ny - j -1) * nx + i] = ic;
    }

    public void drawPixelOn(Drawable drawable, int i, int j) {
        long start = System.currentTimeMillis();

        DataBufferInt dataBuffer = (DataBufferInt) drawable.getDrawableRaster().getDataBuffer();
        int[] offScreenRaster = dataBuffer.getData();


        HitableList world = getSceneData();

        renderPoint(scrWidth, scrHeight, offScreenRaster, world, i, j, true);

        System.out.println(String.format("Point time is %d[ms]", System.currentTimeMillis() - start));
    }

    private HitableList getSceneData() {
        List<Hitable> hitables = new ArrayList<>();
        hitables.add(Sphere.of(Vec3.of(0f, 0f, -1f),.5f, Lambertian.of(Vec3.of(.1f, .2f, .5f))));
        hitables.add(Sphere.of(Vec3.of(0f, -100.5f, -1f),100f, Lambertian.of(Vec3.of(.8f, .8f, 0f))));
        hitables.add(Sphere.of(Vec3.of(1f, 0f, -1f),.5f, Metal.of(Vec3.of(.8f, .6f, .2f), .1f)));
        hitables.add(Sphere.of(Vec3.of(-1f, 0f, -1f),.5f, Dielectric.of(1.5f)));
        hitables.add(Sphere.of(Vec3.of(-1f, 0f, -1f),-.45f, Dielectric.of(1.5f)));
        return HitableList.of(hitables);
    }

    private Vec3 color(Ray ray, Hitable world, int depth) {
        HitRecord rec = world.hit(ray, .001f, Float.MAX_VALUE);
        if (rec != null) {
            Out<Ray> scattered = Out.empty();
            Out<Vec3> attenuation = Out.empty();

            if (depth < 50 && rec.mat.scatter(ray, rec, attenuation, scattered)) {
                return Vec3.mul(attenuation.get(), color(scattered.get(), world, depth + 1));
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
