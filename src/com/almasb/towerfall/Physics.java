package com.almasb.towerfall;

import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.FXGLEvent;
import com.almasb.towerfall.TowerFallApp.Type;

public class Physics {

    private GameApplication app;

    public Physics(GameApplication app) {
        this.app = app;
    }

    /**
     * Returns true iff entity has moved value units
     *
     * @param e
     * @param value
     * @return
     */
    public boolean moveX(Entity e, int value) {
        boolean movingRight = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Entity platform : app.getEntities(Type.PLATFORM)) {
                if (e.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if (e.getTranslateX() + e.getWidth() == platform.getTranslateX()) {
                            e.fireFXGLEvent(new FXGLEvent(Event.COLLIDED_PLATFORM, platform));
                            e.translate(-1, 0);
                            return false;
                        }
                    }
                    else {
                        if (e.getTranslateX() == platform.getTranslateX() + platform.getWidth()) {
                            e.fireFXGLEvent(new FXGLEvent(Event.COLLIDED_PLATFORM, platform));
                            e.translate(1, 0);
                            return false;
                        }
                    }
                }
            }
            e.setTranslateX(e.getTranslateX() + (movingRight ? 1 : -1));
        }

        return true;
    }

    public void moveY(Entity e, int value) {
        boolean movingDown = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Entity platform : app.getEntities(Type.PLATFORM)) {
                if (e.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if (e.getTranslateY() + e.getHeight() == platform.getTranslateY()) {
                            e.fireFXGLEvent(new FXGLEvent(Event.COLLIDED_PLATFORM, platform));
                            e.setTranslateY(e.getTranslateY() - 1);
                            e.setProperty("jumping", false);
                            return;
                        }
                    }
                    else {
                        if (e.getTranslateY() == platform.getTranslateY() + platform.getHeight()) {
                            e.fireFXGLEvent(new FXGLEvent(Event.COLLIDED_PLATFORM, platform));
                            return;
                        }
                    }
                }
            }
            e.setTranslateY(e.getTranslateY() + (movingDown ? 1 : -1));
            e.setProperty("jumping", true);
        }
    }
}
