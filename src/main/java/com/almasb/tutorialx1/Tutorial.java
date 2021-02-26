//package com.almasb.tutorialx1;
//
////import com.interactivemesh.jfx.importer.Importer;
////import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
//import javafx.animation.*;
//import javafx.application.Application;
//import javafx.geometry.Point2D;
//import javafx.geometry.Point3D;
//import javafx.scene.AmbientLight;
//import javafx.scene.Group;
//import javafx.scene.Parent;
//import javafx.scene.PerspectiveCamera;
//import javafx.scene.PointLight;
//import javafx.scene.Scene;
//import javafx.scene.SceneAntialiasing;
//import javafx.scene.SubScene;
//import javafx.scene.input.KeyCode;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.PhongMaterial;
//import javafx.scene.shape.Box;
//import javafx.scene.shape.CullFace;
//import javafx.scene.shape.MeshView;
//import javafx.scene.shape.Rectangle;
//import javafx.scene.text.Font;
//import javafx.scene.text.Text;
//import javafx.scene.transform.Rotate;
//import javafx.scene.transform.Translate;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class Tutorial extends Application {
//
//    PerspectiveCamera camera;
//
//    Translate translate;
//    Rotate rotate;
//    Rotate rotate2;
//
//    Group worldRoot = new Group();
//
//    private Group scooter;
//
//    private AnimationTimer timer;
//    private double angle = 0;
//
//    private ParallelTransition transition;
//
//    private Text t = new Text("JavaFX 3D Demo\nBGM: Presentation https://www.ashamaluevmusic.com\n" +
//            "3D Model: http://www.interactivemesh.org under CC 3.0");
//
//    private Parent createContent() {
//        placeCube(new Point3D(0, 6, 0));
//
//        Cube c = new Cube(1, Color.GREEN);
//        c.setTranslateX(-1);
//        c.setRotationAxis(Rotate.Y_AXIS);
//        //c.setRotate(45);
//
//
//        camera = new PerspectiveCamera(true);
//        //camera.setFieldOfView(45);
//        camera.setFarClip(1000);
//        translate = new Translate(0, -1.8, -13);
//        rotate = new Rotate(-25, Rotate.X_AXIS);
//        rotate2 = new Rotate(0, Rotate.Y_AXIS);
//        camera.getTransforms().addAll(translate, rotate2, rotate);
//
//        PointLight light = new PointLight(Color.WHITE);
//
//        //light.setTranslateX(3);
//        light.setTranslateY(-0.1);
//        light.setTranslateZ(-12.5);
//
//        PointLight light2 = new PointLight(Color.WHITE);
//
//        light2.setTranslateY(-0.1);
//        light2.setTranslateZ(12.5);
//
//
//        AmbientLight globalLight = new AmbientLight(Color.WHITE.deriveColor(0, 1, 0.2, 1));
//        globalLight.setTranslateY(-15);
//
//        worldRoot.getChildren().addAll(globalLight, light, light2);
//
//        SubScene subScene = new SubScene(worldRoot, 1280, 720, true, SceneAntialiasing.BALANCED);
//        subScene.setCamera(camera);
//
//
//
////        ObjModelImporter importer = new ObjModelImporter();
////        importer.read(new File("res/Scooter-smgrps.obj"));
////        MeshView[] views = importer.getImport();
//
//        //worldRoot.getChildren().add(views[0]);
//
//        scooter = new Group();
//
////        for (MeshView view : views) {
////            scooter.getChildren().add(view);
////        }
//
//        scooter.setTranslateY(1.0);
//
//
//
//
//
//        MeshView tire = importer.getNamedMeshViews().get("RimFront");
//        MeshView tire2 = importer.getNamedMeshViews().get("RimRear");
//
//        RotateTransition rt = new RotateTransition(Duration.seconds(0.5), tire);
//        rt.setByAngle(360);
//        rt.setInterpolator(Interpolator.LINEAR);
//        rt.setCycleCount(222);
//        rt.setAxis(Rotate.X_AXIS);
//        //rt.play();
//
//        RotateTransition rt2 = new RotateTransition(Duration.seconds(0.5), tire2);
//        rt2.setByAngle(360);
//        rt2.setInterpolator(Interpolator.LINEAR);
//        rt2.setCycleCount(222);
//        rt2.setAxis(Rotate.X_AXIS);
//        //rt2.play();
//
//        TranslateTransition tt = new TranslateTransition(Duration.seconds(3), scooter);
//        tt.setInterpolator(Interpolators.EXPONENTIAL.EASE_IN());
//        tt.setToZ(-15);
//
//        transition = new ParallelTransition(rt, rt2, tt);
//
//
//
//        importer.getNamedMeshViews().forEach((name, mesh) -> {
//            System.out.println(name + " : " + mesh);
//        });
//
//
//        //readCar();
//
//
//        timer = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                angle += 0.01;
//
//                double x = Math.cos(angle) * 4;
//                double z = Math.sin(angle) * 4;
//
//                translate.setX(x);
//                translate.setZ(z);
//
//
//                //rotate2.setAngle(90);
//                rotate2.setAngle(90-Math.toDegrees(Math.atan2(-z, -x)));
//            }
//        };
//        timer.start();
//
//
//        t.setTranslateY(60);
//        t.setFont(Font.font(36));
//        t.setOpacity(0);
//
//
//        return new Group(new Rectangle(1280, 720, Color.LIGHTBLUE), t, subScene);
//    }
//
//    private int index = 0;
//
//    private void startAnim(Group group) {
//        List<TranslateTransition> transitions = new ArrayList<>();
//
//        worldRoot.getChildren().add(group);
//
//        group.getChildren().forEach(n -> {
//            double x = (Math.random() * 10 - 5) * 7;
//            double y = -Math.random() * 10;
//            double z = (Math.random() * 10 - 5) * 7;
//
//            n.setTranslateX(x);
//            n.setTranslateY(y);
//            n.setTranslateZ(z);
//
//            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.66), n);
//            tt.setFromX(x);
//            tt.setFromY(y);
//            tt.setFromZ(z);
//            tt.setToX(0);
//            tt.setToY(0);
//            tt.setToZ(0);
//            tt.setDelay(Duration.seconds(0.66 * index++));
//
//            tt.setInterpolator(Interpolators.EXPONENTIAL.EASE_OUT());
//            //tt.setInterpolator(Interpolators.values()[(int)(Math.random() * Interpolators.values().length)].EASE_OUT());
//
//            transitions.add(tt);
//        });
//
//        transitions.get(transitions.size() - 1).setOnFinished(e -> transition.play());
//
//        transitions.forEach(t -> t.play());
//    }
//
//    private void readCar() {
//        ObjModelImporter importer = new ObjModelImporter();
//        importer.read(new File("res/models/Building.obj"));
//        MeshView[] views = importer.getImport();
//
//        Group building = new Group();
//        building.setScaleX(0.25);
//        building.setScaleY(0.25);
//        building.setScaleZ(0.25);
//
//        building.setTranslateX(2);
//        building.setTranslateY(205);
//        building.setTranslateZ(30);
//
//        for (MeshView view : views) {
//            view.setCullFace(CullFace.NONE);
//            building.getChildren().add(view);
//        }
//
//        importer.getNamedMeshViews().forEach((name, mesh) -> {
//            System.out.println(name + " : " + mesh);
//        });
//
//        worldRoot.getChildren().add(building);
//    }
//
//    private Cube worldCube;
//
//    private void placeCube(Point3D point) {
//        Random random = new Random();
//        worldCube = new Cube(10, Color.WHITESMOKE);
//        worldCube.setTranslateX(point.getX());
//        worldCube.setTranslateY(point.getY());
//        worldCube.setTranslateZ(point.getZ());
//        worldRoot.getChildren().add(worldCube);
//
//        worldCube.setOpacity(0);
//    }
//
//    private void showText() {
//        FadeTransition ft = new FadeTransition(Duration.seconds(2), t);
//        ft.setToValue(1.0);
//        ft.setOnFinished(e -> {
//            FadeTransition ft2 = new FadeTransition(Duration.seconds(2), t);
//            ft2.setToValue(0.0);
//            ft2.setOnFinished(ee -> {
//                FadeTransition ft3 = new FadeTransition(Duration.seconds(2), worldCube);
//                ft3.setToValue(1);
//                ft3.setOnFinished(e2 -> startAnim(scooter));
//                ft3.play();
//            });
//            ft2.play();
//        });
//        ft.play();
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        Scene scene = new Scene(createContent());
//
//        scene.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.W) {
//                translate.setZ(translate.getZ() + 1);
//            } else if (event.getCode() == KeyCode.S) {
//                translate.setZ(translate.getZ() - 1);
//            } else if (event.getCode() == KeyCode.A) {
//                //rotate.setAngle(rotate.getAngle() - 5);
//                translate.setX(translate.getX() - 1);
//            } else if (event.getCode() == KeyCode.D) {
//                //rotate.setAngle(rotate.getAngle() + 5);
//                translate.setX(translate.getX() + 1);
//            }
//
//            if (event.getCode() == KeyCode.UP) {
//
//            } else if (event.getCode() == KeyCode.DOWN) {
//                showText();
//            } else if (event.getCode() == KeyCode.LEFT) {
//                rotate.setAngle(rotate.getAngle() - 5);
//            } else if (event.getCode() == KeyCode.RIGHT) {
//                rotate.setAngle(rotate.getAngle() + 5);
//            }
//        });
//
//        new File("").toURI().toURL();
//
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    class Cube extends Box {
//
//        public Cube(double size, Color color) {
//            super(size, size, size);
//            setMaterial(new PhongMaterial(color));
//        }
//    }
//}
