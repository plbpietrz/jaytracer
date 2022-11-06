package org.rhx.window;

import java.util.Objects;

public class RenderStatistics {

    public final int pixelsRendered;
    public final int raysFromLastCheck;
    public final int pixelsToRender;
    public final long renderTime;

    public RenderStatistics(int pixelsRendered, int pixelsToRender, int raysFromLastCheck, long renderTime) {
        this.pixelsRendered = pixelsRendered;
        this.pixelsToRender = pixelsToRender;
        this.raysFromLastCheck = raysFromLastCheck;
        this.renderTime = renderTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenderStatistics that = (RenderStatistics) o;
        return pixelsRendered == that.pixelsRendered && raysFromLastCheck == that.raysFromLastCheck && pixelsToRender == that.pixelsToRender && renderTime == that.renderTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pixelsRendered, raysFromLastCheck, pixelsToRender, renderTime);
    }
}
