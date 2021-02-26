package com.almasb.gc;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class GCApp extends Application {

    private static final int EDEN_SIZE = 10;
    private static final int SURVIVOR_SIZE = 10;
    private static final int OLD_GEN_SIZE = 25;
    private static final int AGE_THRESHOLD = 4;

    private HBox edenSpace = new HBox();
    private HBox s1Space = new HBox();
    private HBox s2Space = new HBox();
    private HBox oldSpace = new HBox();

    private Parent createContent() {
        VBox root = new VBox(10);
        root.setPrefSize(800, 600);
        root.setPadding(new Insets(50, 50, 50, 50));

        root.getChildren().addAll(makeToolbar(),
                new Label("EDEN"), edenSpace,
                new Label("S1"), s1Space,
                new Label("S2"), s2Space,
                new Label("OLD"), oldSpace);
        return root;
    }

    private ToolBar makeToolbar() {
        Button btnNewObject = new Button("New Object");
        btnNewObject.setOnAction(e -> newObject());

        Button btnRandomKill = new Button("Random Kill");
        btnRandomKill.setOnAction(e -> randomKill());

        Button btnMinorGC = new Button("Minor GC");
        btnMinorGC.setOnAction(e -> minorGC());

        Button btnMajorGC = new Button("Major GC");
        btnMajorGC.setOnAction(e -> majorGC());

        return new ToolBar(btnNewObject, btnRandomKill, btnMinorGC, btnMajorGC);
    }

    private void newObject() {
        if (edenSpace.getChildren().size() == EDEN_SIZE)
            minorGC();

        edenSpace.getChildren().add(new GCObject());
    }

    private void promoteObject(GCObject object) {
        if (oldSpace.getChildren().size() == OLD_GEN_SIZE)
            majorGC();

        oldSpace.getChildren().add(object);

        if (oldSpace.getChildren().size() > OLD_GEN_SIZE) {
            System.out.println("Out of memory");
        }
    }

    private void randomKill() {
        List<Node> nodes = new ArrayList<>();
        nodes.addAll(edenSpace.getChildren());
        nodes.addAll(s1Space.getChildren());
        nodes.addAll(s2Space.getChildren());
        nodes.addAll(oldSpace.getChildren());

        nodes.stream().map(n -> (GCObject)n).forEach(obj -> {
            if (obj.isAlive() && Math.random() < 0.3) {
                obj.markDead();
            }
        });
    }

    private HBox toSpace = s1Space;
    private HBox fromSpace = s2Space;

    private void minorGC() {
        System.out.println("Minor GC");

        Stream.concat(new ArrayList<>(edenSpace.getChildren()).stream(),
                new ArrayList<>(fromSpace.getChildren()).stream())
                .map(n -> (GCObject)n)
                .filter(GCObject::isAlive)
                .forEach(object -> {
                    object.mature();

                    if (object.getAge() == AGE_THRESHOLD) {
                        promoteObject(object);
                    } else {
                        if (toSpace.getChildren().size() == SURVIVOR_SIZE) {
                            promoteObject(object);
                        } else {
                            toSpace.getChildren().add(object);
                        }
                    }
                });

        edenSpace.getChildren().clear();
        fromSpace.getChildren().clear();

        // switch spaces
        HBox tmp = fromSpace;
        fromSpace = toSpace;
        toSpace = tmp;
    }

    private void majorGC() {
        System.out.println("Major GC");

        oldSpace.getChildren().removeIf(n -> !((GCObject)n).isAlive());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("GC");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
