package com.almasb.animations;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class NotificationPane extends Parent {

    private int w;
    private int h;
    private Rectangle bg;

    private TranslateTransition tt = new TranslateTransition(Duration.seconds(1), this);

    private boolean isAnimating = false;

    public NotificationPane(int w, int h) {
        this.w = w;
        this.h = h;

        bg = new Rectangle(w, h, Color.color(0.2, 0.2, 0.2, 0.75));

        getChildren().add(bg);

        tt.setOnFinished(e -> isAnimating = false);

        tt.setInterpolator(new Interpolator() {
            @Override
            protected double curve(double t) {
                //return (t == 0.0) ? 0.0 : Math.pow(2.0, 10 * (t - 1));
                return (t == 1.0) ? 1.0 : 1 - Math.pow(2.0, -10 * t);
            }
        });
    }

    public void animate() {
        if (isAnimating)
            return;

        isAnimating = true;

        tt.setFromX(getTranslateX());

        if (getTranslateX() < 800) {
            tt.setToX(getTranslateX() + w);
        } else {
            tt.setToX(getTranslateX() - w);
        }

        tt.play();
    }
}
