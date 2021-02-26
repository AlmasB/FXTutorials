package com.almasb.recursion;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class RecursionApp extends Application {

    private Pane root = new Pane();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        root.setPrefSize(1600, 900);

        addText("*", 440, 300, 0, 150);

        return root;
    }

    private void addText(String message, double x, double y, double angle, double fontSize) {
        if (fontSize < 5)
            return;

        Text text = new Text(message);
        text.setTranslateX(x);
        text.setTranslateY(y);
        text.setFont(Font.font(fontSize));
        //text.setRotate(angle);

        root.getChildren().add(text);

//        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.4), text);
//        tt.setFromX(800);
//        tt.setFromY(450);
//        tt.setToX(x);
//        tt.setToY(y);

        double delay = 1 - fontSize / 150;

//        tt.setDelay(Duration.seconds(delay * 2.5));
//        tt.play();

        ScaleTransition st = new ScaleTransition(Duration.seconds(0.4), text);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setDelay(Duration.seconds(delay * 7.5));
        st.play();

        Point2D vector = new Point2D(Math.cos(angle), Math.sin(angle)).multiply(fontSize * 4.5);

        char c = message.charAt(0);
        //c++;

        addText(String.valueOf(c), x + vector.getX(), y + vector.getY(), angle + 90, fontSize - 1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
