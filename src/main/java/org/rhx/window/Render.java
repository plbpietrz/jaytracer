package org.rhx.window;


import java.awt.*;

/**
 * Basic rendering object. Contains logic required to render on {@link Drawable} surface.
 * Created by plbpietrz on 2014-08-11.
 */
public interface Render {

    /**
     * Set the current active drawing surface based on {@link Drawable}.
     *
     * @param surface {@link Drawable} surface on which the image will be drawn.
     */
    void setDrawableSurface(Drawable surface);

    /**
     * Set the active {@link Renderer} object that is suposed to draw on {@link Drawable} surface
     *
     * @param renderer {@link Renderer} instance
     */
    void setRenderer(Renderer renderer);

    /**
     * Renders the active frame.
     *
     */
    void render();

    /**
     * The actual {@link java.awt.Component} that will be responsible for displaying drawn image.
     * @param display {@link java.awt.Component} responsible for displaying the content
     */
    void setDisplay(Component display);
}
