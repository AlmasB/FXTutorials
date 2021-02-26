package gamedev;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameDevMath extends Application {

    PointView player = new PointView(new Point2D(100, 100));
    PointView enemy = new PointView(new Point2D(250, 75));

    VectorView vectorView = new VectorView(player.getPoint().subtract(enemy.getPoint()));

    Pane root = new Pane();

    private Parent createContent() {

        root.setPrefSize(800, 600);

        vectorView.moveTo(enemy.getPoint());

        root.getChildren().addAll(player, enemy, vectorView);
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnMouseClicked(event -> {
            player.setTranslateX(event.getSceneX());
            player.setTranslateY(event.getSceneY());

            Point2D vector = player.getPoint().subtract(enemy.getPoint());

            vectorView.setVector(vector);

            PointView bullet = new PointView(enemy.getPoint());
            root.getChildren().add(bullet);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(3), bullet);
            tt.setByX(vector.getX());
            tt.setByY(vector.getY());
            tt.setAutoReverse(true);
            tt.setCycleCount(2);
            tt.setOnFinished(e -> root.getChildren().remove(bullet));
            tt.play();
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static class PointView extends Circle {
        public PointView(Point2D point) {
            super(5);
            setTranslateX(point.getX());
            setTranslateY(point.getY());
        }

        public Point2D getPoint() {
            return new Point2D(getTranslateX(), getTranslateY());
        }
    }

    private static class VectorView extends Line {
        private Point2D vector;

        public VectorView(Point2D vector) {
            this.vector = vector;
        }

        public void setVector(Point2D vector) {
            this.vector = vector;
            setEndX(getStartX() + vector.getX());
            setEndY(getStartY() + vector.getY());
        }

        public void moveTo(Point2D start) {
            setStartX(start.getX());
            setStartY(start.getY());
            setEndX(start.getX() + vector.getX());
            setEndY(start.getY() + vector.getY());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
