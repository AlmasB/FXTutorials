package com.almasb.towerfall;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.GameSettings;
import com.almasb.fxgl.asset.Assets;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityType;
import com.almasb.fxgl.event.InputManager.Mouse;
import com.almasb.fxgl.event.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.time.TimerManager;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TowerFallApp extends GameApplication {

    public enum Type implements EntityType {
        PLAYER, ARROW, PLATFORM
    }

    private Entity player;
    private Physics physics = new Physics(this);
    private Assets assets;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setFullScreen(true);
        settings.setIntroEnabled(false);
        settings.setMenuEnabled(false);
        settings.setWidth(1920);
        settings.setHeight(1080);
    }

    @Override
    protected void initAssets() throws Exception {
        assets = assetManager.cache();
    }

    Entity bg2 = Entity.noType();

    @Override
    protected void initGame() {
        Entity bg = Entity.noType();
        bg.setScaleX(1.3);
        bg.setScaleY(1.3);
        bg.setGraphics(assets.getTexture("bg.jpg"));
        sceneManager.addEntities(bg);


        bg2.setGraphics(assets.getTexture("bg.jpg"));
        bg2.setScaleX(1.3);
        bg2.setScaleY(1.3);
        //bg2.setOpacity(0.66);
        bg2.setBlendMode(BlendMode.MULTIPLY);

        RotateTransition rt = new RotateTransition(Duration.seconds(20), bg2);
        rt.setFromAngle(-10);
        rt.setToAngle(10);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setAutoReverse(true);
        rt.play();

        sceneManager.addEntities(bg2);

        //addPlatform(0, 200);
        //addPlatform(getWidth() - getWidth() / 3, 200);
        addPlatform(0, getHeight() - 200);
        addPlatform(getWidth() - getWidth() / 3, getHeight() - 200);
        //addPlatform(getWidth() / 2 - getWidth() / 6, getHeight() / 2);

        player = new Entity(Type.PLAYER);
        Rectangle rect = new Rectangle(40, 40);

        player.setGraphics(rect);
        player.setCollidable(true);
        player.setPosition(50, 830);
        player.setProperty("spawn", new Point2D(50, 830));
        player.setProperty("kills", new SimpleIntegerProperty());
        player.addControl(new PhysicsControl(physics));

        sceneManager.addEntities(player);

        //addEnemy(1850, 150);
        //addEnemy(50, 150);
        //addEnemy(1850, 830);

        nextLevel();
    }

    @Override
    protected void initPhysics() {
        physicsManager.addCollisionHandler(new CollisionHandler(Type.ARROW, Type.PLATFORM) {
            @Override
            public void onCollisionBegin(Entity a, Entity b) {
                a.removeControls();
                a.setCollidable(false);
            }

            @Override
            public void onCollision(Entity a, Entity b) {}

            @Override
            public void onCollisionEnd(Entity a, Entity b) {}
        });

        physicsManager.addCollisionHandler(new CollisionHandler(Type.ARROW, Type.PLAYER) {
            @Override
            public void onCollisionBegin(Entity arrow, Entity character) {
                if (arrow.getProperty("owner") == character)
                    return;

                IntegerProperty kills = arrow.<Entity>getProperty("owner").getProperty("kills");
                kills.set(kills.get() + 1);
                sceneManager.removeEntity(arrow);

                character.setControlsEnabled(false);
                character.setVisible(false);
                character.setCollidable(false);

                if (player == character)
                    inputManager.setProcessActions(false);

                if (sceneManager.getEntities(Type.PLAYER).stream().filter(e -> e.isVisible()).count() < 2) {
                    nextLevel();
                }
            }

            @Override
            public void onCollision(Entity a, Entity b) {}

            @Override
            public void onCollisionEnd(Entity a, Entity b) {}
        });
    }

    private Text debug = new Text();

    boolean once = true;

    @Override
    protected void initUI() {
        debug.setFill(Color.WHITE);
        debug.setTranslateX(150);
        debug.setTranslateY(50);

        sceneManager.addUINodes(debug);
    }

    private Mouse mouse = inputManager.getMouse();
    private List<Entity> platforms = new ArrayList<>();

    @Override
    protected void initInput() {
        inputManager.addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getControl(PhysicsControl.class).moveX(-5);
            }
        }, KeyCode.A);

        inputManager.addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getControl(PhysicsControl.class).moveX(5);
            }
        }, KeyCode.D);

        inputManager.addAction(new UserAction("Jump") {
            @Override
            protected void onAction() {
                player.getControl(PhysicsControl.class).jump();
            }
        }, KeyCode.W);

        inputManager.addAction(new UserAction("Exit") {
            @Override
            protected void onActionBegin() {
                exit();
            }
        }, KeyCode.L);

        inputManager.addAction(new UserAction("Shoot") {
            @Override
            protected void onActionBegin() {
                shootPlayerArrow();
            }
        }, MouseButton.PRIMARY);

