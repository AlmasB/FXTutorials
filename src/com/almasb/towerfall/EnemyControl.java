package com.almasb.towerfall;

import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.entity.AbstractControl;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.FXGLEvent;
import com.almasb.fxgl.time.TimerManager;

import javafx.geometry.Rectangle2D;

public class EnemyControl extends AbstractControl {

    private Entity target;
    private PhysicsControl control;

    private long timeSwitchedX = 0;
    private int dirX = -1;

    private long timeShot = 0;

    private Rectangle2D screenBounds;

    public EnemyControl(Entity target) {
        this.target = target;
        this.screenBounds = TowerFallApp.getInstance().getScreenBounds();
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    @Override
    protected void initEntity(Entity entity) {
        control = entity.getControl(PhysicsControl.class);
    }

    @Override
    public void onUpdate(Entity entity, long now) {
        double distance = target.getCenter().distance(entity.getCenter());


        if (now - timeSwitchedX >= TimerManager.SECOND) {
            int dx = (int)Math.signum(target.getTranslateX() - entity.getTranslateX());

            if (target.getCenter().distance(entity.getCenter()) > screenBounds.getWidth() / 4 + Math.random() * (screenBounds.getWidth() / 4))
                dirX = dx;
            else
                dirX = -dx;

            timeSwitchedX = now;
        }

        control.moveX(dirX * 5);

        if (Math.random() < 0.03)
            control.jump();

        if (Math.random() < (1 - distance / screenBounds.getWidth())
                && now - timeShot >= TimerManager.SECOND * 0.5) {
            entity.fireFXGLEvent(new FXGLEvent(Event.SHOOTING));
            timeShot = now;
        }





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
    }
}
