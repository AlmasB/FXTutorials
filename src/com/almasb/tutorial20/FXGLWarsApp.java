package com.almasb.tutorial20;

import java.util.Random;

import javafx.animation.TranslateTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.GameSettings;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;

public class FXGLWarsApp extends GameApplication {

    private long lastTimeEnemySpawned = 0, lastTimeShot = 0;
    private IntegerProperty score = new SimpleIntegerProperty();

    private Entity player = new Entity("player");
    private Random random = new Random();

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("FXGLWars");
        settings.setVersion("1.0");
    }

    @Override
    protected void initGame(Pane gameRoot) {
        player.setTranslateX(640);
        player.setTranslateY(360);
        player.setUsePhysics(true);

        Rectangle rect = new Rectangle(40, 40);
        rect.setFill(Color.BLUE);
        player.setGraphics(rect);

        addEntities(player);

        addKeyPressBinding(KeyCode.W, () -> {
            player.translate(0, -5);
        });

        addKeyPressBinding(KeyCode.S, () -> {
            player.translate(0, 5);
        });

        addKeyPressBinding(KeyCode.A, () -> {
            player.translate(-5, 0);
        });

        addKeyPressBinding(KeyCode.D, () -> {
            player.translate(5, 0);
        });

        addCollisionHandler("player", "enemy", (player, enemy) -> {
            enemy.setProperty("alive", false);
            playScoreAnimation(player, -1000);
        });

        addCollisionHandler("bullet", "enemy", (bullet, enemy) -> {
            bullet.setProperty("alive", false);
            enemy.setProperty("alive", false);

            playScoreAnimation(enemy, 100);
            playDeathAnimation(enemy);
        });
    }

    @Override
    protected void initUI(Pane uiRoot) {
        Text textScore = new Text();
        textScore.setTranslateX(1100);
        textScore.setTranslateY(50);
        textScore.textProperty().bind(score.asString());

        uiRoot.getChildren().add(textScore);
    }

    @Override
    protected void onUpdate(long now) {
        getEntities("enemy", "bullet", "particle").stream()
            .filter(enemy -> !enemy.<Boolean>getProperty("alive"))
            .forEach(this::removeEntity);

        if (now - lastTimeEnemySpawned >= 1 * SECOND) {
            spawnEnemy();
            lastTimeEnemySpawned = now;
        }

        if (mouse.leftPressed && now - lastTimeShot >= 0.25 * SECOND) {
            spawnBullet();
            lastTimeShot = now;
        }
    }

    private void spawnEnemy() {
        Entity enemy = new Entity("enemy");
        enemy.setTranslateX(random.nextInt(1200));
        enemy.setTranslateY(random.nextInt(700));
        enemy.setUsePhysics(true);

        Rectangle rect = new Rectangle(40, 40);
        rect.setFill(Color.RED);
        enemy.setGraphics(rect);
        enemy.setProperty("alive", true);

        enemy.addControl(new Control() {
            private Point2D velocity = new Point2D(0, 0);
            private long lastTimeChangedVelocity = 0;

            @Override
            public void onUpdate(Entity entity, long now) {
                if (now - lastTimeChangedVelocity >= 0.5 * SECOND) {
                    velocity = new Point2D(player.getTranslateX(), player.getTranslateY())
                        .subtract(entity.getTranslateX(), entity.getTranslateY())
                        .normalize()
                        .multiply(2);
                    lastTimeChangedVelocity = now;
                }

                entity.translate(velocity.getX(), velocity.getY());
            }
        });

        addEntities(enemy);
    }

    private void spawnBullet() {
        Entity bullet = new Entity("bullet");
        bullet.setTranslateX(player.getTranslateX() + 20);
        bullet.setTranslateY(player.getTranslateY() + 20);
        bullet.setUsePhysics(true);
        bullet.setProperty("alive", true);

        Point2D vector = new Point2D(mouse.x, mouse.y)
            .subtract(bullet.getTranslateX(), bullet.getTranslateY())
            .normalize()
            .multiply(8);

        bullet.setProperty("vector", vector);

        double angle = Math.toDegrees(Math.atan(vector.getY() / vector.getX()));
        angle = vector.getX() > 0 ? angle : 180 + angle;

        bullet.getTransforms().add(new Rotate(angle, 0, 0));

        bullet.addControl(new Control() {
            @Override
            public void onUpdate(Entity entity, long now) {
                Point2D velocity = entity.getProperty("vector");
                entity.translate(velocity.getX(), velocity.getY());

                if (entity.getTranslateX() < 0 || entity.getTranslateX() > 1280
                        || entity.getTranslateY() < 0 || entity.getTranslateY() > 720)
                    entity.setProperty("alive", false);
            }
        });

        Rectangle rect = new Rectangle(10, 1);
        rect.setFill(Color.BLACK);
        bullet.setGraphics(rect);

        addEntities(bullet);
    }

    private void playScoreAnimation(Entity object, int value) {
        Entity scoreEntity = new Entity("score");
        scoreEntity.setTranslateX(object.getTranslateX());
        scoreEntity.setTranslateY(object.getTranslateY());
        scoreEntity.setGraphics(new Text(String.valueOf(value)));

        addEntities(scoreEntity);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(1), scoreEntity);
        tt.setToX(1100);
        tt.setToY(50);
        tt.setOnFinished(event -> {
            removeEntity(scoreEntity);
            score.set(score.get() + value);
        });
        tt.play();
    }

    private void playDeathAnimation(Entity enemy) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                Entity particle = new Entity("particle");
                particle.setTranslateX(j * 2 + enemy.getTranslateX());
                particle.setTranslateY(i * 2 + enemy.getTranslateY());
                particle.setProperty("alive", true);

                Rectangle rect = new Rectangle(2, 2);
                rect.setFill(Color.RED);
                particle.setGraphics(rect);

                Point2D vector = new Point2D(random.nextDouble() - 0.5, random.nextDouble() - 0.8).multiply(2);
                particle.setProperty("vector", vector);

                particle.addControl((entity, now) -> {
                    Point2D v = entity.getProperty("vector");
                    entity.setProperty("vector", v.add(0, 0.05));
                    entity.translate(v.getX(), v.getY());

                    if (entity.getTranslateY() > 720)
                        entity.setProperty("alive", false);
                });

                addEntities(particle);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
