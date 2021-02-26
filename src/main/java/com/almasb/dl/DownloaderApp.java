package com.almasb.dl;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DownloaderApp extends Application {

    private ExecutorService service = Executors.newCachedThreadPool();

    private Parent createContent() {
        VBox root = new VBox();
        root.setPrefSize(400, 600);

        TextField fieldURL = new TextField();
        root.getChildren().addAll(fieldURL);

        fieldURL.setOnAction(event -> {

            // 2 jobs: 2x6 ticks
            // Sequential
            // ------------

            // Concurrency
            // 1: --  --  --
            // 2:   --  --  --

            // Parallelism
            // 1: ------
            // 2: ------

            IntStream.rangeClosed(1, 3)
                    .parallel()
                    .forEach(index -> {
                        doHeavyWork();

                        System.out.println("Completed on core: " + index);
                    });

            //service.submit(this::doHeavyWork);

//            Task<Void> task = new DownloadTask(fieldURL.getText());
//            ProgressBar progressBar = new ProgressBar();
//            progressBar.setPrefWidth(350);
//            progressBar.progressProperty().bind(task.progressProperty());
//            root.getChildren().add(progressBar);
//
//            fieldURL.clear();
//
//            Thread thread = new Thread(task);
//            thread.setDaemon(true);
//            thread.start();
        });

        return root;
    }

    private void doHeavyWork() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }

        Platform.runLater(() -> {
            System.out.println("Work done on thread: " + Thread.currentThread().getName());
        });
    }

    private class DownloadTask extends Task<Void> {

        private String url;

        public DownloadTask(String url) {
            this.url = url;
        }

        @Override
        protected Void call() throws Exception {
            String ext = url.substring(url.lastIndexOf("."), url.length());
            URLConnection connection = new URL(url).openConnection();
            long fileLength = connection.getContentLengthLong();

            try (InputStream is = connection.getInputStream();
                    OutputStream os = Files.newOutputStream(Paths.get("downloadedfile" + ext))) {

                long nread = 0L;
                byte[] buf = new byte[8192];
                int n;
                while ((n = is.read(buf)) > 0) {
                    os.write(buf, 0, n);
                    nread += n;
                    updateProgress(nread, fileLength);
                }
            }

            return null;
        }

        @Override
        protected void failed() {
            System.out.println("Finished on thread: " + Thread.currentThread().getName());
            System.out.println("failed");
        }

        @Override
        protected void succeeded() {
            System.out.println("downloaded");
        }
    }

    @Override
    public void stop() throws Exception {
        service.shutdownNow();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
