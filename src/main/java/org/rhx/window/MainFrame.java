package org.rhx.window;

import org.rhx.graphics.jaytracer.Jaytracer;
import org.rhx.graphics.jaytracer.scene.RandomBallsScene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

public class MainFrame extends JFrame {

    private static final Logger LOG = LoggerFactory.getLogger(MainFrame.class);

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    public static final String TITLE = "Jaytracer";


    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        JPanel drawPanel = new JPanel() {
            //init panel size
            {setSize(image.getWidth(), image.getHeight());}

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };

        Jaytracer renderer = getJaytracer(image);
        renderer.onRenderFinish(drawPanel::repaint);

        drawPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                new Thread(() -> renderer.draw(new SimpleTestScene())).start();
//                new Thread(() -> renderer.draw(Static9BallsScene.get())).start();
                new Thread(() -> renderer.draw(RandomBallsScene.get())).start();
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

        MainFrame mainFrame = new MainFrame();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.add(drawPanel);
        mainFrame.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.pack();
        mainFrame.setTitle(TITLE);

        new Thread(() -> {
            try {
                int percent = 0;
                while (percent != 100) {
                    Stats stats = renderer.getStats();
                    if (stats == null)
                        break;
                    percent = (int) (((float) stats.done / (float) stats.max) * 100f);
                    mainFrame.setTitle(String.format("%s [%d%%]", TITLE, percent));
                    if (percent != 0)
                        LOG.info("Speed {}[r/s]", stats.rays);
                    TimeUnit.SECONDS.sleep(1L);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        mainFrame.setVisible(true);
    }

    private static Jaytracer getJaytracer(BufferedImage image) {
        return new Jaytracer(50, image);
    }

}
