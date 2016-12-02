package com.almasb.algo.disintegration;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class Particle {

    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();

    private Point2D velocity = Point2D.ZERO;

    private Color color;

    private double life = 1.0;
    private boolean active = false;

    public Particle(int x, int y, Color color) {
        this.x.set(x);
        this.y.set(y);
        this.color = color;
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public double getX() {
        return x.get();
    }

    public double getY() {
        return y.get();
    }

    public boolean isDead() {
        return life == 0;
    }

    public boolean isActive() {
        return active;
    }

    public void activate(Point2D velocity) {
        active = true;
        this.velocity = velocity;
    }

    public void update() {
        if (!active)
            return;

        life -= 0.017 * 0.75;

        if (life < 0)
            life = 0;

        this.x.set(getX() + velocity.getX());
        this.y.set(getY() + velocity.getY());
    }

    public void draw(GraphicsContext g) {
        g.setFill(color);

        g.setGlobalAlpha(life);
        g.fillOval(getX(), getY(), 1, 1);
    }
}
