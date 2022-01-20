package com.almasb.basics;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayDeque;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class UndoApp extends Application {

    private ArrayDeque<UIAction> history = new ArrayDeque<>();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        VBox root = new VBox();
        root.setPrefSize(800, 600);

        var field = new TextField("");
        field.setFont(Font.font(22));

        var btnAdd = new Button("Add");
        btnAdd.setFont(Font.font(22));
        btnAdd.setOnAction(e -> {
            var action = new AddText(field.getText(), root);

            run(action);

            action.uiText.setOnMouseClicked(event ->
                    run(new RemoveText(action.uiText, root))
            );
        });

        var btnUndo = new Button("Undo");
        btnUndo.setOnAction(e -> undo());

        root.getChildren().addAll(field, btnAdd, btnUndo);

        return root;
    }

    private void run(UIAction action) {
        history.addLast(action);

        action.run();
    }

    private void undo() {
        if (history.isEmpty())
            return;

        var lastAction = history.removeLast();
        lastAction.undo();
    }

    class AddText implements UIAction {

        String text;
        Pane root;

        Text uiText;

        public AddText(String text, Pane root) {
            this.text = text;
            this.root = root;
        }

        @Override
        public void run() {
            uiText = new Text(text);
            uiText.setFont(Font.font(22));

            root.getChildren().add(uiText);
        }

        @Override
        public void undo() {
            root.getChildren().remove(uiText);
        }
    }

    class RemoveText implements UIAction {

        Text uiText;
        Pane root;

        public RemoveText(Text uiText, Pane root) {
            this.uiText = uiText;
            this.root = root;
        }

        @Override
        public void run() {
            root.getChildren().remove(uiText);
        }

        @Override
        public void undo() {
            root.getChildren().add(uiText);
        }
    }

    interface UIAction {
        void run();
        void undo();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
