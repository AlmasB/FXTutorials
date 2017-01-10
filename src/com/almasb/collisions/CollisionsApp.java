package com.almasb.collisions;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CollisionsApp extends Application {

    private GraphicsContext g;

    private Entity e1, e2;

    private Parent createContent() {
        Pane root = new Pane();

        Canvas canvas = new Canvas(600, 600);
        g = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        e1 = new Entity(0, 0, 250, 40);
        e2 = new Entity(200, 300, 400, 40);

        e1.rotation = 75;

        render();

        return root;
    }

    private void render() {
        g.clearRect(0, 0, 600, 600);
        e1.draw(g);
        e2.draw(g);

        g.strokeText(e1.isCollidingSAT(e2) ? "Collision" : "NO Collision", 500, 50);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W) {
                e1.y -= 5;
            } else if (e.getCode() == KeyCode.S) {
                e1.y += 5;
            } else if (e.getCode() == KeyCode.A) {
                e1.x -= 5;
            } else if (e.getCode() == KeyCode.D) {
                e1.x += 5;
            }

            render();
        });

        primaryStage.setTitle("Collisions");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
