package com.almasb.tetris;

import java.util.Arrays;
import java.util.List;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class Piece {

    public int distance;
    public List<Direction> directions;
    public Tetromino parent;
    public int x, y;

    public Piece(int distance, Direction... direction) {
        this.distance = distance;
        this.directions = Arrays.asList(direction);
    }

    public void setParent(Tetromino parent) {
        this.parent = parent;

        int dx = 0, dy = 0;

        for (Direction d : directions) {
            dx += distance * d.x;
            dy += distance * d.y;
        }

        x = parent.x + dx;
        y = parent.y + dy;
    }

    public void setDirection(Direction... direction) {
        this.directions = Arrays.asList(direction);

        int dx = 0, dy = 0;

        for (Direction d : directions) {
            dx += distance * d.x;
            dy += distance * d.y;
        }

        x = parent.x + dx;
        y = parent.y + dy;
    }

    public Piece copy() {
        return new Piece(distance, directions.toArray(new Direction[0]));
    }
}
