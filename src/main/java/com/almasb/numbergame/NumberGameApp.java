package com.almasb.numbergame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class NumberGameApp extends Application {

    private static final String[] WORDS = new String[] {
            "apple",
            "orange",
            "computer",
            "screen",
            "keyboard",
            "class",
            "file",
            "module",
            "record",
            "pattern"
    };

    private Text word = new Text();
    private TextField userInput = new TextField();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        nextWord();

        word.setFont(Font.font(30));
        userInput.setFont(Font.font(30));
        userInput.setOnAction(e -> guess(userInput.getText()));

        VBox box = new VBox(50, word, userInput);
        box.setPrefSize(800, 600);
        box.setAlignment(Pos.CENTER);

        return box;
    }

    private void nextWord() {
        var randomWord = WORDS[new Random().nextInt(WORDS.length)];

        var randomWordInNumbers = "";

        for (char c : randomWord.toCharArray()) {
            randomWordInNumbers += charToInt(c) + "_";
        }

        word.setText(randomWordInNumbers.substring(0, randomWordInNumbers.length() - 1));
        word.getProperties().put("word", randomWord);
    }

    private int charToInt(char c) {
        // a - 97
        // b - 98, and so on
        int i = c;

        return i - 96;
    }

    private void guess(String text) {
        userInput.clear();

        String actualWord = (String) word.getProperties().get("word");

        if (text.equalsIgnoreCase(actualWord)) {
            System.out.println("Correct!");
            nextWord();
        } else {
            System.out.println("Incorrect, try again!");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
