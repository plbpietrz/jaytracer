package org.rhx.window;

import java.awt.*;
import java.awt.image.Raster;

/**
 * Object that contains a drawable plane in the sense of a {@link java.awt.image.Raster}.
 * Created by plbpietrz on 7/15/2014.
 */
public interface Drawable {

    /**
     * Return the drawable plane.
     * @return {@link java.awt.image.Raster}
     */
    Raster getDrawableRaster();

    /**
     * Get the size of the underlying drawing plane.
     * @return {@link java.awt.Dimension}
     */
    Dimension getDimension();
}
