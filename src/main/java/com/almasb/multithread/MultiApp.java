package com.almasb.multithread;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class MultiApp extends Application {

    /*

    Sequential

    J1 ------
    J2       ------

    Concurrency

    J1 --  --  --
    J2   --  --  --

    Parallelism

    J1 ------
    J2 ------

     */

    private List<JobCanvas> jobs;

    private AnimationTimer timer;

    private Parent createContent() {
        Pane root = new Pane();

        root.setPrefSize(800, 600);

        jobs = Arrays.asList(
                new JobCanvas(0, 0),
                new JobCanvas(400, 0),
                new JobCanvas(0, 300),
                new JobCanvas(400, 300)
        );

        root.getChildren().addAll(
                jobs
        );

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                concurrent();
                parallel();
            }
        };

        timer.start();

        return root;
    }

    private int jobIndex = 0;

    private void concurrent() {
        jobs.get(jobIndex).makeProgress();

        jobIndex++;

        if (jobIndex == 2) {
            jobIndex = 0;
        }
    }

    private void parallel() {
        jobs.get(2).makeProgress();
        jobs.get(3).makeProgress();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private static class JobCanvas extends Canvas {
        private GraphicsContext g;

        private boolean isDone = false;

        private double currentX = 0;
        private double currentY = 0;

        private static final double PIXELS_PER_PROGRESS = 4;

        public JobCanvas(int x, int y) {
            super(400, 300);

            setTranslateX(x);
            setTranslateY(y);

            g = getGraphicsContext2D();
            g.setFill(Color.BLACK);
        }

        public void makeProgress() {
            if (isDone)
                return;

            g.fillRect(currentX, currentY, PIXELS_PER_PROGRESS, PIXELS_PER_PROGRESS);

            currentX += PIXELS_PER_PROGRESS;

            if (currentX >= 400) {
                currentX = 0;

                currentY += PIXELS_PER_PROGRESS;

                if (currentY >= 300) {
                    isDone = true;
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
