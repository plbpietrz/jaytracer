package org.rhx.window;


/**
 * Renderer.
 * Created by plbpietrz on 2014-08-11.
 */
public interface Renderer {

    void init(Drawable drawable);

    /**
     * Draw operation.
     */
    void draw();

    Stats getStats();

}
