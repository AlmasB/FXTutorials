package com.almasb.algo.traversal;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class Tile {

    private static final int MAX_LIFE = 2;

    public int x, y;
    public double life = 0;
    public boolean visited = false;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void visit() {
        life = MAX_LIFE;
        visited = true;
    }

    public void update() {
        if (life > 0) {
            life -= 0.017;
        }

        if (life < 0) {
            life = 0;
        }
    }

    public void draw(GraphicsContext g) {
        g.setFill(Color.color(life / MAX_LIFE, life / MAX_LIFE, life / MAX_LIFE));
        g.fillRect(x * AlgorithmApp.TILE_SIZE, y * AlgorithmApp.TILE_SIZE, AlgorithmApp.TILE_SIZE, AlgorithmApp.TILE_SIZE);
    }
}
