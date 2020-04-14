package com.almasb.datavis;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class CommitVisualizer extends Application {

    private Parent createContent() {
        StackPane root = new StackPane();
        root.setPrefSize(800, 600);

        LineChart<String, Number> chart = new LineChart<>(
                new CategoryAxis(), new NumberAxis()
        );

        chart.getData().add(extract(Paths.get("log1.txt"), "Project 1"));
        chart.getData().add(extract(Paths.get("log2.txt"), "Project 2"));

        root.getChildren().add(chart);

        return root;
    }

    private XYChart.Series<String, Number> extract(Path file, String name) {
        try {
            Map<LocalDate, Long> data = Files.readAllLines(file)
                    .stream()
                    .map(line -> LocalDate.parse(line))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(name);

            data = new TreeMap<>(data);

            data.forEach((date, numCommits) -> {
                series.getData().add(new XYChart.Data<>(
                        date.format(DateTimeFormatter.BASIC_ISO_DATE),
                        numCommits
                ));
            });

            return series;

        } catch (IOException e) {
            e.printStackTrace();
            return new XYChart.Series<>();
        }
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
