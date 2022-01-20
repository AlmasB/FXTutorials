package com.almasb.basics;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class BasicApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        VBox root = new VBox();
        root.setPrefSize(600, 600);

        TextField input = new TextField();
        input.setFont(Font.font(18));

        Text output = new Text();
        output.setFont(Font.font(18));

        Button button = new Button("Press");
        button.setFont(Font.font(18));

        button.setOnAction(e -> {
            output.setText(input.getText());
        });

        root.getChildren().addAll(
                input, button, output
        );

        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
