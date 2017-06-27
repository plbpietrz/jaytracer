package org.rhx.graphics.jaytracer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.rhx.graphics.jaytracer.Ray.*;
import static org.rhx.graphics.jaytracer.Vec3.*;

/**
 * Jaytracer main class.
 */
public class Jaytracer {
    public static void main(String[] args) throws IOException {
        int nx = 200;
        int ny = 100;

        BufferedImage image = new BufferedImage(nx, ny, BufferedImage.TYPE_INT_RGB);
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        DataBufferInt dataBufferInt = (DataBufferInt) dataBuffer;

        Vec3 lowerLeftCorner = Vec3.of(-2.0f, -1.0f, -1.0f);
        Vec3 horizontal = Vec3.of(4.0f, 0.0f, 0.0f);
        Vec3 vertical = Vec3.of(0.0f, 2.0f, 0.0f);
        Vec3 origin = Vec3.of(0.0f, 0.0f, 0.0f);

        List<Hitable> hitables = new ArrayList<>();
        hitables.add(Sphere.of(Vec3.of(0,0,-1),0.5f));
        hitables.add(Sphere.of(Vec3.of(0,-100.5f,-1),100.0f));
        HitableList world = HitableList.of(hitables);

        for (int j = ny - 1; j >= 0; j--) {
            for (int i = 0; i < nx; i++) {
                float u = ((float) i) / ((float) nx);
                float v = ((float) j) / ((float) ny);

                Ray ray = Ray.of(origin, add(lowerLeftCorner, mul(u, horizontal), mul(v, vertical)));
                Vec3 p = pap(2.0f, ray);

                Vec3 color = color(ray, world);

                int ic = 0;
                ic |= ((int) (255.99 * color.r())) << 16;
                ic |= ((int) (255.99 * color.g())) << 8;
                ic |= ((int) (255.99 * color.b()));
                dataBufferInt.setElem(j * nx + i, ic);
            }
        }

        File outImg = Paths.get("out_img.jpg").toFile();
        ImageIO.write(image, "jpg", outImg);
    }

    private static Vec3 color(Ray ray) {
        float t = hitSphere(Vec3.of(0.0f, 0.0f, -1.0f), 0.5f, ray);
        if (t > 0.0f) {
            Vec3 n = unit(sub(pap(t, ray), Vec3.of(0.0f, 0.0f, -1.0f)));
            return mul(0.5f, Vec3.of(n.x() + 1, n.y() + 1, n.z() + 1));
        } else {
            Vec3 unit = unit(dir(ray));
            t = 0.5f * (unit.y() + 1.0f);
            return add(mul((1.0f - t), Vec3.of(1.0f, 1.0f, 1.0f)), mul(t, Vec3.of(0.5f, 0.7f, 1.0f)));
        }
    }

    private static Vec3 color(Ray ray, Hitable world) {
        OutRef<Hitable.HitRecord> rec = OutRef.of((Hitable.HitRecord) null);
        if (world.hit(ray, 0.0f, Float.MAX_VALUE, rec)) {
            return mul(
                    0.5f,
                    Vec3.of(
                            rec.get().normal.x()+1,
                            rec.get().normal.y()+1,
                            rec.get().normal.z()+1)
            );
        } else {
            Vec3 unitDir = unit(dir(ray));
            float t = 0.5f *( unitDir.y() + 1.0f);
            return add(
                    mul(
                            1.0f - t,
                            Vec3.of(1.0f, 1.0f, 1.0f)),
                    mul(
                            t,
                            Vec3.of(.5f, .7f, 1.0f)));
        }
    }

    private static float hitSphere(final Vec3 center, final float radius, final Ray ray) {
        Vec3 oc = sub(org(ray), center);
        float a = dot(dir(ray), dir(ray));
        float b = 2.0f * dot(oc, dir(ray));
        float c = dot(oc, oc) - radius * radius;
        float discr = b * b - 4 * a * c;
        if (discr < 0)
            return -1.0f;
        else
            return (-b - (float) Math.sqrt(discr)) / (2.0f * a);
    }
}
