package com.almasb.parametric;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.*;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class DrawingApp extends Application {

    private GraphicsContext g;

    private double t = 0.0;
    private double oldX = 400, oldY = 300;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(800, 600);

        Canvas canvas = new Canvas(800, 600);
        g = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                t += 0.017;
                draw();
            }
        };
        timer.start();

        root.getChildren().add(canvas);
        return root;
    }

    private void draw() {
        Point2D p = curveFunction();

        g.setStroke(Color.BLUE);

        double newX = 400 + p.getX();
        double newY = 300 + p.getY();

        if (oldX != 400 && oldY != 300)
            g.strokeLine(oldX, oldY, newX, newY);

        //g.strokeOval(newX, newY, 1, 1);

        oldX = newX;
        oldY = newY;
    }

    private Point2D curveFunction() {
        double x = sin(t) * (pow(E, cos(t)) - 2 * cos(4*t) - pow(sin(t/12), 5));
        double y = cos(t) * (pow(E, cos(t)) - 2 * cos(4*t) - pow(sin(t/12), 5));

        return new Point2D(x, -y).multiply(85);
    }

    private void saveScreenshot(Scene scene) {
        WritableImage fxImage = scene.snapshot(null);

        BufferedImage awtImage = SwingFXUtils.fromFXImage(fxImage, null);

        try {
            ImageIO.write(awtImage, "png", new File("screenshot.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                saveScreenshot(scene);
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
