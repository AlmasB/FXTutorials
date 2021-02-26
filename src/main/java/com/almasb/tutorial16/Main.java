package com.almasb.tutorial16;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// from tutorial 11
public class Main extends Application {

    private Text info = new Text();
    private Entity player, enemy, coin;

    private List<Entity> objects = new ArrayList<Entity>();

    private List<CollisionPair> registeredCollisions = new ArrayList<CollisionPair>();

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(1280, 720);

        info.setTranslateX(50);
        info.setTranslateY(50);

        player = new Entity(400, 400, 30, 60, Color.GREEN);
        enemy = new Entity(300, 300, 40, 40, Color.RED);
        coin = new Entity(300, 400, 20, 20, Color.YELLOW);

        player.setUserData("Player");
        enemy.setUserData("Enemy");
        coin.setUserData("Coin");

        objects.addAll(Arrays.asList(player, enemy, coin));

        registeredCollisions.add(new CollisionPair("Player", "Enemy", () -> {
            info.setText("Got hit by enemy");
        }));

        registeredCollisions.add(new CollisionPair("Player", "Coin", () -> {
            info.setText("Picked up a coin");
        }));

        root.getChildren().addAll(info, player, enemy, coin);
        return root;
    }

    private void checkCollisions() {
        for (int i = 0; i < objects.size(); i++) {
            Entity a = objects.get(i);
            if (a.getUserData() == null) {
                continue;
            }

            String typeA = (String) a.getUserData();
            for (int j = i + 1; j < objects.size(); j++) {
                Entity b = objects.get(j);
                if (b.getUserData() == null) {
                    continue;
                }

                String typeB = (String) b.getUserData();

                for (CollisionPair pair : registeredCollisions) {
                    if (pair.equals(typeA, typeB)) {
                        if (a.getBoundsInParent().intersects(b.getBoundsInParent())) {
                            pair.handler.run();
                        }
                        break;
                    }
                }
            }
        }
    }

    private static class CollisionPair {
        public String typeA, typeB;
        public Runnable handler;

        public CollisionPair(String typeA, String typeB, Runnable handler) {
            this.typeA = typeA;
            this.typeB = typeB;
            this.handler = handler;
        }

        public boolean equals(String typeA, String typeB) {
            return (this.typeA.equals(typeA) && this.typeB.equals(typeB))
                    || (this.typeA.equals(typeB) && this.typeB.equals(typeA));
        }
    }

    private static class Entity extends Parent {
        public Entity(double x, double y, double w, double h, Color c) {
            setTranslateX(x);
            setTranslateY(y);
            Rectangle rect = new Rectangle(w, h);
            rect.setFill(c);
            getChildren().add(rect);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(event -> {
            //System.out.println(event);

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

            checkCollisions();
        });

        primaryStage.setTitle("Tutorial");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
