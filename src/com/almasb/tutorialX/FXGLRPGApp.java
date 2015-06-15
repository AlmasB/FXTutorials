package com.almasb.tutorial26;

import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.GameSettings;
import com.almasb.fxgl.asset.Assets;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityType;
import com.almasb.fxgl.search.AStarLogic;
import com.almasb.fxgl.search.AStarNode;

public class FXGLRPGApp extends GameApplication {

    private enum Type implements EntityType {
        CONTENT
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("FXGL RPG");
        settings.setVersion("1.0");
    }

    @Override
    protected void initAssets() throws Exception {

        Assets assets = assetManager.cache();
        assets.logCached();


    }

    @Override
    protected void initGame(Pane gameRoot) {
        Entity content = new Entity(Type.CONTENT);
        content.setGraphics(createContent());

        addEntities(content);
    }

    @Override
    protected void initUI(Pane uiRoot) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void initInput() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onUpdate(long now) {
        // TODO Auto-generated method stub

    }

    private static final int GRID_WIDTH = 1000 / 40;
    private static final int GRID_HEIGHT = 720 / 40;

    private GridPane grid;
    private Button btnStart;

    private GameNode[][] gameGrid = new GameNode[GRID_WIDTH][GRID_HEIGHT];
    private GameNode start;
    private GameNode target;

    private AStarLogic logic = new AStarLogic();

    private AStarService service = new AStarService();

    private List<AStarNode> path;

    private Parent createContent() {
        grid = new GridPane();

        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                gameGrid[j][i] = new GameNode(j, i);
                grid.add(gameGrid[j][i].sprite, j, i);
            }
        }

        start = gameGrid[0][0];
        target = gameGrid[0][0];
        start.sprite.setFill(Color.AQUA);
        target.sprite.setFill(Color.YELLOW);
        initGrid();

        btnStart = new Button("START");
        btnStart.setOnAction(event -> {
            btnStart.setDisable(true);
            start.sprite.setFill(Color.BLACK);

            for (Node n : grid.getChildren()) {
                Rectangle r = (Rectangle) n;
                if (r.getFill() == Color.GREEN)
                    r.setFill(Color.BLACK);
            }
            service.restart();
        });

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getChildren().addAll(btnStart);

        HBox hbox = new HBox();
        hbox.getChildren().addAll(grid, vbox);

        return hbox;
    }

    private void initGrid() {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                gameGrid[j][i].setHCost(Math.abs(target.getX() - i) + Math.abs(target.getY() - j));
            }
        }
    }

    private class GameNode extends AStarNode {
        private Rectangle sprite = new Rectangle(40, 40);

        public GameNode(int x, int y) {
            super(x, y, 0, 0);

            //sprite.setStroke(Color.RED);
            sprite.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (target.sprite.getFill() == Color.YELLOW)
                        target.sprite.setFill(Color.BLACK);
                    sprite.setFill(Color.YELLOW);
                    target = this;
                    initGrid();
                }
                else {
                    // set wall
                    if (sprite.getFill() != Color.BLUE) {
                        sprite.setFill(Color.BLUE);
                        setNodeValue(1);
                    }
                    else {
                        sprite.setFill(Color.BLACK);
                        setNodeValue(0);
                    }
                }
            });
        }
    }

    private class AStarService extends Service<Void> {
        @Override
        protected Task<Void> createTask() {
            return new AStarTask();
        }
    }

    private class AStarTask extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            path = logic.getPath(gameGrid, start, target);

            return null;
        }

        @Override
        protected void succeeded() {
            new Thread(() -> {
                for (AStarNode n : path) {
                    Platform.runLater(() -> {
                        Rectangle r = (Rectangle) getNodeFromGridPane(grid, n.getX(), n.getY());
                        r.setFill(Color.GREEN);
                    });

                    try {
                        Thread.sleep(200);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                start = target;

                Platform.runLater(() -> {
                    start.sprite.setFill(Color.AQUA);
                    btnStart.setDisable(false);
                });
            }).start();
        }
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
