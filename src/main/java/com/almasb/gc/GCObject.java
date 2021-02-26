package com.almasb.gc;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class GCObject extends StackPane {

    private boolean alive = true;
    private IntegerProperty age = new SimpleIntegerProperty();

    private Rectangle bg = new Rectangle(35, 70);

    public GCObject() {
        Text text = new Text();
        text.setFill(Color.WHITE);
        text.setFont(Font.font(32));
        text.textProperty().bind(age.asString());

        getChildren().addAll(bg, text);
    }

    public boolean isAlive() {
        return alive;
    }

    public int getAge() {
        return age.get();
    }

    public void markDead() {
        alive = false;
        bg.setFill(Color.RED);
    }

    public void mature() {
        age.set(age.get() + 1);
    }
}
