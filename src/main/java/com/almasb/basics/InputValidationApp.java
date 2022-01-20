package com.almasb.basics;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.function.Predicate;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class InputValidationApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        var field = new ValidatingTextField(input -> input.matches("\\d+"));
        field.setFont(Font.font(22));

        var btn = new Button("Submit");
        btn.setFont(Font.font(22));
        btn.disableProperty().bind(field.isValidProperty.not());

        return new VBox(field, btn);
    }

    private static class ValidatingTextField extends TextField {
        private final Predicate<String> validation;
        private BooleanProperty isValidProperty = new SimpleBooleanProperty();

        ValidatingTextField(Predicate<String> validation) {
            this.validation = validation;

            textProperty().addListener((o, oldValue, newText) -> {
                isValidProperty.set(validation.test(newText));
            });

            isValidProperty.set(validation.test(""));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
