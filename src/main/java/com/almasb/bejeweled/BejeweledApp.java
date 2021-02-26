package com.almasb.bejeweled;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BejeweledApp extends Application {

    private static final int W = 6;
    private static final int H = 6;
    private static final int SIZE = 100;

    private Color[] colors = new Color[] {
            Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW
    };

    private Jewel selected = null;
    private List<Jewel> jewels;

    private IntegerProperty score = new SimpleIntegerProperty();

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(W * SIZE + 150, H * SIZE);

        jewels = IntStream.range(0, W * H)
                .mapToObj(i -> new Point2D(i % W, i / W))
                .map(Jewel::new)
                .collect(Collectors.toList());

        root.getChildren().addAll(jewels);

        Text textScore = new Text();
        textScore.setTranslateX(W * SIZE);
        textScore.setTranslateY(100);
        textScore.setFont(Font.font(68));
        textScore.textProperty().bind(score.asString("Score: [%d]"));

        root.getChildren().add(textScore);
        return root;
    }

    private void checkState() {
        Map<Integer, List<Jewel>> rows = jewels.stream().collect(Collectors.groupingBy(Jewel::getRow));
        Map<Integer, List<Jewel>> columns = jewels.stream().collect(Collectors.groupingBy(Jewel::getColumn));

        rows.values().forEach(this::checkCombo);
        columns.values().forEach(this::checkCombo);
    }

    private void checkCombo(List<Jewel> jewelsLine) {
        Jewel jewel = jewelsLine.get(0);
        long count = jewelsLine.stream().filter(j -> j.getColor() != jewel.getColor()).count();
        if (count == 0) {
            score.set(score.get() + 1000);
            jewelsLine.forEach(Jewel::randomize);
        }
    }

    private void swap(Jewel a, Jewel b) {
        Paint color = a.getColor();
        a.setColor(b.getColor());
        b.setColor(color);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    private class Jewel extends Parent {
        private Circle circle = new Circle(SIZE / 2);

        public Jewel(Point2D point) {
            circle.setCenterX(SIZE / 2);
            circle.setCenterY(SIZE / 2);
            circle.setFill(colors[new Random().nextInt(colors.length)]);

            setTranslateX(point.getX() * SIZE);
            setTranslateY(point.getY() * SIZE);
            getChildren().add(circle);

            setOnMouseClicked(event -> {
                if (selected == null) {
                    selected = this;
                }
                else {
                    swap(selected, this);
                    checkState();
                    selected = null;
                }
            });
        }

        public void randomize() {
            circle.setFill(colors[new Random().nextInt(colors.length)]);
        }

        public int getColumn() {
            return (int)getTranslateX() / SIZE;
        }

        public int getRow() {
            return (int)getTranslateY() / SIZE;
        }

        public void setColor(Paint color) {
            circle.setFill(color);
        }

        public Paint getColor() {
            return circle.getFill();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
