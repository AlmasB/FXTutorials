package com.almasb.minesweeper;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class InverseGridApp extends Application {

    private static final int GRID_SIZE_IN_CELLS = 4;
    private static final int CELL_SIZE = 100;

    private Pane root;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(GRID_SIZE_IN_CELLS * CELL_SIZE, GRID_SIZE_IN_CELLS * CELL_SIZE);

        for (int y = 0; y < GRID_SIZE_IN_CELLS; y++) {
            for (int x = 0; x < GRID_SIZE_IN_CELLS; x++) {
                final int finalX = x;
                final int finalY = y;

                Cell cell = new Cell(x, y);

                cell.setOnMouseClicked(e -> {

                    cell.flip();

                    Stream.of(
                            new Point(finalX + 1, finalY),
                            new Point(finalX - 1, finalY),
                            new Point(finalX, finalY + 1),
                            new Point(finalX, finalY - 1)
                    )
                            .filter(p -> isValid(p))
                            .map(p -> getCell(p.x, p.y))
                            .forEach(Cell::flip);

                    // TODO: check all are flipped, if so player wins
                });

                root.getChildren().add(cell);
            }
        }

        return root;
    }

    private Cell getCell(int x, int y) {
        return root.getChildren()
                .stream()
                .map(n -> (Cell) n)
                .filter(c -> c.x == x && c.y == y)
                .findAny()
                .get();
    }

    private boolean isValid(Point p) {
        return p.x >= 0 && p.x < GRID_SIZE_IN_CELLS
                && p.y >= 0 && p.y < GRID_SIZE_IN_CELLS;
    }

    private static class Cell extends StackPane {

        private int x;
        private int y;

        private boolean isFlipped = false;

        private Rectangle bg;
        private Text symbol;

        Cell(int x, int y) {
            this.x = x;
            this.y = y;

            setTranslateX(x * CELL_SIZE);
            setTranslateY(y * CELL_SIZE);

            bg = new Rectangle(CELL_SIZE, CELL_SIZE, Color.GREEN);
            bg.setStroke(Color.WHITE);

            symbol = new Text("0");
            symbol.setFont(Font.font(30));

            getChildren().addAll(bg, symbol);
        }

        void flip() {
            isFlipped = !isFlipped;

            bg.setFill(isFlipped ? Color.BLUE : Color.GREEN);
            symbol.setText(isFlipped ? "1" : "0");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
