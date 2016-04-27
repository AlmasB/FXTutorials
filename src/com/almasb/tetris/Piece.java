package com.almasb.tetris;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class Piece {

    public int distance;
    public Direction direction;
    public Tetromino parent;
    public int x, y;

    public Piece(int distance, Direction direction) {
        this.distance = distance;
        this.direction = direction;
    }

    public void setParent(Tetromino parent) {
        this.parent = parent;
        x = parent.x + distance * direction.x;
        y = parent.y + distance * direction.y;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        x = parent.x + distance * direction.x;
        y = parent.y + distance * direction.y;
    }

    public Piece copy() {
        return new Piece(distance, direction);
    }
}
