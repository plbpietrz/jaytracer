package org.rhx.graphics.jaytracer;

import org.rhx.window.Drawable;
import org.rhx.window.Renderer;

import java.awt.*;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.rhx.graphics.jaytracer.Ray.*;
import static org.rhx.graphics.jaytracer.Vec3.*;

/**
 * Jaytracer main class.
 */
public class Jaytracer implements Renderer {

    private int scrWidth, scrHeight;
    private int[] offScreenRaster;

    @Override
    public void init(Drawable drawable) {
        Dimension dimension = drawable.getDimension();
        scrWidth = dimension.width;
        scrHeight = dimension.height;

        DataBufferInt dataBuffer = (DataBufferInt) drawable
                .getDrawableRaster()
                .getDataBuffer();
        offScreenRaster = new int[dataBuffer.getData().length];
    }

    @Override
    public void drawOn(Drawable drawable) {
        int nx = scrWidth;
        int ny = scrHeight;
        int ns = 20;


        List<Hitable> hitables = new ArrayList<>();
        hitables.add(Sphere.of(Vec3.of(0f,0f,-1f),.5f));
        hitables.add(Sphere.of(Vec3.of(0f,-100.5f,-1f),100f));
        HitableList world = HitableList.of(hitables);

        Camera camera = new Camera();
        Random rand = new Random(System.currentTimeMillis());
        for (int j = ny - 1; j >= 0; j--) {
            for (int i = 0; i < nx; i++) {

                Vec3 color = Vec3.ZERO;
                for (int s = 0; s < ns; ++s) {
                    float u = (float)(i + rand.nextDouble())/(float)nx;
                    float v = (float)(j + rand.nextDouble())/(float)ny;
                    Ray r = camera.getRay(u, v);
                    Ray.pap(2f, r);
                    color = Vec3.add(color, color(r, world));
                }
                color = Vec3.div(color, (float)ns);
                int ic = 0;
                ic |= ((int) (255.99f * color.r())) << 16;
                ic |= ((int) (255.99f * color.g())) << 8;
                ic |= ((int) (255.99f * color.b()));
                offScreenRaster[(ny - j -1) * nx + i] = ic;
            }
        }
        DataBufferInt dataBuffer = (DataBufferInt) drawable.getDrawableRaster().getDataBuffer();
        System.arraycopy(offScreenRaster, 0, dataBuffer.getData(), 0, offScreenRaster.length);
    }

    private static Vec3 color(Ray ray) {
        float t = hitSphere(Vec3.of(0f, 0f, -1f), .5f, ray);
        if (t > 0f) {
            Vec3 n = unit(sub(pap(t, ray), Vec3.of(0f, 0f, -1f)));
            return mul(.5f, Vec3.of(n.x() + 1, n.y() + 1, n.z() + 1));
        } else {
            Vec3 unit = unit(dir(ray));
            t = .5f * (unit.y() + 1f);
            return add(mul((1f - t), Vec3.of(1f, 1f, 1f)), mul(t, Vec3.of(.5f, .7f, 1f)));
        }
    }

    private static Vec3 color(Ray ray, Hitable world) {
        Hitable.HitRecord rec = world.hit(ray, 0f, Float.MAX_VALUE);
        if (rec != null) {
            return mul(
                    .5f,
                    Vec3.of(
                            rec.normal.x()+1,
                            rec.normal.y()+1,
                            rec.normal.z()+1)
            );
        } else {
            Vec3 unitDir = unit(dir(ray));
            float t = .5f *( unitDir.y() + 1f);
            return add(
                    mul(
                            1f - t,
                            Vec3.of(1f, 1f, 1f)),
                    mul(
                            t,
                            Vec3.of(.5f, .7f, 1f)));
        }
    }

    private static float hitSphere(final Vec3 center, final float radius, final Ray ray) {
        Vec3 oc = sub(org(ray), center);
        float a = dot(dir(ray), dir(ray));
        float b = 2f * dot(oc, dir(ray));
        float c = dot(oc, oc) - radius * radius;
        float discr = b * b - 4 * a * c;
        if (discr < 0)
            return -1f;
        else
            return (-b - (float) Math.sqrt(discr)) / (2f * a);
    }

}
