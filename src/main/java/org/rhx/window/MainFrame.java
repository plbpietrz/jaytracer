package org.rhx.window;

import org.rhx.graphics.jaytracer.Jaytracer;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Main frame class.
 */
public class MainFrame {

    public static final int WIDTH_PARAM = 0;
    public static final int HEIGHT_PARAM = 1;
    public static final String TITLE = "Jaytracer";

    public static void main(String[] args) throws IOException {
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
        Renderer renderer = new Jaytracer();

        DrawFramePanel panel = new DrawFramePanel(frame);

        frame.add(panel);

        resizeWindowToFitContent(frame);

        initRenderThread(frame, renderer, panel);

        panel.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                ((Jaytracer) renderer).drawPixelOn(panel, x, height - y);
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {}
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