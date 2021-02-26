package com.almasb.mobile;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalTime;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class MobileLockScreenApp extends Application {

    private static final int NUM_ICONS_PER_ROW = 4;

    private LockScreenView lockScreen;

    private boolean isAnimating = false;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        lockScreen = new LockScreenView(800 * 9 / 16.0, 800);

        Pane root = new Pane();
        root.setPrefSize(800 * 9 / 16.0, 800);

        Rectangle bg = new Rectangle(root.getPrefWidth(), root.getPrefHeight(), Color.RED);
        root.getChildren().add(bg);

        for (int i = 0; i < 23; i++) {
            double x = 75 + i % NUM_ICONS_PER_ROW * 75;
            double y = 75 + i / NUM_ICONS_PER_ROW * 75;

            IconView view = new IconView(x, y);

            root.getChildren().add(view);
        }

        lockScreen.setTranslateY(-800);
        root.getChildren().add(lockScreen);

        bg.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                lockScreen();
            }
        });

        return root;
    }

    private void lockScreen() {
        if (isAnimating)
            return;

        isAnimating = true;

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.2), lockScreen);
        tt.setOnFinished(e -> isAnimating = false);
        tt.setFromY(-800);
        tt.setToY(0);
        tt.play();
    }

    private void openScreen() {
        if (isAnimating)
            return;

        isAnimating = true;

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.2), lockScreen);
        tt.setOnFinished(e -> isAnimating = false);
        tt.setFromY(0);
        tt.setToY(-800);
        tt.play();
    }

    private static class IconView extends Parent {
        IconView(double x, double y) {
            Rectangle bg = new Rectangle(50, 50, Color.color(0.3, 0.3, 0.5, 0.75));
            bg.setArcWidth(15);
            bg.setArcHeight(15);

            setTranslateX(x);
            setTranslateY(y);

            getChildren().add(bg);
        }
    }

    private class LockScreenView extends Parent {
        LockScreenView(double width, double height) {
            Rectangle bg = new Rectangle(width, height);

            Text textTime = new Text(LocalTime.now().toString());
            textTime.setFill(Color.WHITE);
            textTime.setFont(Font.font(36));
            textTime.setTranslateX(width / 2.0 - textTime.getLayoutBounds().getWidth() / 2.0);
            textTime.setTranslateY(height / 2.0 - textTime.getLayoutBounds().getHeight() / 2.0);

            bg.setOnMouseClicked(e -> openScreen());

            getChildren().addAll(bg, textTime);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
