package com.almasb.tutorial20;

import java.util.Random;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.GameSettings;
import com.almasb.fxgl.asset.Assets;
import com.almasb.fxgl.asset.Texture;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.FXGLEvent;
import com.almasb.fxgl.entity.FXGLEventType;

public class FXWarsApp extends GameApplication {

    private enum Event implements FXGLEventType {
        DEATH
    }

    private long lastTimeEnemySpawned = 0, lastTimeShot = 0,
            lastTimePowerupSpawned = 0, lastTimeShotLaser = 0,
            lastTimeTraceSpawned = 0;

    private IntegerProperty score = new SimpleIntegerProperty();
    private BooleanProperty laserAlive = new SimpleBooleanProperty(false);
    private Rectangle laserBar = new Rectangle(100, 20);

    private Text fpsText = new Text();

    private Entity player = new Entity("player");
    private Random random = new Random();

    private AudioClip soundShoot, soundPowerup,
                    soundExplosion, soundLaserReady, soundLaserShoot;

    private Assets assets;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("FXGLWars");
        settings.setVersion("1.0");
    }

    @Override
    protected void initAssets() throws Exception {
        assets = assetManager.cache();
        assets.logCached();

        soundShoot = assets.getAudio("shoot.wav");
        soundPowerup = assets.getAudio("powerup.wav");
        soundExplosion = assets.getAudio("explosion.wav");
        soundLaserReady = assets.getAudio("laser_ready.wav");
        soundLaserShoot = assets.getAudio("laser_shoot.wav");

//        soundShoot.setVolume(0);
//        soundPowerup.setVolume(0);
//        soundLaserReady.setVolume(0);
//        soundExplosion.setVolume(0);
//        soundLaserShoot.setVolume(0);
    }

    @Override
    protected void initGame(Pane gameRoot) {
        Entity bg = new Entity("background");
        bg.setGraphics(new Rectangle(1280, 720));

        addEntities(bg);

        initCollisions();
        initInput();
        spawnPlayer();
    }

    private void initCollisions() {
        addCollisionHandler("player", "enemy", (player, enemy) -> {
            player.fireFXGLEvent(new FXGLEvent(Event.DEATH));
            enemy.fireFXGLEvent(new FXGLEvent(Event.DEATH, player));
        });

        addCollisionHandler("bullet", "enemy", (bullet, enemy) -> {
            bullet.fireFXGLEvent(new FXGLEvent(Event.DEATH));
            enemy.fireFXGLEvent(new FXGLEvent(Event.DEATH, bullet));
        });

        addCollisionHandler("laser", "enemy", (laser, enemy) -> {
            enemy.fireFXGLEvent(new FXGLEvent(Event.DEATH, laser));
        });

        addCollisionHandler("player", "powerup", (player, powerup) -> {
            powerup.fireFXGLEvent(new FXGLEvent(Event.DEATH));
        });
    }

    @Override
    protected void initInput() {
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

        addKeyTypedBinding(KeyCode.SPACE, () -> {
            if (currentTime - lastTimeShotLaser >= 5 * SECOND) {
                spawnLaser();
                lastTimeShotLaser = currentTime;
            }
        });
    }

    @Override
    protected void initUI(Pane uiRoot) {
        Text textScore = new Text();
        textScore.setTranslateX(1100);
        textScore.setTranslateY(50);
        textScore.textProperty().bind(score.asString());
        textScore.setFill(Color.WHITE);

        laserBar.setFill(Color.YELLOWGREEN);

        Text laserText = new Text("SPACE");
        laserText.setFont(Font.font(18));
        laserText.setFill(Color.WHITE);
        laserText.visibleProperty().bind(laserBar.widthProperty().isEqualTo(100));
        laserText.visibleProperty().addListener((obs, old, newValue) -> {
            if (newValue.booleanValue()) {
                soundLaserReady.play();
            }
        });

        StackPane stack = new StackPane();
        stack.setTranslateX(50);
        stack.setTranslateY(50);
        stack.setAlignment(Pos.CENTER);

        stack.getChildren().addAll(laserBar, laserText);

        fpsText.setTranslateX(50);
        fpsText.setTranslateY(100);
        fpsText.setFill(Color.WHITE);

        uiRoot.getChildren().addAll(textScore, stack, fpsText);
    }

    @Override
    protected void onUpdate(long now) {
        if (now - lastTimeEnemySpawned >= 1 * SECOND) {
            spawnEnemy();
            lastTimeEnemySpawned = now;
        }

        if (now - lastTimePowerupSpawned >= 5 * SECOND) {
            spawnPowerup();
            lastTimePowerupSpawned = now;
        }

        if (now - lastTimeTraceSpawned >= 0.1 * SECOND) {
            spawnTrace();
            lastTimeTraceSpawned = now;
        }

        if (mouse.leftPressed && now - lastTimeShot >= 0.25 * SECOND) {
            spawnBullet();
            lastTimeShot = now;
        }

        laserBar.setWidth(Math.min(100, ((now - lastTimeShotLaser) * 1.0f / (5 * SECOND)) * 100));

        fpsText.setText("FPS: " + fps + " Performance: " + fpsPerformance);
    }

    private void spawnPlayer() {
        player.setTranslateX(640);
        player.setTranslateY(360);
        player.setUsePhysics(true);
        player.addFXGLEventHandler(Event.DEATH, event -> {
            Entity p = event.getTarget();
            p.setTranslateX(random.nextInt(1200));
            p.setTranslateY(random.nextInt(600));
            playScoreAnimation(p, -1000);
            soundExplosion.play();
        });

        Rectangle rect = new Rectangle(40, 40);
        rect.setFill(Color.BLUE);
        player.setGraphics(rect);

        addEntities(player);
    }

    private void spawnEnemy() {
        Entity enemy = new Entity("enemy");
        enemy.setTranslateX(random.nextInt(1200));
        enemy.setTranslateY(random.nextInt(700));
        enemy.setUsePhysics(true);

        Rectangle rect = new Rectangle(40, 40);
        rect.setFill(Color.RED);
        enemy.setGraphics(rect);

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

        enemy.addFXGLEventHandler(Event.DEATH, event -> {
            switch (event.getSource().getType()) {
                case "bullet":
                    playEnemyDeathAnimation(event.getTarget());
                    break;
                case "laser":
                    playEnemyDeathAnimationLaser(event.getTarget());
                    break;
                case "player":  // fallthru
                default:
            }
        });

        addEntities(enemy);
    }

    private void spawnBullet() {
        Entity bullet = new Entity("bullet");
        bullet.setTranslateX(player.getTranslateX() + 20);
        bullet.setTranslateY(player.getTranslateY() + 20);
        bullet.setUsePhysics(true);

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
                    entity.fireFXGLEvent(new FXGLEvent(Event.DEATH));
            }
        });

        Rectangle rect = new Rectangle(10, 1);
        rect.setFill(Color.WHITE);
        bullet.setGraphics(rect);

        bullet.addFXGLEventHandler(Event.DEATH, event -> {
            playBulletDeathAnimation(event.getTarget());
        });

        addEntities(bullet);

        soundShoot.play();
    }

    private void spawnTrace() {
        Entity trace = new Entity("particle");
        trace.setTranslateX(player.getTranslateX());
        trace.setTranslateY(player.getTranslateY());

        Rectangle rect = new Rectangle(40, 40);
        rect.setStroke(Color.BLUE);
        rect.setFill(null);

        trace.setGraphics(rect);
        addEntities(trace);

        FadeTransition ft = new FadeTransition(Duration.seconds(1), trace);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(event -> removeEntity(trace));
        ft.play();
    }

    private void spawnPowerup() {
        Entity powerup = new Entity("powerup");
        powerup.setTranslateX(random.nextInt(1200));
        powerup.setTranslateY(random.nextInt(600));
        powerup.setUsePhysics(true);

        Texture texture = assets.getTexture("powerup_0" + (random.nextInt(6) + 1) + ".png");
        powerup.setGraphics(texture.toStaticAnimatedTexture(8, 0.5 * SECOND));

        powerup.addFXGLEventHandler(Event.DEATH, event -> {
            Entity p = event.getTarget();
            // disable collision detection
            p.setUsePhysics(false);

            ScaleTransition st = new ScaleTransition(Duration.seconds(1), p);
            st.setFromX(1);
            st.setFromY(1);
            st.setToX(0);
            st.setToY(0);
            st.setOnFinished(e -> {
                removeEntity(p);
            });
            st.play();

            playScoreAnimation(p, 2000);
            soundPowerup.play();
        });

        addEntities(powerup);
    }

    int angle = -90;

    private void spawnLaser() {
        Rectangle rect = new Rectangle(1000, 1);
        rect.setFill(Color.YELLOW);

        Entity laser = new Entity("laser")
                    .setPosition(player.getTranslateX() + 20, player.getTranslateY() + 20)
                    .setUsePhysics(true)
                    .setGraphics(rect);

        laser.getTransforms().add(new Rotate(angle));
        laserAlive.set(true);

        runAtIntervalWhile(() -> {
            laser.setPosition(player.getTranslateX() + 20, player.getTranslateY() + 20);
            laser.getTransforms().clear();
            laser.getTransforms().add(new Rotate(++angle));

            if (angle == 270) {
                laserAlive.set(false);
                removeEntity(laser);
                angle = -90;
            }
        }, 0.002 * SECOND, laserAlive);

        addEntities(laser);

        soundLaserShoot.play();
    }

    private void playScoreAnimation(Entity object, int value) {
        Entity scoreEntity = new Entity("score");
        scoreEntity.setTranslateX(object.getTranslateX());
        scoreEntity.setTranslateY(object.getTranslateY());

        Text text = new Text(String.valueOf(value));
        text.setFill(Color.WHITESMOKE);
        scoreEntity.setGraphics(text);

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

    private void playEnemyDeathAnimation(Entity enemy) {
        removeEntity(enemy);
        playScoreAnimation(enemy, 100);

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                Entity particle = new Entity("particle");
                particle.setTranslateX(j * 2 + enemy.getTranslateX());
                particle.setTranslateY(i * 2 + enemy.getTranslateY());

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
                        removeEntity(entity);
                });

                addEntities(particle);
            }
        }

        soundExplosion.play();
    }

    private void playEnemyDeathAnimationLaser(Entity enemy) {
        enemy.setUsePhysics(false);
        enemy.removeControls();
        playScoreAnimation(enemy, 100);

        ScaleTransition st = new ScaleTransition(Duration.seconds(0.66), enemy);
        st.setFromY(1);
        st.setToY(0);
        st.setOnFinished(event -> removeEntity(enemy));
        st.play();
    }

    private void playBulletDeathAnimation(Entity bullet) {
        removeEntity(bullet);

        Color color = Color.hsb(random.nextInt(150) + 50, 1, 1, 0.75);

        int max = random.nextInt(20) + 20;

        for (int i = 0; i < max; i++) {
            Entity particle = new Entity("particle");
            particle.setPosition(bullet.getTranslateX(), bullet.getTranslateY());

            Rectangle rect = new Rectangle(5, 2);
            rect.setFill(color);
            DropShadow ds = new DropShadow(2, Color.GOLD);
            ds.setInput(new Glow(0.7));
            rect.setEffect(ds);
            particle.setGraphics(rect);

            Point2D vector = new Point2D(random.nextDouble() - 0.5, random.nextDouble() - 0.5).multiply(2);
            particle.setProperty("vector", vector);

            double angle = Math.toDegrees(Math.atan(vector.getY() / vector.getX()));
            angle = vector.getX() > 0 ? angle : 180 + angle;

            particle.getTransforms().add(new Rotate(angle, 0, 0));

            final long spawnTime = currentTime;

            particle.addControl((entity, now) -> {
                Point2D v = entity.getProperty("vector");
                entity.translate(v.getX(), v.getY());
                entity.setOpacity(entity.getOpacity() - 0.016);

                if (now - spawnTime >= 1.5 * SECOND) {
                    removeEntity(entity);
                }
            });

            addEntities(particle);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
