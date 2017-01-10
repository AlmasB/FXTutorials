package com.almasb.tutorialx1;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Tutorial extends Application {

    PerspectiveCamera camera;

    Translate translate;
    Rotate rotate;

    Group worldRoot = new Group();

    private Parent createContent() {
        Cube c = new Cube(1, Color.GREEN);
        c.setTranslateX(-1);
        c.setRotationAxis(Rotate.Y_AXIS);
        c.setRotate(45);

        Cube c2 = new Cube(1, Color.BLUE);
        c2.setTranslateX(1);
        c2.setRotationAxis(Rotate.Y_AXIS);
        c2.setRotate(45);

        Cube c3 = new Cube(1, Color.RED);
        c3.setRotationAxis(Rotate.Y_AXIS);
        c3.setRotate(45);

        camera = new PerspectiveCamera(true);
        translate = new Translate(0, 0, -10);
        rotate = new Rotate(0, new Point3D(0, 1, 0));
        camera.getTransforms().addAll(translate, rotate);

        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(3);
        light.setTranslateZ(-5);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), light);
        tt.setFromX(-3);
        tt.setToX(3);
        tt.setAutoReverse(true);
        tt.setCycleCount(Animation.INDEFINITE);

        AmbientLight globalLight = new AmbientLight(Color.WHITE.deriveColor(0, 1, 0.2, 1));


        worldRoot.getChildren().addAll(c, c2, c3, globalLight, light);

        SubScene subScene = new SubScene(worldRoot, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        tt.play();

        return new Group(new Rectangle(800, 600), subScene);
    }

    private void placeCube(Point3D point) {
        Random random = new Random();
        Cube cube = new Cube(1, Color.rgb(random.nextInt(150) + 100, random.nextInt(150) + 100, random.nextInt(250)));
        cube.setTranslateX(point.getX());
        cube.setTranslateY(point.getY());
        cube.setTranslateZ(point.getZ());
        worldRoot.getChildren().add(cube);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());

        placeCube(new Point3D(10, 0, 0));
        placeCube(new Point3D(-10, 0, 0));
        placeCube(new Point3D(0, 0, -20));

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W) {
                translate.setZ(translate.getZ() + 1);
            } else if (event.getCode() == KeyCode.S) {
                translate.setZ(translate.getZ() - 1);
            } else if (event.getCode() == KeyCode.A) {
                //rotate.setAngle(rotate.getAngle() - 5);
                translate.setX(translate.getX() - 1);
            } else if (event.getCode() == KeyCode.D) {
                //rotate.setAngle(rotate.getAngle() + 5);
                translate.setX(translate.getX() + 1);
            }

            if (event.getCode() == KeyCode.UP) {

            } else if (event.getCode() == KeyCode.DOWN) {

            } else if (event.getCode() == KeyCode.LEFT) {
                rotate.setAngle(rotate.getAngle() - 5);
            } else if (event.getCode() == KeyCode.RIGHT) {
                rotate.setAngle(rotate.getAngle() + 5);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    class Cube extends Box {

        public Cube(double size, Color color) {
            super(size, size, size);
            setMaterial(new PhongMaterial(color));
        }
    }
}
