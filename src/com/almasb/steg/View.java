package com.almasb.steg;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class View extends Pane {

    private Controller controller;

    public View(Controller controller) {
        this.controller = controller;

        setPrefSize(635 * 2, 500);

        Image image = new Image("http://cdn.ndtv.com/tech/windows_10_startmenu_cropped.jpg");
        ImageView originalView = new ImageView(image);

        ImageView modifiedView = new ImageView();
        modifiedView.setTranslateX(635);

        TextField field = new TextField("Enter message");
        field.setTranslateY(454);
        field.setOnAction(e -> controller.onEncode());

        Button btnDecode = new Button("DECODE");
        btnDecode.setTranslateX(635);
        btnDecode.setTranslateY(454);
        btnDecode.setOnAction(e -> controller.onDecode());

        controller.injectUI(originalView, modifiedView, field);

        getChildren().addAll(originalView, modifiedView, field, btnDecode);
    }
}
