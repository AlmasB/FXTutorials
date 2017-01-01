package com.almasb.tutorial7;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private enum UserAction {
        NONE, LEFT, RIGHT
    }

    private static final int APP_W = 800;
    private static final int APP_H = 600;

    private static final int BALL_RADIUS = 10;
    private static final int BAT_W = 100;
    private static final int BAT_H = 20;

    private Circle ball = new Circle(BALL_RADIUS);
    private Rectangle bat = new Rectangle(BAT_W, BAT_H);

    private boolean ballUp = true, ballLeft = false;
    private UserAction action = UserAction.NONE;

    private Timeline timeline = new Timeline();
    private boolean running = true;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(APP_W, APP_H);

        bat.setTranslateX(APP_W / 2);
        bat.setTranslateY(APP_H - BAT_H);
        bat.setFill(Color.BLUE);

        KeyFrame frame = new KeyFrame(Duration.seconds(0.016), event -> {
            if (!running)
                return;

            switch (action) {
                case LEFT:
                    if (bat.getTranslateX() - 5 >= 0)
                        bat.setTranslateX(bat.getTranslateX() - 5);
                    break;
                case RIGHT:
                    if (bat.getTranslateX() + BAT_W + 5 <= APP_W)
                        bat.setTranslateX(bat.getTranslateX() + 5);
                    break;
                case NONE:
                    break;
            }

            ball.setTranslateX(ball.getTranslateX() + (ballLeft ? -5 : 5));
            ball.setTranslateY(ball.getTranslateY() + (ballUp ? -5 : 5));

            if (ball.getTranslateX() - BALL_RADIUS == 0)
                ballLeft = false;
            else if (ball.getTranslateX() + BALL_RADIUS == APP_W)
                ballLeft = true;

            if (ball.getTranslateY() - BALL_RADIUS == 0)
                ballUp = false;
            else if (ball.getTranslateY() + BALL_RADIUS == APP_H - BAT_H
                    && ball.getTranslateX() + BALL_RADIUS >= bat.getTranslateX()
                    && ball.getTranslateX() - BALL_RADIUS <= bat.getTranslateX() + BAT_W)
                ballUp = true;

            if (ball.getTranslateY() + BALL_RADIUS == APP_H)
                restartGame();
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);

        root.getChildren().addAll(ball, bat);
        return root;
    }

    private void restartGame() {
        stopGame();
        startGame();
    }

    private void stopGame() {
        running = false;
        timeline.stop();
    }

    private void startGame() {
        ballUp = true;
        ball.setTranslateX(APP_W / 2);
        ball.setTranslateY(APP_H / 2);

        timeline.play();
        running = true;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case A:
                    action = UserAction.LEFT;
                    break;
                case D:
                    action = UserAction.RIGHT;
                    break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case A:
                    action = UserAction.NONE;
                    break;
                case D:
                    action = UserAction.NONE;
                    break;
            }
        });

        primaryStage.setTitle("Tutorial");
        primaryStage.setScene(scene);
        primaryStage.show();
        startGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
