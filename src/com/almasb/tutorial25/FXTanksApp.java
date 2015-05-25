package com.almasb.tutorial25;

import java.util.Random;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.GameSettings;
import com.almasb.fxgl.asset.Assets;
import com.almasb.fxgl.asset.Texture;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityType;

public class FXTanksApp extends GameApplication {

    private enum Type implements EntityType {
        PLAYER, ENEMY, PLAYER_BULLET, ENEMY_BULLET, WALL, BACKGROUND
    }

    private enum Action {
        UP(0, -5, 0),
        DOWN(0, 5, 180),
        LEFT(-5, 0, -90),
        RIGHT(5, 0, 90),
        NONE(0, 0, 0);

        final int dx, dy, angle;

        Action(int dx, int dy, int angle) {
            this.dx = dx;
            this.dy = dy;
            this.angle = angle;
        }
    }

    private static final int BLOCK_SIZE = 40;

    private Random random = new Random();

    private Assets assets;

    private Entity player;
    private Action action = Action.NONE;

    private long lastTimeShot = 0;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("FXGLTanks");
        settings.setVersion("1.0");
    }

    @Override
    protected void initAssets() throws Exception {
        assets = assetManager.cache();
    }

    @Override
    protected void initGame(Pane gameRoot) {
        Entity bg = new Entity("bg").setGraphics(new Rectangle(1280, 720));
        addEntities(bg);

        for (int i = 0; i < 30; i++) {
            Entity wall = new Entity(Type.WALL);
            wall.setUsePhysics(true);
            wall.setPosition(random.nextInt(25) * BLOCK_SIZE + 120, random.nextInt(15) * BLOCK_SIZE + 40);
            Texture t = assets.getTexture("tank_wall.png");
            t.setFitHeight(40);
            t.setFitWidth(40);
            wall.setGraphics(t);
            addEntities(wall);
        }

        initCollisions();
        spawnPlayer();
        spawnEnemy(0, 0);
        spawnEnemy(1200, 600);
        spawnEnemy(1200, 0);
    }

    private void initCollisions() {
        addCollisionHandler(Type.PLAYER_BULLET, Type.WALL, (bullet, wall) -> {
            removeEntity(bullet);
        });

        addCollisionHandler(Type.ENEMY_BULLET, Type.WALL, (bullet, wall) -> {
            removeEntity(bullet);
        });

        addCollisionHandler(Type.PLAYER_BULLET, Type.ENEMY_BULLET, (playerBullet, enemyBullet) -> {
            removeEntity(playerBullet);
            removeEntity(enemyBullet);
        });

        addCollisionHandler(Type.ENEMY_BULLET, Type.PLAYER, (bullet, player) -> {
            removeEntity(bullet);
            System.out.println("Player hit");
        });

        addCollisionHandler(Type.PLAYER_BULLET, Type.ENEMY, (bullet, enemy) -> {
            removeEntity(bullet);
            removeEntity(enemy);
        });
    }

    @Override
    protected void initUI(Pane uiRoot) {

    }

    @Override
    protected void initInput() {
        addKeyPressBinding(KeyCode.W, () -> {
            action = Action.UP;
        });

        addKeyPressBinding(KeyCode.S, () -> {
            action = Action.DOWN;
        });

        addKeyPressBinding(KeyCode.A, () -> {
            action = Action.LEFT;
        });

        addKeyPressBinding(KeyCode.D, () -> {
            action = Action.RIGHT;
        });

        addKeyPressBinding(KeyCode.SPACE, () -> {
            if (currentTime - lastTimeShot >= 0.33 * SECOND) {
                spawnBullet(player);
                lastTimeShot = currentTime;
            }
        });
    }

    @Override
    protected void onUpdate(long now) {
        moveEntity(player, action);

        action = Action.NONE;
    }

    private void spawnPlayer() {
        player = new Entity(Type.PLAYER);
        player.setPosition(0, 600);
        player.setUsePhysics(true);

        player.setProperty("vx", 0);
        player.setProperty("vy", -1);
        player.getTransforms().setAll(new Rotate(0, 20, 20));

        Texture t = assets.getTexture("tank_player.png");
        t.setFitHeight(40);
        t.setFitWidth(40);
        player.setGraphics(t);

        addEntities(player);
    }

    private void spawnEnemy(double x, double y) {
        Entity enemy = new Entity(Type.ENEMY);
        enemy.setUsePhysics(true);
        enemy.setPosition(x, y);
        enemy.setProperty("vx", 0).setProperty("vy", -1);
        Texture t = assets.getTexture("tank_enemy.png");
        t.setFitHeight(40);
        t.setFitWidth(40);
        enemy.setGraphics(t);
        enemy.addControl(new Control() {
            private long lastTimeChangedTarget = 0, lastTimeShotTarget = 0;
            private Action move = Action.NONE;

            @Override
            public void onUpdate(Entity entity, long now) {
                if (now - lastTimeChangedTarget >= 1.5 * SECOND) {
                    move = Action.values()[random.nextInt(5)];
                    lastTimeChangedTarget = now;
                }

                if (now - lastTimeShotTarget >= 1 * SECOND) {
                    spawnBullet(entity);
                    lastTimeShotTarget = now;
                }

                moveEntity(entity, move);
            }
        });

        addEntities(enemy);
    }

    private void spawnBullet(Entity entity) {
        // 20 half entity, 8 half bullet
        Entity bullet = new Entity(entity.isType(Type.PLAYER) ? Type.PLAYER_BULLET : Type.ENEMY_BULLET)
            .setPosition(entity.getTranslateX() + 20 - 8, entity.getTranslateY() + 20 - 8)
            .setGraphics(assets.getTexture("tank_bullet.png"))
            .setUsePhysics(true);

        final int vx = entity.getProperty("vx");
        final int vy = entity.getProperty("vy");

        bullet.addControl((e, now) -> {
            e.translate(vx * 10, vy * 10);

            if (e.getTranslateX() < 0 || e.getTranslateX() > 1280
                    || e.getTranslateY() < 0 || e.getTranslateY() > 720)
                removeEntity(e);
        });

        addEntities(bullet);
    }

    private boolean canMove(Entity entity, Action move) {
        entity.translate(move.dx, move.dy);

        boolean isPlayer = entity.isType(Type.PLAYER);
        boolean collision = getEntities(Type.WALL, isPlayer ? Type.ENEMY : Type.PLAYER).stream()
                .anyMatch(e -> e.getBoundsInParent().intersects(entity.getBoundsInParent()));

        if (!collision) {
            collision = entity.getTranslateX() < 0 || entity.getTranslateX() + 40 > 1280
                    || entity.getTranslateY() < 0 || entity.getTranslateY() + 40 > 720;
        }

        entity.translate(-move.dx, -move.dy);
        return !collision;
    }

    private void moveEntity(Entity entity, Action move) {
        if (move == Action.NONE || !canMove(entity, move))
            return;
        // 20, 20 pivot point (center) coz sprite is 40x40
        entity.getTransforms().setAll(new Rotate(move.angle, 20, 20));
        entity.translate(move.dx, move.dy);
        entity.setProperty("vx", move.dx / 5);
        entity.setProperty("vy", move.dy / 5);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