//        inputManager.addAction(new UserAction("PARTICLE") {
//            @Override
//            protected void onActionBegin() {
//                m++;
//                if (m < BlendMode.values().length)
//                    mode = BlendMode.values()[m];
//
//                bg2.setBlendMode(mode);
//                debug.setText(mode.toString());
//            }
//        }, KeyCode.F);
    }
    int m = 0;
    BlendMode mode = BlendMode.ADD;

    @Override
    protected void onUpdate() {
        if (once) {
            int i = 0;
            for (Entity e : sceneManager.getEntities(Type.PLAYER)) {
                Text text = new Text();
                text.setTranslateX(50);
                text.setTranslateY(50 * (++i));
                text.textProperty().bind(e.<IntegerProperty>getProperty("kills").asString("Kills: [%d]"));
                sceneManager.addUINodes(text);
            }

            once = false;
        }




        if (player.getTranslateX() < 0) {
            player.setTranslateX(getWidth());
        }

        if (player.getTranslateX() > getWidth()) {
            player.setTranslateX(0);
        }

        if (player.getTranslateY() > getHeight()) {
            player.setTranslateY(0);
        }

        if (player.getTranslateY() < 0) {
            player.setTranslateY(getHeight());
        }

        for (Entity e : sceneManager.getEntities(Type.PLAYER)) {
            EnemyControl control = e.getControl(EnemyControl.class);
            if (control != null) {
                Optional<Entity> closest = sceneManager.getEntities(Type.PLAYER).stream()
                    .filter(p -> p != e && p.isVisible())
                    .sorted((e1, e2) -> (int)e1.distance(e) - (int)e2.distance(e))
                    .findFirst();

                closest.ifPresent(control::setTarget);
            }
        }
    }

    private void nextLevel() {
        sceneManager.getEntities(Type.PLAYER).forEach(character -> {
            character.setControlsEnabled(true);
            character.setVisible(true);
            character.setCollidable(true);

            Point2D spawn = character.getProperty("spawn");
            character.setPosition(spawn);
        });

        inputManager.setProcessActions(true);
    }

    private void addPlatform(double x, double y) {
        Entity platform = new Entity(Type.PLATFORM);
        platform.setPosition(x, y);
        platform.setCollidable(true);

        Rectangle rect = new Rectangle(getWidth() / 3, 40);
        rect.setFill(Color.GRAY);
        platform.setGraphics(rect);

        platforms.add(platform);

        sceneManager.addEntities(platform);
    }

    private void addEnemy(double x, double y) {
        Entity enemy = new Entity(Type.PLAYER);
        enemy.setPosition(x, y);
        enemy.setCollidable(true);
        enemy.setProperty("spawn", new Point2D(x, y));
        enemy.setProperty("kills", new SimpleIntegerProperty());

        Rectangle rect = new Rectangle(40, 40);
        rect.setFill(Color.RED);
        enemy.setGraphics(rect);

        enemy.addControl(new PhysicsControl(physics));
        enemy.addControl(new EnemyControl(player));

        enemy.addFXGLEventHandler(Event.SHOOTING, event -> shootEnemyArrow(event.getTarget()));

        sceneManager.addEntities(enemy);
    }

    private void shootPlayerArrow() {
        Entity arrow = new Entity(Type.ARROW);
        arrow.setPosition(player.getCenter());
        arrow.setGraphics(assets.getTexture("arrow.png"));
        arrow.setCollidable(true);
        arrow.setExpireTime(TimerManager.SECOND * 15);
        arrow.setProperty("owner", player);
        arrow.addControl(new ArrowControl(new Point2D(mouse.x, mouse.y).subtract(arrow.getPosition()), getScreenBounds()));

        sceneManager.addEntities(arrow);
    }

    private void shootEnemyArrow(Entity enemy) {
        Entity arrow = new Entity(Type.ARROW);
        arrow.setPosition(enemy.getCenter());
        arrow.setGraphics(assets.getTexture("arrow.png"));
        arrow.setCollidable(true);
        arrow.setExpireTime(TimerManager.SECOND * 15);
        arrow.setProperty("owner", enemy);
        arrow.addControl(new ArrowControl(sceneManager.getClosestEntity(enemy, Type.PLAYER).get().getCenter().subtract(arrow.getPosition()), getScreenBounds()));

        sceneManager.addEntities(arrow);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
