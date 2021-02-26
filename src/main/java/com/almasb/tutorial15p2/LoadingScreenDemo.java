package com.almasb.tutorial15p2;

import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoadingScreenDemo extends Application {

    @FXML
    private RotateTransition loadingCircleAnimation;
    @FXML
    private ProgressBar progressBar;

    private ResourceLoadingTask task = new ResourceLoadingTask();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui.fxml"));
        loader.setController(this);

        Pane root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        progressBar.progressProperty().bind(task.progressProperty());

        loadingCircleAnimation.play();

        Thread t = new Thread(task);
        t.start();
    }

    private class ResourceLoadingTask extends Task<Void> {
        @Override
        protected Void call() throws Exception {
            for (int i = 0; i < 100; i++) {
                Thread.sleep((int)(Math.random() * 100));
                updateProgress(i + 1, 100);
            }

            return null;
        }

        @Override
        protected void succeeded() {
            loadingCircleAnimation.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
