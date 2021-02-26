package com.almasb.fallout4menu;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class LoadingScreen extends Pane {

    private Timeline timeline = new Timeline();

    public LoadingScreen(int width, int height, Runnable action) {
        ImageView bg = new ImageView(new Image(
                getClass().getResource("res/Fallout4_loading.jpg").toExternalForm()
        ));
        bg.setFitWidth(width);
        bg.setFitHeight(height);

        Node symbol = makeSymbol();
        symbol.setTranslateX(width - 150);
        symbol.setTranslateY(height - 100);

        timeline.setOnFinished(e -> action.run());

        getChildren().addAll(bg, symbol);
    }

    private Node makeSymbol() {
        Pane symbol = new Pane();

        GaussianBlur blur = new GaussianBlur(2.5);
        symbol.setEffect(blur);

        Rectangle top = new Rectangle(70, 5, Colors.LOADING_SYMBOL);
        top.setArcWidth(25);
        top.setArcHeight(25);

        Rectangle mid = new Rectangle(100, 5, Colors.LOADING_SYMBOL);
        mid.setArcWidth(25);
        mid.setArcHeight(25);

        Rectangle bot = new Rectangle(70, 5, Colors.LOADING_SYMBOL);
        bot.setArcWidth(25);
        bot.setArcHeight(25);

        top.setTranslateX(15);
        bot.setTranslateX(15);

        top.setTranslateY(10);
        mid.setTranslateY(10 + 10 + 5);
        bot.setTranslateY(10 + 10 + 5 + 10 + 5);

        Circle circle = new Circle(25, 25, 25, Color.BLACK);
        circle.setStroke(Colors.LOADING_SYMBOL);
        circle.setStrokeWidth(2);
        circle.setTranslateX(25);

        Circle circle2 = new Circle(25, 25, 25, Color.BLACK);
        circle2.setStroke(Colors.LOADING_SYMBOL);
        circle2.setStrokeWidth(1);
        circle2.setTranslateX(25);
        circle2.setRadius(2);

        Circle point = new Circle(25, 25, 25, Colors.LOADING_SYMBOL);
        point.setStroke(Colors.LOADING_SYMBOL);
        point.setStrokeWidth(1);
        point.setTranslateX(25);
        point.setRadius(1);

        KeyFrame frame = new KeyFrame(Duration.seconds(1),
                new KeyValue(circle2.radiusProperty(), 20));

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(5);
        timeline.play();

        symbol.getChildren().addAll(top, mid, bot, circle, circle2, point);
        return symbol;
    }
}
