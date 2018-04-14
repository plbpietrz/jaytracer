package org.rhx.window;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Main render loop. Can be run in a separate thread.
 * Created by plbpietrz on 2014-08-15.
 */
public class RenderLoop implements Render, Runnable {

    private static final int NOT_SET = -1;
    private static final int ONE_SECOND = 1000;

    private int maxFps = NOT_SET;
    private boolean renderFlag = true;

    private Renderer renderer;
    private Drawable surface;
    private Component display;

    @Override
    public void run() {
        renderer.init(surface);
        if (maxFps == 0) {
            render();
        } else if (maxFps == NOT_SET) {
            while (renderFlag) {
                render();
            }
        } else {
            try {
                long frameBudget = ONE_SECOND / maxFps, instant;
                while (renderFlag) {
                    instant = System.currentTimeMillis();
                    render();
                    instant = System.currentTimeMillis() - instant;
                    long duration = frameBudget - instant;
                    if (duration > 0)
                        Thread.sleep(TimeUnit.MILLISECONDS.toMillis(duration));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                stop();
            }
        }
    }

    @Override
    public void render() {
        renderer.drawOn(surface);
        display.repaint();
    }

    @Override
    public void setDrawableSurface(Drawable surface) {
        this.surface = surface;
    }

    @Override
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void setDisplay(Component display) {
        this.display = display;
    }

    /**
     * Stop render loop from drawing image.
     * @return this object
     */
    public RenderLoop stop() {
        this.renderFlag = false;
        return this;
    }

    /**
     * Set the target maximum frames per second.
     * @param fps frames per second
     * @return this object
     */
    public RenderLoop setMaxFPS(int fps) {
        this.maxFps = fps;
        return this;
    }

}
