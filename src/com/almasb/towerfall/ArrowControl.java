package com.almasb.towerfall;

import com.almasb.fxgl.entity.AbstractControl;
import com.almasb.fxgl.entity.Entity;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.transform.Rotate;

public class ArrowControl extends AbstractControl {

    private Point2D velocity;
    private Rectangle2D screenBounds;

    public ArrowControl(Point2D direction, Rectangle2D screenBounds) {
        velocity = direction.normalize().multiply(15);
        this.screenBounds = screenBounds;
    }

    @Override
    protected void initEntity(Entity entity) {

    }

    @Override
    public void onUpdate(Entity entity, long now) {
        entity.translate(velocity);
        velocity = velocity.add(0, 0.1);

        if (entity.getTranslateX() < 0) {
            entity.setTranslateX(screenBounds.getWidth());
        }

        if (entity.getTranslateX() > screenBounds.getWidth()) {
            entity.setTranslateX(0);
        }

        if (entity.getTranslateY() > screenBounds.getHeight()) {
            entity.setTranslateY(0);
        }

        if (entity.getTranslateY() < 0) {
            entity.setTranslateY(screenBounds.getHeight());
        }

        entity.getTransforms().setAll(new Rotate(Math.toDegrees(Math.atan2(velocity.getY(), velocity.getX())), 20, 4));
    }
}
