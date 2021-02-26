package com.almasb.image;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class ImageEditingApp extends Application {

    private List<Filter> filters = Arrays.asList(
            new Filter("Invert", c -> c.invert()),
            new Filter("Grayscale", c -> c.grayscale()),
            new Filter("Black and White", c -> valueOf(c) < 1.5 ? Color.BLACK : Color.WHITE),
            new Filter("Red", c -> Color.color(1.0, c.getGreen(), c.getBlue())),
            new Filter("Green", c -> Color.color(c.getRed(), 1.0, c.getBlue())),
            new Filter("Blue", c -> Color.color(c.getRed(), c.getGreen(), 1.0))
    );

    private double valueOf(Color c) {
        return c.getRed() + c.getGreen() + c.getBlue();
    }

    private Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(800, 600);

        ImageView view1 = new ImageView(new Image("https://placekitten.com/400/550", true));
        ImageView view2 = new ImageView();

        MenuBar bar = new MenuBar();
        Menu menu = new Menu("Filter...");

        filters.forEach(filter -> {
            MenuItem item = new MenuItem(filter.name);
            item.setOnAction(e -> {
                view2.setImage(filter.apply(view1.getImage()));
            });

            menu.getItems().add(item);
        });

        bar.getMenus().add(menu);

        root.setTop(bar);
        root.setCenter(new HBox(view1, view2));

        return root;
    }

    private static class Filter implements Function<Image, Image> {

        private String name;
        private Function<Color, Color> colorMap;

        Filter(String name, Function<Color, Color> colorMap) {
            this.name = name;
            this.colorMap = colorMap;
        }

        @Override
        public Image apply(Image source) {
            int w = (int) source.getWidth();
            int h = (int) source.getHeight();

            WritableImage image = new WritableImage(w, h);

            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    Color c1 = source.getPixelReader().getColor(x, y);
                    Color c2 = colorMap.apply(c1);

                    image.getPixelWriter().setColor(x, y, c2);
                }
            }

            return image;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
