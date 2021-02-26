package com.almasb.misc;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class ParticlesClockApp extends Application {

    private static final int ANIMATION_SECONDS = 4;

    private Random random = new Random();

    private Digit[] digits = new Digit[10];
    private int currentIndex = -1;

    private GraphicsContext g;
    private double time = 0;

    private List<Particle> particles = new ArrayList<>();
    private int maxParticles = 0;

    private Parent createContent() {

        Pane root = new Pane();
        root.setPrefSize(800, 600);

        Canvas canvas = new Canvas(800, 600);
        g = canvas.getGraphicsContext2D();
        g.setFill(Color.BLUE);

        root.getChildren().add(canvas);

        populateDigits();
        populateParticles();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        return root;
    }

    private void onUpdate() {
        g.clearRect(0, 0, 800, 600);

        if (time == 0 || time > ANIMATION_SECONDS) {
            currentIndex++;
            if (currentIndex == 10)
                currentIndex = 0;

            particles.parallelStream().forEach(p -> {
                if (p.index < digits[currentIndex].positions.size()) {
                    Point2D point = digits[currentIndex].positions.get(p.index);

                    // offset to center of screen
                    p.nextX = point.getX() + 350;
                    p.nextY = point.getY() + 150;
                } else {
                    if (random.nextBoolean()) {
                        // move horizontally
                        p.nextX = random.nextBoolean() ? 805 : -5;
                        p.nextY = random.nextInt(600);
                    } else {
                        // move vertically
                        p.nextX = random.nextInt(800);
                        p.nextY = random.nextBoolean() ? 605 : -5;
                    }
                }

                p.dx = p.nextX - p.x;
                p.dy = p.nextY - p.y;
                p.dx /= ANIMATION_SECONDS * 60 * random.nextDouble();
                p.dy /= ANIMATION_SECONDS * 60 * random.nextDouble();
            });

//            for (int i = 0; i < particles.size(); i++) {
//                Particle p = particles.get(i);
//
//                if (i < digits[currentIndex].positions.size()) {
//                    Point2D point = digits[currentIndex].positions.get(i);
//
//                    p.nextX = point.getX();
//                    p.nextY = point.getY();
//                } else {
//                    p.nextX = 0;
//                    p.nextY = 0;
//                }
//
//                p.dx = p.nextX - p.x;
//                p.dy = p.nextY - p.y;
//                p.dx /= ANIMATION_SECONDS * 60;
//                p.dy /= ANIMATION_SECONDS * 60;
//            }

            time = 0;
        }

        for (Particle p : particles) {
            p.update();
            p.render(g);
        }

        time += 0.016;
    }

    private void populateDigits() {
        for (int i = 0; i < digits.length; i++) {
            digits[i] = new Digit();

            Text text = new Text(i + "");
            text.setFont(Font.font(144));
            text.setFill(Color.BLACK);

            Image snapshot = text.snapshot(null, null);

            for (int y = 0; y < snapshot.getHeight(); y++) {
                for (int x = 0; x < snapshot.getWidth(); x++) {
                    if (snapshot.getPixelReader().getColor(x, y).equals(Color.BLACK)) {
                        digits[i].positions.add(new Point2D(x, y));
                    }
                }
            }

            if (digits[i].positions.size() > maxParticles) {
                maxParticles = digits[i].positions.size();
            }
        }
    }

    private void populateParticles() {
        for (int i = 0; i < maxParticles; i++) {
            particles.add(new Particle(i));
        }

        //Collections.shuffle(particles);
    }

    private static class Digit {
        private List<Point2D> positions = new ArrayList<>();
    }

    private static class Particle {
        private double x, y;
        private double nextX, nextY;
        private double dx, dy;
        private int index;

        public Particle(int index) {
            this.index = index;
        }

        void update() {
            if (Math.abs(nextX - x) > Math.abs(dx)) {
                x += dx;
            } else {
                x = nextX;
            }

            if (Math.abs(nextY - y) > Math.abs(dy)) {
                y += dy;
            } else {
                y = nextY;
            }
        }

        void render(GraphicsContext g) {
            Point2D center = new Point2D(350, 200);
            double alpha = 1 - new Point2D(x, y).distance(center) / 500;

            g.setGlobalAlpha(alpha);
            g.fillOval(x, y, 2, 2);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
