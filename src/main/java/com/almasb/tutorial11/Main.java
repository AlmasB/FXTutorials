package com.almasb.tutorial11;

import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private Text info = new Text();
    private Entity player, enemy;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(1280, 720);

        info.setTranslateX(50);
        info.setTranslateY(50);

        player = new Entity(400, 400, 30, 60, Color.GREEN);
        enemy = new Entity(300, 300, 40, 40, Color.RED);

        root.getChildren().addAll(info, player, enemy);
        return root;
    }

    private void checkCollisionAuto(Entity a, Entity b) {
        if (a.getBoundsInParent().intersects(b.getBoundsInParent())) {
            info.setText("Collision");
        }
        else {
            info.setText("");
        }
    }

    private void checkCollisionManual(Entity a, Entity b) {
        double minX = a.getTranslateX();
        double minY = a.getTranslateY();
        double maxX = minX + a.width;
        double maxY = minY + a.height;

        double minX2 = b.getTranslateX();
        double minY2 = b.getTranslateY();
        double maxX2 = minX2 + b.width;
        double maxY2 = minY2 + b.height;

        if (maxX >= minX2 && minX <= maxX2
                && maxY >= minY2 && minY <= maxY2) {
            info.setText("Collision");
        }
        else {
            info.setText("");
        }
    }

    private static class Entity extends Parent {
        double width, height;

        public Entity(double x, double y, double w, double h, Color c) {
            setTranslateX(x);
            setTranslateY(y);
            width = w;
            height = h;
            Rectangle rect = new Rectangle(w, h);
            rect.setFill(c);
            getChildren().add(rect);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W) {
                player.setTranslateY(player.getTranslateY() - 7);
            }
            else if (event.getCode() == KeyCode.S) {
                player.setTranslateY(player.getTranslateY() + 7);
            }
            else if (event.getCode() == KeyCode.A) {
                player.setTranslateX(player.getTranslateX() - 7);
            }
            else if (event.getCode() == KeyCode.D) {
                player.setTranslateX(player.getTranslateX() + 7);
            }

            //checkCollisionAuto(player, enemy);
            checkCollisionManual(player, enemy);
        });

        primaryStage.setTitle("Tutorial");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
