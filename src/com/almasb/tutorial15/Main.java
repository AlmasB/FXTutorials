package com.almasb.tutorial15;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private ResourceLoadingTask resourceLoader = new ResourceLoadingTask();
    private LoadingBar progress = new LoadingBar();

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(800, 600);

        progress.setTranslateX(700);
        progress.setTranslateY(500);

        resourceLoader.valueProperty().addListener((obs, old, newValue) -> {
            root.getChildren().add(newValue);
        });

        root.getChildren().add(progress);
        return root;
    }

    private class LoadingBar extends Parent {

        private RotateTransition rt;

        public LoadingBar() {
            Circle outer = new Circle(50);
            outer.setFill(null);
            outer.setStroke(Color.BLACK);

            Circle inner = new Circle(5);
            inner.setTranslateY(-50);

            rt = new RotateTransition(Duration.seconds(2), this);
            rt.setToAngle(360);
            rt.setInterpolator(Interpolator.LINEAR);
            rt.setCycleCount(RotateTransition.INDEFINITE);

            getChildren().addAll(outer, inner);
            setVisible(false);
        }

        public void show() {
            setVisible(true);
            rt.play();
        }

        public void hide() {
            rt.stop();
            setVisible(false);
        }
    }

    private class ResourceLoadingTask extends Task<Node> {
        @Override
        protected Node call() throws Exception {
            Platform.runLater(progress::show);

            for (int i = 0; i < 100; i++) {
                Thread.sleep((int)(Math.random() * 50));
            }

            Rectangle rect = new Rectangle(200, 50);

            System.out.println("Resources loaded");
            return rect;
        }

        @Override
        protected void succeeded() {
            progress.hide();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Tutorial 15 Resource Loading");
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(resourceLoader).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
