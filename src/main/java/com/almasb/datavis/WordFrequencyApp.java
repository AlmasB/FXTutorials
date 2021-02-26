package com.almasb.datavis;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.nio.file.Paths;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class WordFrequencyApp extends Application {

    private DataExtractor extractor = new WordExtractor(Paths.get("chapter1.tex"));

    private Parent createContent() {
        StackPane root = new StackPane();
        root.setPrefSize(800, 600);

        BarChart<String, Number> chart = new BarChart<>(new CategoryAxis(), new NumberAxis());

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        extractor.extract()
                .stream()
                .sorted((e1, e2) -> e2.getFrequency() - e1.getFrequency())
                .limit(10)
                .forEach(entry -> {
                    series.getData().add(new XYChart.Data<>(entry.getWord(), entry.getFrequency()));
                });

        chart.getData().add(series);

        root.getChildren().add(chart);

        return root;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
