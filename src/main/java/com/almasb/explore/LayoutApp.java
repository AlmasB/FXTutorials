package com.almasb.explore;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class LayoutApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        var btn1 = new Button("Button 1");
        btn1.setFont(Font.font(18));

        var btn2 = new Button("Button 2");
        btn2.setFont(Font.font(18));

        var btn3 = new Button("Button 3");
        btn3.setFont(Font.font(18));

        var root = new Pane(btn1, btn2, btn3);
        root.setPrefSize(800, 600);

        btn1.setLayoutX(50);
        btn1.setLayoutY(50);

        btn2.setLayoutX(400 - 20);
        btn2.setLayoutY(300 - 10);

        btn3.setLayoutX(800 - 40);
        btn3.setLayoutY(600 - 20);

        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}