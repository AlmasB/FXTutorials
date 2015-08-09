package com.almasb.towerfall;

import com.almasb.fxgl.entity.AbstractControl;
import com.almasb.fxgl.entity.Entity;

import javafx.geometry.Point2D;

/**
 * Allows moving an entity with physics collision
 * rules, including gravity effect
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 * @version 1.0
 *
 */
public class PhysicsControl extends AbstractControl {

    private Physics physics;
    private Point2D velocity;

    public PhysicsControl(Physics physics) {
        this(physics, 0);
    }

    public PhysicsControl(Physics physics, int y) {
        this.physics = physics;
        velocity = new Point2D(0, y);
    }

    @Override
    protected void initEntity(Entity entity) {
        entity.setProperty("jumping", false);
        entity.setProperty("g", true);
    }

    @Override
    public void onUpdate(Entity entity, long now) {
        if (entity.<Boolean>getProperty("g")) {
            velocity = velocity.add(0, 1);
            if (velocity.getY() > 10)
                velocity = new Point2D(velocity.getX(), 10);

            physics.moveY(entity, (int)velocity.getY());
        }
    }

    public boolean moveX(int value) {
        return physics.moveX(entity, value);
    }

    public void moveY(int value) {
        physics.moveY(entity, value);
    }

    public void jump() {
        if (entity.<Boolean>getProperty("jumping"))
            return;

        entity.setProperty("jumping", true);
        velocity = velocity.add(0, -35);
    }

    public Point2D getVelocity() {
        return velocity;
    }
}
