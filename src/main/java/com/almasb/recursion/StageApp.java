package com.almasb.recursion;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class StageApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //stage.initStyle(StageStyle.UNDECORATED);

        stage.setX(0);

        Button btn = new Button("Close");
        btn.setOnAction(e -> stage.close());

        var image = new WritableImage(32, 32);
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                image.getPixelWriter().setColor(x, y, Color.RED);
            }
        }

        stage.getIcons().add(image);

        stage.setAlwaysOnTop(true);
        //stage.setFullScreen(true);

        Button btnIcon = new Button("Iconify");
        btnIcon.setTranslateY(50);
        btnIcon.setOnAction(e -> stage.setIconified(true));

        stage.setTitle("Hello Stage JavaFX");

        stage.setMaxWidth(1280);
        stage.setMaxHeight(720);

        stage.setResizable(false);
//        stage.setOnCloseRequest(e -> {
//            e.consume();
//            System.out.println("Closing");
//        });

        stage.setOpacity(0.5);

        stage.setRenderScaleX(0.2);
        stage.setRenderScaleY(0.2);

        stage.centerOnScreen();

        var r = new Rectangle(800, 600);
        r.setEffect(new BoxBlur(20, 20, 3));

        stage.setScene(new Scene(new Pane(r, btn, btnIcon), 800, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
