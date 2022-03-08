package org.rhx.graphics.jaytracer.scene;

import org.rhx.graphics.jaytracer.Camera;
import org.rhx.graphics.jaytracer.core.HitableList;

public interface SceneDescription {

    HitableList getSceneDescription();

    Camera getCamera(int width, int height);

}
