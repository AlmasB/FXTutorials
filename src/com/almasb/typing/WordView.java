package com.almasb.typing;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class WordView extends HBox {

    private char[] letters;
    private int correctLetters = 0;

    public WordView(String word) {
        letters = word.toUpperCase().toCharArray();

        for (char c : letters) {
            Text letter = new Text(c + "");
            letter.setFont(Font.font(108));

            getChildren().add(letter);
        }

        setAlignment(Pos.CENTER);
    }

    public void handleKeyPress(String letter) {
        if (isFinished())
            return;

        char c = letters[correctLetters];

        if (letter.charAt(0) == c) {
            getChildren().get(correctLetters).setVisible(false);
            correctLetters++;
        } else {
            // incorrectly typed
        }
    }

    public boolean isFinished() {
        return correctLetters == letters.length;
    }
}
