package org.rhx.window;

import org.rhx.graphics.jaytracer.Jaytracer;
import org.rhx.graphics.jaytracer.scene.SceneDescription;
import org.rhx.graphics.jaytracer.scene.Static9BallsScene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainFrame extends JFrame {

    private static final Logger LOG = LoggerFactory.getLogger(MainFrame.class);

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    public static final String TITLE = "Jaytracer";

    private static final SceneDescription SCENE_DESCRIPTION = Static9BallsScene.get();

    public static void main(String[] args) {
        Map<Option, Object> options = Option.parseArgs(args);

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Jaytracer renderer = getJaytracer(image);

        if (options.containsKey(Option.BENCHMARK)) {
            LOG.info("Benchmark mode: ON");
            benchmark(options, renderer);
            return;
        }

        MainFrame mainFrame = new MainFrame();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.add(getDrawPanel(image, renderer));
        mainFrame.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.pack();
        mainFrame.setTitle(TITLE);

        new Thread(() -> statsLoop(renderer, mainFrame)).start();

        mainFrame.setVisible(true);
    }

    private static void statsLoop(Jaytracer renderer, MainFrame mainFrame) {
        try {
            RenderStatistics currentRenderStats, previousRenderStats = null;
            while (true) {
                currentRenderStats = renderer.getStats();

                int percent = (int) (((float) currentRenderStats.pixelsRendered / (float) currentRenderStats.pixelsToRender) * 100f);
                mainFrame.setTitle(String.format("%s [%d%%]", TITLE, percent));

                if (!currentRenderStats.equals(previousRenderStats)) {
                    if (percent != 0)
                        LOG.info("Speed {}[r/s]", currentRenderStats.raysFromLastCheck);
                    if (currentRenderStats.pixelsRendered == currentRenderStats.pixelsToRender)
                        LOG.info("Frame time is {}[ms]", currentRenderStats.renderTime);
                    previousRenderStats = currentRenderStats;
                }

                TimeUnit.SECONDS.sleep(1L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static JPanel getDrawPanel(BufferedImage image, Jaytracer renderer) {
        JPanel drawPanel = new JPanel() {

            {
                BufferedImage image = renderer.getImage();
                setSize(image.getWidth(), image.getHeight());
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };

        renderer.onRenderFinish(drawPanel::repaint);

        drawPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Thread(() -> renderer.draw(SCENE_DESCRIPTION)).start();
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
        });
        return drawPanel;
    }

    private static void benchmark(Map<Option, Object> options, Jaytracer renderer) {
        Integer count = (Integer) options.get(Option.BENCHMARK);
        count = count == null ? 1 : count;
        int iteration = 1;
        while (count-- > 0) {
            LOG.info("Iteration: {}", iteration++);
            renderer.draw(SCENE_DESCRIPTION);
        }
    }

    private static Jaytracer getJaytracer(BufferedImage image) {
        return new Jaytracer(50, image);
    }

}
