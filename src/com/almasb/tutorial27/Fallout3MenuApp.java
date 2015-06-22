package com.almasb.tutorial27;

import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Fallout3MenuApp extends Application {

    private enum Mode {
        STATS, ITEMS, DATA
    }

    private static final Font FONT = Font.font("", FontWeight.SEMI_BOLD, 18);

    private Pane root = new Pane();

    private Random random = new Random();
    private MenuPane menuStats, menuItems, menuData;
    private ModeBox mode;

    private Parent createContent() {

        root.setPrefSize(900, 600);

        mode = new ModeBox();
        mode.setTranslateX(350);
        mode.setTranslateY(600 - 100);

        menuStats = new MenuPane("STATS",
                new Tab("Status"),
                new Tab("SPECIAL"),
                new Tab("Skills"),
                new Tab("Perks"),
                new Tab("General"));

        menuItems = new MenuPane("ITEMS",
                new Tab("Weapons"),
                new Tab("Armor"),
                new Tab("Consumable"),
                new Tab("Quest"),
                new Tab("Misc"));

        menuData = new MenuPane("DATA",
                new Tab("D1"),
                new Tab("D2"),
                new Tab("D3"),
                new Tab("D4"),
                new Tab("D5"));

        root.getChildren().addAll(mode, menuStats);
        return root;
    }

    private static class MenuPane extends Parent {
        public MenuPane(String title, Tab... tabs) {
            Text text = new Text(title);
            text.setTranslateX(300);
            text.setTranslateY(50);
            text.setFont(FONT);


            Line top = new Line(0, 0, 700, 0);
            //Line bot = new Line(0, 400, 700, 400);
            Line left = new Line(0, 0, 0, 400);
            Line right = new Line(700, 0, 700, 400);



            int size = 0;
            for (Tab tab : tabs) {
                size += tab.getSize();
            }

            int lineLength = (700 - size) / (tabs.length + 1) - 6 * tabs.length;

            Line bot = new Line(0, 0, lineLength, 0);

            HBox hbox = new HBox(10, bot);
            hbox.setTranslateY(400);
            hbox.setAlignment(Pos.CENTER);
            for (Tab tab : tabs) {
                hbox.getChildren().addAll(new Text(tab.getTitle()),
                        new Line(0, 0, lineLength, 0));
            }

            getChildren().addAll(top, left, right, hbox, text);

            setTranslateX(100);
            setTranslateY(50);
        }
    }

    private static class Tab extends Parent {
        private String title;

        public Tab(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public int getSize() {
            return title.length() * 2;
        }
    }

    private class ModeBox extends HBox {
        public ModeBox() {
            super(15);
            for (Mode mode : Mode.values()) {
                Circle circle = new Circle(30);
                circle.setStroke(Color.GREY);
                circle.setFill(mode == Mode.STATS ? Color.BLACK : Color.TRANSPARENT);
                circle.setOnMouseClicked(event -> {
                    Parent node = null;
                    switch (mode) {
                        case DATA:
                            node = menuData;
                            break;
                        case ITEMS:
                            node = menuItems;
                            break;
                        case STATS:
                            node = menuStats;
                            break;
                    }

                    root.getChildren().set(1, node);
                    getChildren().stream()
                        .map(n -> (Circle)n)
                        .forEach(c -> c.setFill(Color.TRANSPARENT));

                    circle.setFill(Color.BLACK);
                });

                getChildren().add(circle);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
