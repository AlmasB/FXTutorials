package com.almasb.tutorial14;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameDialog extends Stage {

    private Text textQuestion = new Text();
    private TextField fieldAnswer = new TextField();
    private Text textActualAnswer = new Text();

    private boolean correct = false;

    public GameDialog() {
        Button btnSubmit = new Button("Submit");
        btnSubmit.setOnAction(event -> {
            fieldAnswer.setEditable(false);
            textActualAnswer.setVisible(true);
            correct = textActualAnswer.getText().equals(fieldAnswer.getText().trim());
        });

        VBox vbox = new VBox(10, textQuestion, fieldAnswer, textActualAnswer, btnSubmit);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox);

        setScene(scene);
        initModality(Modality.APPLICATION_MODAL);
    }

    public void open() {
        textQuestion.setText("What is 2x2?");
        fieldAnswer.setText("");
        fieldAnswer.setEditable(true);
        textActualAnswer.setText("4");
        textActualAnswer.setVisible(false);

        show();
    }

    public boolean isCorrect() {
        return correct;
    }
}
