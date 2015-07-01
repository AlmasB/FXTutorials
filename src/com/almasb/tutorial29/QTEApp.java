package com.almasb.tutorial29;

import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.event.QTEHandler;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class QTEApp extends GameApplication {

    private Entity player = Entity.noType();

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
    }

    @Override
    protected void initAssets() throws Exception {}

    @Override
    protected void initGame(Pane gameRoot) {
        qteManager.setColor(Color.RED);

        player.setPosition(100, 100).setGraphics(new Rectangle(20, 50));
        //addEntities(Entity.noType().setGraphics(new Rectangle(1280, 600)));
        addEntities(player);
    }

    @Override
    protected void initUI(Pane uiRoot) {}

    @Override
    protected void initInput() {
        addKeyTypedBinding(KeyCode.ENTER, () -> {
            qteManager.startQTE(2 * SECOND, new QTEHandler() {
                @Override
                public void onSuccess() {
                    System.out.println("Success");
                }

                @Override
                public void onFailure() {
                    System.out.println("Failure");
                }
            }, KeyCode.W, KeyCode.S, KeyCode.A);
        });
    }

    @Override
    protected void onUpdate(long now) {}

    public static void main(String[] args) {
        launch(args);
    }
}
