package com.almasb.tutorialx1;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Tutorial extends Application {

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

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().add(new Translate(0, 0, -10));

        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(3);
        light.setTranslateZ(-5);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), light);
        tt.setFromX(-3);
        tt.setToX(3);
        tt.setAutoReverse(true);
        tt.setCycleCount(Animation.INDEFINITE);

        AmbientLight globalLight = new AmbientLight(Color.WHITE.deriveColor(0, 1, 0.2, 1));

        Group root = new Group();
        root.getChildren().addAll(c, c2, c3, globalLight, light);

        SubScene subScene = new SubScene(root, 640, 480, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        tt.play();

        return new Group(new Rectangle(800, 600), subScene);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());

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
