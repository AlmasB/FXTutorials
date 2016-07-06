package com.almasb.algo.traversal;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class AlgorithmApp extends Application {

    public static final int TILE_SIZE = 10;

    private static final int W = 800;
    private static final int H = 600;

    private GraphicsContext g;

    private Tile[][] grid = new Tile[W / TILE_SIZE][H / TILE_SIZE];

    private List<Tile> tilesToVisit = new ArrayList<>();

    private ScheduledExecutorService algorithmThread = Executors.newSingleThreadScheduledExecutor();
    private TraversalAlgorithm algorithm;

    private Parent createContent() {
        Pane root = new Pane();

        Canvas canvas = new Canvas(W, H);
        g = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                grid[x][y] = new Tile(x, y);
            }
        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();

        algorithm = AlgorithmFactory.breadthFirst();
        algorithmThread.scheduleAtFixedRate(this::algorithmUpdate, 0, 1, TimeUnit.MILLISECONDS);

        return root;
    }

    private void update() {
        g.clearRect(0, 0, W, H);

        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                grid[x][y].update();
                grid[x][y].draw(g);
            }
        }
    }

    private void algorithmUpdate() {
        if (!tilesToVisit.isEmpty()) {
            Tile tile = algorithm.next(tilesToVisit);
            tile.visit();

            tilesToVisit.addAll(getNeighbors(tile));
        }
    }

    private List<Tile> getNeighbors(Tile tile) {
        List<Pair<Integer, Integer> > points = Arrays.asList(
                new Pair<>(1, 0),
                new Pair<>(0, 1),
                new Pair<>(-1, 0),
                new Pair<>(0, -1)
        );

        return points.stream()
                .map(pair -> new Pair<>(tile.x + pair.getKey(), tile.y + pair.getValue()))
                .filter(pair -> {
                    int newX = pair.getKey();
                    int newY = pair.getValue();

                    return newX >= 0 && newX < W / TILE_SIZE
                            && newY >= 0 && newY < H / TILE_SIZE
                            && !grid[newX][newY].visited;
                })
                .map(pair -> {
                    Tile t = grid[pair.getKey()][pair.getValue()];
                    t.visit();

                    return t;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void stop() throws Exception {
        algorithmThread.shutdownNow();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnMouseClicked(e -> {
            int x = (int) e.getSceneX() / TILE_SIZE;
            int y = (int) e.getSceneY() / TILE_SIZE;

            tilesToVisit.add(grid[x][y]);
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
