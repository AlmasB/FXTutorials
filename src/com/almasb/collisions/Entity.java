package com.almasb.collisions;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class Entity {

    public int x, y, w, h;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public int rotation;

    public Entity(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void draw(GraphicsContext g) {
        g.save();

        Rotate r = new Rotate(rotation, x + w /2, y + h / 2);
        g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        g.strokeRect(x, y, w, h);

        g.restore();
    }

    public BBox bbox() {
        return new BBox(x, x + w, y, y + h);
    }

    public Point2D center() {
        return new Point2D(x + w / 2, y + h / 2);
    }

    public List<Point2D> cornerVectors() {
        return Arrays.asList(
                new Point2D(x, y),
                new Point2D(x + w, y),
                new Point2D(x + w, y + h),
                new Point2D(x, y + h)
        )
                .stream()
                .map(v -> v.subtract(center()))
                .collect(Collectors.toList());
    }

    public List<Point2D> corners() {
        return cornerVectors().stream()
                .map(v -> new Point2D(
                        v.getX() * cos(rotation) - v.getY() * sin(rotation),
                        v.getX() * sin(rotation) + v.getY() * cos(rotation)
                        )
                )
                .map(v -> v.add(center()))
                .collect(Collectors.toList());
    }

    public boolean isColliding(Entity other) {
        return bbox().isColliding(other.bbox());
    }

    public boolean isCollidingSAT(Entity other) {
        return SAT.isColliding(this, other);
    }

    private static double cos(double angle) {
        return Math.cos(Math.toRadians(angle));
    }

    private static double sin(double angle) {
        return Math.sin(Math.toRadians(angle));
    }
}
