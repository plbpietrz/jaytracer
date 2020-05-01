package org.rhx.graphics.jaytracer.model.scene;

import org.rhx.graphics.jaytracer.Camera;
import org.rhx.graphics.jaytracer.model.HitableList;

public interface SceneDecription {

    HitableList getSceneDescription();

    Camera getCamera(int width, int height);

}
