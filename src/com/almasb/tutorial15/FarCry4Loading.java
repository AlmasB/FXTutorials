package com.almasb.tutorial15;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FarCry4Loading extends Application {

    private static final int APP_W = 800;
    private static final int APP_H = 600;

    private Line loadingBar = new Line();

    private ResourceLoadingTask task = new ResourceLoadingTask();

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(APP_W, APP_H);

        Rectangle bg = new Rectangle(APP_W, APP_H);

        LoadingCircle loadingCircle = new LoadingCircle();
        loadingCircle.setTranslateX(APP_W - 120);
        loadingCircle.setTranslateY(APP_H - 100);

        LoadingArc loadingArc = new LoadingArc();
        loadingArc.setTranslateX(500);
        loadingArc.setTranslateY(300);

        Line loadingBarBG = new Line(100, APP_H - 70, APP_W - 100, APP_H - 70);
        loadingBarBG.setStroke(Color.GREY);

        loadingBar.setStartX(100);
        loadingBar.setStartY(APP_H - 70);
        loadingBar.setEndX(100);
        loadingBar.setEndY(APP_H - 70);
        loadingBar.setStroke(Color.WHITE);

        task.progressProperty().addListener((obs, old, newValue) -> {
            double progress = newValue.doubleValue();
            loadingBar.setEndX(100 + progress * (APP_W - 200));
        });

        root.getChildren().addAll(bg, loadingArc, loadingBarBG, loadingBar);
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());

        primaryStage.setScene(scene);
        primaryStage.show();

//        Thread t = new Thread(task);
//        t.start();
    }

    private static class LoadingCircle extends Parent {
        private RotateTransition animation;

        public LoadingCircle() {
            Circle circle = new Circle(20);
            circle.setFill(null);
            circle.setStroke(Color.WHITE);
            circle.setStrokeWidth(2);

            Rectangle rect = new Rectangle(20, 20);

            Shape shape = Shape.subtract(circle, rect);
            shape.setFill(Color.WHITE);

            getChildren().add(shape);

            animation = new RotateTransition(Duration.seconds(2.5), this);
            animation.setByAngle(-360);
            animation.setInterpolator(Interpolator.LINEAR);
            animation.setCycleCount(Animation.INDEFINITE);
            animation.play();
        }
    }

    private static class LoadingArc extends Parent {
        public LoadingArc() {
            Arc arc = new Arc();

            arc.setCenterX(25);
            arc.setCenterY(25);
            arc.setRadiusX(25.0f);
            arc.setRadiusY(25.0f);
            arc.setLength(30.0f);
            arc.setStrokeWidth(5);

            Stop[] stops = new Stop[] { new Stop(0, Color.WHITE), new Stop(1, Color.BLUE)};
            LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);

            arc.setStroke(lg1);

            Rectangle rect = new Rectangle(50, 50);
            rect.setFill(null);
            rect.setStroke(Color.RED);

            getChildren().addAll(rect, arc);


            double time = 0.75;

            Rotate r = new Rotate(0, 25, 25);
            arc.getTransforms().add(r);
            //arc.getTransforms().add(new Scale(-1, 1, 25, 25));

            Timeline timeline = new Timeline();
            KeyFrame kf2 = new KeyFrame(Duration.seconds(time), new KeyValue(r.angleProperty(), 270));


            timeline.getKeyFrames().addAll(kf2);

            Timeline timeline3 = new Timeline(new KeyFrame(Duration.seconds(time), new KeyValue(r.angleProperty(), 360)));


            SequentialTransition st = new SequentialTransition(timeline, timeline3);
            st.setCycleCount(Timeline.INDEFINITE);
            st.setInterpolator(Interpolator.EASE_BOTH);
            st.play();

            //////////

            Timeline timeline2 = new Timeline();
            timeline2.setAutoReverse(true);
            timeline2.setCycleCount(Timeline.INDEFINITE);


            KeyFrame kf = new KeyFrame(Duration.seconds(time), new KeyValue(arc.lengthProperty(), 270, Interpolator.EASE_BOTH));

            timeline2.getKeyFrames().add(kf);
            timeline2.play();
        }
    }

    private class ResourceLoadingTask extends Task<Void> {
        @Override
        protected Void call() throws Exception {

            for (int i = 0; i < 100; i++) {
                Thread.sleep((int)(Math.random() * 100));
                updateProgress(i + 1, 100);
            }

            System.out.println("Resources loaded");
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
