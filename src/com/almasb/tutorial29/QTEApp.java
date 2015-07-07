package com.almasb.tutorial29;

import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityType;
import com.almasb.fxgl.event.QTEHandler;
import com.almasb.fxgl.physics.CollisionHandler;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class QTEApp extends GameApplication {

    private enum Type implements EntityType {
        PLAYER, BULLET
    }

    private Entity player;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setIntroEnabled(false);
    }

    @Override
    protected void initAssets() throws Exception {}

    @Override
    protected void initGame() {
        qteManager.setColor(Color.RED);

        player = new Entity(Type.PLAYER);
        player.setPosition(100, 50);
        player.setGraphics(new Rectangle(20, 50));
        player.setCollidable(true);

        addEntities(player);

        physicsManager.addCollisionHandler(new CollisionHandler(Type.PLAYER, Type.BULLET) {
            @Override
            public void onCollisionBegin(Entity a, Entity b) {
                qteManager.startQTE(2 * SECOND, new QTEHandler() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Success");
                        player.translate(0, 50);
                        removeEntity(b);
                    }

                    @Override
                    public void onFailure() {
                        System.out.println("Failure");
                        System.out.println("Player Died");
                        removeEntity(b);
                    }
                }, KeyCode.W, KeyCode.S, KeyCode.A);
            }

            @Override
            public void onCollision(Entity a, Entity b) {}

            @Override
            public void onCollisionEnd(Entity a, Entity b) {}
        });
    }

    @Override
    protected void initUI(Pane uiRoot) {}

    @Override
    protected void initInput() {}

    long timeShot = 0;

    @Override
    protected void onUpdate() {
        if (now - timeShot >= 5 * SECOND) {
            shoot();
            timeShot = now;
        }
    }

    private void shoot() {
        Entity bullet = new Entity(Type.BULLET);
        bullet.setPosition(500, player.getTranslateY() + 25);
        bullet.setGraphics(new Rectangle(10, 2));
        bullet.setCollidable(true);
        bullet.addControl((entity, now) -> {
            entity.translate(-5, 0);
        });

        addEntities(bullet);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
