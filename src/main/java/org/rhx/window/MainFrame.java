package org.rhx.window;

import org.rhx.graphics.jaytracer.Jaytracer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Main frame class.
 */
public class MainFrame {

    public static final int WIDTH_PARAM = 0;
    public static final int HEIGHT_PARAM = 1;
    public static final String SOFTWARE_RENDER_WINDOW = "SRW";

    public static void main(String[] args) throws IOException {
        final int width;
        final int height;
        if (args.length == 2) {
            width = Integer.parseInt(args[WIDTH_PARAM]);
            height = Integer.parseInt(args[HEIGHT_PARAM]);
        } else {
            width = 800;
            height = 400;
        }

        final JFrame frame = buildFrame(width, height);
        final Renderer renderer = new Jaytracer();

        DrawFramePanel panel = new DrawFramePanel(frame);
        frame.add(panel);


        resizeWindowToFitContent(frame);
        final RenderLoop renderLoop = new RenderLoop();
        renderLoop.setDrawableSurface(panel);
        renderLoop.setRenderer(renderer);
        renderLoop.setDisplay(frame);
        renderLoop.setMaxFPS(60);
        new Thread(renderLoop).start();

        new Thread(() -> {
            try {
                while (true) {
                    frame.setTitle(SOFTWARE_RENDER_WINDOW + " FPS:" + renderLoop.getFrameCountAndReset());
                    Thread.sleep(TimeUnit.SECONDS.toMillis(1l));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void resizeWindowToFitContent(JFrame frame) {
        Dimension frameSize = frame.getSize();
        Dimension contentPaneSize = frame.getContentPane().getSize();
        frame.setSize(new Dimension(2 * frameSize.width - contentPaneSize.width, 2 * frameSize.height - contentPaneSize.height));
    }


    private static JFrame buildFrame(final int width, final int height) {
        JFrame frame = new JFrame();
        frame.setTitle(SOFTWARE_RENDER_WINDOW);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(width, height));
        frame.setVisible(true);
        return frame;
    }


}