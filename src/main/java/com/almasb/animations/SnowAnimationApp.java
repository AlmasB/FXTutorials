package com.almasb.animations;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class SnowAnimationApp extends Application {

    private static final String MESSAGE = "HELLO WORLD";

    private static final int NUM_PARTICLES = 52000;
    private static final Random RANDOM = new Random();

    // 0 - empty
    // 1 - collidable
    // 2 - snow
    private int[][] collisionMap = new int[1280][720];

    private List<SnowParticle> particles;

    private Canvas canvas;
    private GraphicsContext g;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        particles = new ArrayList<>();

        for (int i = 0; i < NUM_PARTICLES; i++) {
            particles.add(new SnowParticle(RANDOM.nextInt(1280), 0, Duration.millis(i*2)));
        }

        textToPixels();

        var root = new Pane();
        root.setPrefSize(1280, 720);

        canvas = new Canvas(1280, 720);
        g = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        var timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        return root;
    }

    private void textToPixels() {
        var params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        var text = new Text(MESSAGE);
        text.setFont(Font.font(100));
        var image = text.snapshot(params, null);

        for (int y = 0; y < (int) image.getHeight(); y++) {
            for (int x = 0; x < (int) image.getWidth(); x++) {
                var color = image.getPixelReader().getColor(x, y);

                if (!color.equals(Color.TRANSPARENT)) {
                    collisionMap[x + 300][y + 300] = 1;
                }
            }
        }
    }

    private void onUpdate() {
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, 1280, 720);
        g.setFill(Color.WHITE);

        particles.forEach(p -> {
            if (p.isMoving) {
                if (collisionMap[p.x][p.y] == 2) {
                    p.isMoving = false;

                    while (p.y > 0 && collisionMap[p.x][p.y] == 2) {
                        p.y--;
                    }

                    // for icicle like effects
                    //collisionMap[p.x][p.y] = 2;
                }

                // the chance on the right controls whether the snow particle stays and falls through
                if (collisionMap[p.x][p.y] == 1 && RANDOM.nextBoolean()) {
                    p.isMoving = false;
                    collisionMap[p.x][p.y] = 2;
                }
            }

            p.onUpdate();

            g.fillRect(p.x, p.y, 1, 1);

        });
    }

    private static class SnowParticle {
        int x;
        int y;
        Duration delay;
        double t = 0.0;
        boolean isMoving = true;

        SnowParticle(int x, int y, Duration delay) {
            this.x = x;
            this.y = y;
            this.delay = delay;
        }

        void onUpdate() {
            if (!isMoving)
                return;

            t += 0.016;

            if (t < delay.toSeconds()) {
                return;
            }

            y += RANDOM.nextInt(16);

            if (y >= 720) {
                x = RANDOM.nextInt(1280);
                y = 0;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
