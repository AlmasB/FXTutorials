package com.almasb.explore;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class DraggableApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        Circle circle = new Circle(25, 25, 25, Color.YELLOW);
        Rectangle rect = new Rectangle(150, 30, Color.RED);
        Label control = new Label("Hello World");
        control.setFont(Font.font(42));

        circle.setTranslateX(50);
        circle.setTranslateY(50);

        rect.setTranslateX(150);
        rect.setTranslateY(50);

        control.setTranslateX(250);
        control.setTranslateY(150);

        var root = new Pane(circle, rect, control);
        root.setPrefSize(800, 600);

        root.getChildren().forEach(this::makeDraggable);

        return root;
    }

    private double startX;
    private double startY;

    private void makeDraggable(Node node) {

        // a = b - c
        // a + c = b
        // c = b - a

        node.setOnMousePressed(e -> {
            startX = e.getSceneX() - node.getTranslateX();
            startY = e.getSceneY() - node.getTranslateY();
        });

        node.setOnMouseDragged(e -> {
            node.setTranslateX(e.getSceneX() - startX);
            node.setTranslateY(e.getSceneY() - startY);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
