package com.almasb.tutorial17;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private WheelMenu menu = new WheelMenu();

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(1280, 720);

        menu.setTranslateX(600);
        menu.setTranslateY(300);
        menu.setScaleX(2);
        menu.setScaleY(2);

        menu.selectedItem.addListener((obs, old, newValue) -> {
            System.out.println("Selected: " + newValue);
        });

        try (InputStream is = getClass().getResourceAsStream("farcry_gameplay.jpg")) {
            Image img = new Image(is);
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(1280);
            imgView.setFitHeight(720);
            root.getChildren().add(imgView);
        }
        catch (Exception e) {
            System.out.println("Failed to load image: " + e.getMessage());
        }

        root.getChildren().addAll(menu);
        return root;
    }

    private class WheelMenu extends Parent {
        public SimpleStringProperty selectedItem = new SimpleStringProperty();

        public WheelMenu() {
            EventHandler<? super MouseEvent> handler = event -> {
                QuarterCircle source = (QuarterCircle) event.getSource();
                selectedItem.set(source.text.getText());
                menu.setVisible(false);
            };

            QuarterCircle circle = new QuarterCircle("PRIMARY");
            circle.setTranslateY(0);
            circle.setOnMouseClicked(handler);

            QuarterCircle circle2 = new QuarterCircle("SECONDARY");
            circle2.setTranslateX(50);
            circle2.setTranslateY(50);
            circle2.setRotate(90);
            circle2.setOnMouseClicked(handler);

            QuarterCircle circle3 = new QuarterCircle("PROJECTILE");
            circle3.setTranslateY(100);
            circle3.setRotate(180);
            circle3.text.setRotate(180);
            circle3.setOnMouseClicked(handler);

            QuarterCircle circle4 = new QuarterCircle("MEDICAL KIT");
            circle4.setTranslateX(-50);
            circle4.setTranslateY(50);
            circle4.setRotate(-90);
            circle4.setOnMouseClicked(handler);

            setOpacity(0.9);

            getChildren().addAll(circle, circle2, circle3, circle4);
        }
    }

    private class QuarterCircle extends StackPane {
        private Shape shape;
        private Text text;

        public QuarterCircle(String name) {
            Circle circle = new Circle(50);
            circle.setFill(null);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(30);

            Rectangle rect = new Rectangle(200, 100);
            rect.setTranslateX(-100);

            shape = Shape.subtract(circle, rect);

            rect = new Rectangle(100, 100);
            rect.setTranslateY(-100);

            shape = Shape.subtract(shape, rect);
            shape.setRotate(45);
            shape.setStroke(Color.BLACK);

            text = new Text(name);
            text.setFill(Color.WHITE);

            setOpacity(0.6);

            getChildren().addAll(shape, text);

            LinearGradient gradient = new LinearGradient(1, 1, 0.2, 0.2, true, CycleMethod.NO_CYCLE, new Stop[] {
                    new Stop(0.3, Color.GOLD),
                    new Stop(0.9, Color.BLACK)
            });

            DropShadow shadow = new DropShadow(5, Color.WHITE);
            shadow.setInput(new Glow(0.8));

            setOnMouseEntered(event -> {
                setOpacity(0.9);
                shape.setFill(gradient);
                text.setFont(Font.font("", FontPosture.ITALIC, 12));
                text.setEffect(shadow);
            });
            setOnMouseExited(event -> {
                setOpacity(0.6);
                shape.setFill(Color.BLACK);
                text.setFont(Font.getDefault());
                text.setEffect(null);
            });
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnKeyTyped(event -> {
            if (event.getCharacter().equals("q")) {
                menu.setVisible(!menu.isVisible());
            }
        });
        primaryStage.setTitle("Tutorial");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
