package com.almasb.collisions;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class SAT {

    public static boolean isColliding(Entity e1, Entity e2) {
        List<Point2D> axes = new ArrayList<>();
        axes.addAll(getAxes(e1.rotation));
        axes.addAll(getAxes(e2.rotation));

        axes = axes.stream()
                .map(Point2D::normalize)
                .collect(Collectors.toList());

        List<Point2D> e1Corners = e1.corners();
        List<Point2D> e2Corners = e2.corners();

        for (Point2D axis : axes) {
            double e1Min = e1Corners.stream().mapToDouble(p -> p.dotProduct(axis)).min().getAsDouble();
            double e1Max = e1Corners.stream().mapToDouble(p -> p.dotProduct(axis)).max().getAsDouble();

            double e2Min = e2Corners.stream().mapToDouble(p -> p.dotProduct(axis)).min().getAsDouble();
            double e2Max = e2Corners.stream().mapToDouble(p -> p.dotProduct(axis)).max().getAsDouble();

            if (e1Max < e2Min || e2Max < e1Min)
                return false;
        }

        return true;
    }

    public static List<Point2D> getAxes(double angle) {
        return Arrays.asList(
                new Point2D(cos(angle), sin(angle)),
                new Point2D(cos(angle + 90), sin(angle + 90))
        );
    }

    private static double cos(double angle) {
        return Math.cos(Math.toRadians(angle));
    }

    private static double sin(double angle) {
        return Math.sin(Math.toRadians(angle));
    }
}
