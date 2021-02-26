package com.almasb.fallout4menu;

import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class MenuItem extends StackPane {

    private static final Font font;

    static {
        font = Font.loadFont(MenuItem.class.getResource("res/RobotoCondensed-Regular.ttf").toExternalForm(), 26);
    }

    private Text text;
    private Rectangle selection;
    private DropShadow shadow;

    public MenuItem(String name, int width) {
        setAlignment(Pos.CENTER_LEFT);

        text = new Text(name);
        text.setTranslateX(5);
        text.setFont(font);
        text.setFill(Colors.MENU_TEXT);
        text.setStroke(Color.BLACK);

        shadow = new DropShadow(5, Color.BLACK);
        text.setEffect(shadow);

        selection = new Rectangle(width - 45, 30);
        selection.setFill(Colors.MENU_ITEM_SELECTION);
        selection.setStroke(Color.BLACK);
        selection.setVisible(false);

        GaussianBlur blur = new GaussianBlur(8);
        selection.setEffect(blur);

        getChildren().addAll(selection, text);

        setOnMouseEntered(e -> {
            onSelect();
        });

        setOnMouseExited(e -> {
            onDeselect();
        });

        setOnMousePressed(e -> {
            text.setFill(Color.YELLOW);
        });
    }

    private void onSelect() {
        text.setFill(Color.BLACK);
        selection.setVisible(true);

        shadow.setRadius(1);
    }

    private void onDeselect() {
        text.setFill(Colors.MENU_TEXT);
        selection.setVisible(false);

        shadow.setRadius(5);
    }

    public void setOnAction(Runnable action) {
        setOnMouseClicked(e -> {
            FillTransition ft = new FillTransition(Duration.seconds(0.45), selection,
                    Color.YELLOW, Colors.MENU_ITEM_SELECTION);
            ft.setOnFinished(e2 -> action.run());
            ft.play();
        });
    }
}
