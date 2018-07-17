package org.rhx.window;

import org.rhx.graphics.jaytracer.Camera;
import org.rhx.graphics.jaytracer.Jaytracer;
import org.rhx.graphics.jaytracer.Sphere;
import org.rhx.graphics.jaytracer.model.Hitable;
import org.rhx.graphics.jaytracer.model.HitableList;
import org.rhx.graphics.jaytracer.model.Vec3;
import org.rhx.graphics.jaytracer.model.material.Dielectric;
import org.rhx.graphics.jaytracer.model.material.Lambertian;
import org.rhx.graphics.jaytracer.model.material.Metal;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Main frame class.
 */
public class MainFrame {

    private static final int WIDTH_PARAM = 0;
    private static final int HEIGHT_PARAM = 1;
    private static final String TITLE = "Jaytracer";

    public static void main(String[] args) {
        int width;
        int height;
        if (args.length == 2) {
            width = Integer.parseInt(args[WIDTH_PARAM]);
            height = Integer.parseInt(args[HEIGHT_PARAM]);
        } else {
            width = 800;
            height = 400;
        }

        JFrame frame = buildFrame(width, height);

        Vec3 lookFrom = Vec3.of(0, 2, 3);
        Vec3 lookAt = Vec3.of(0, 0, 0);

        List<Hitable> hitables = new ArrayList<>();

        hitables.add(Sphere.of(Vec3.of(0, -100.5f, -1), 100f, Lambertian.of(Vec3.of(0.8f, 0.8f, 0.8f))));
        hitables.add(Sphere.of(Vec3.of(2, 0, -1), 0.5f, Lambertian.of(Vec3.of(0.8f, 0.4f, 0.4f))));
        hitables.add(Sphere.of(Vec3.of(0, 0, -1), 0.5f, Lambertian.of(Vec3.of(0.4f, 0.8f, 0.4f))));

        hitables.add(Sphere.of(Vec3.of(-2, 0, -1), 0.5f, Metal.of(Vec3.of(0.4f, 0.4f, 0.8f), 0f)));
        hitables.add(Sphere.of(Vec3.of(2, 0, 1), 0.5f, Metal.of(Vec3.of(0.4f, 0.8f, 0.4f), 0f)));
        hitables.add(Sphere.of(Vec3.of(0, 0, 1), 0.5f, Metal.of(Vec3.of(0.4f, 0.8f, 0.4f), 0.2f)));
        hitables.add(Sphere.of(Vec3.of(-2, 0, 1), 0.5f, Metal.of(Vec3.of(0.4f, 0.8f, 0.4f), 0.6f)));

        hitables.add(Sphere.of(Vec3.of(0.5f, 1, 0.5f), 0.5f, Dielectric.of(1.5f)));
        hitables.add(Sphere.of(Vec3.of(-1.5f, 1.5f, 0f), 0.3f, Lambertian.of(Vec3.of(.3f, .25f, .15f))));
        Renderer renderer = new Jaytracer(100,
                Camera.of(
                        lookFrom,
                        lookAt,
                        Vec3.of(0f, 1f, 0f),
                        60, (float) width / (float) height,
                        .1f, 3
                ), HitableList.of(hitables)
        );

        DrawFramePanel panel = new DrawFramePanel(frame);

        frame.add(panel);

        resizeWindowToFitContent(frame);

        new Thread(() -> {
            try {
                int percent = 0;
                while (percent != 100) {
                    Stats stats = renderer.getStats();
                    percent = (int) (((float) stats.done / (float) stats.max) * 100f);
                    frame.setTitle(String.format("%s [%d%%]", TITLE, percent));
                    if (percent != 0)
                        System.out.println(String.format("Speed %d[r/s]", stats.rays));
                    Thread.sleep(TimeUnit.SECONDS.toMillis(1L));
                }
                ;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


        panel.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                int x = e.getX();
//                int y = e.getY();
//                ((Jaytracer) renderer).drawPixelOn(panel, x, height - y);
                initRenderThread(frame, renderer, panel);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
    }

    private static RenderLoop initRenderThread(JFrame frame, Renderer renderer, DrawFramePanel panel) {
        final RenderLoop renderLoop = new RenderLoop();
        renderLoop.setDrawableSurface(panel);
        renderLoop.setRenderer(renderer);
        renderLoop.setDisplay(frame);
        renderLoop.setMaxFPS(0);
        new Thread(renderLoop).start();
        return renderLoop;
    }

    private static void resizeWindowToFitContent(JFrame frame) {
        Dimension frameSize = frame.getSize();
        Dimension contentPaneSize = frame.getContentPane().getSize();
        frame.setSize(new Dimension(2 * frameSize.width - contentPaneSize.width, 2 * frameSize.height - contentPaneSize.height));
    }


    private static JFrame buildFrame(final int width, final int height) {
        JFrame frame = new JFrame();
        frame.setTitle(TITLE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(width, height));
        frame.setVisible(true);
        return frame;
    }


}