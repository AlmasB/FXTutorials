package com.almasb.tutorial25;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import com.almasb.fxgl.FXGLLogger;
import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.GameSettings;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityType;
import com.almasb.fxgl.search.AStarLogic;
import com.almasb.fxgl.search.AStarNode;

public class FXGLPacmanApp extends GameApplication {

    private enum Type implements EntityType {
        WALL, PLAYER, ENEMY, BACKGROUND
    }

    private enum Action {
        UP(0, -5),
        DOWN(0, 5),
        LEFT(-5, 0),
        RIGHT(5, 0),
        NONE(0, 0);

        final int dx, dy;

        Action(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    private static final int BLOCK_SIZE = 40;
    private static final int ENTITY_SIZE = BLOCK_SIZE - 10;

    private Random random = new Random();

    private List<String> levelData;
    private AStarNode[][] aiGrid = new AStarNode[15][15];

    private AStarLogic ai = new AStarLogic();
    private AStarNode start, target;

    private Entity player;
    private Action action = Action.NONE;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(600);
        settings.setHeight(600);
    }

    @Override
    protected void initAssets() throws Exception {
        levelData = assetManager.loadText("pacman_level.txt");
    }

    @Override
    protected void initGame(Pane gameRoot) {
        addEntities(Entity.noType().setGraphics(new Rectangle(600, 600)));
        initLevel();
        initPlayer();
        initEnemies();
    }

    private void initLevel() {
        int w = levelData.get(0).length();
        for (int i = 0; i < levelData.size(); i++) {
            String line = levelData.get(i);
            for (int j = 0; j < w; j++) {
                char c = line.charAt(j);
                if (c == '1') {
                    Entity wall = new Entity(Type.WALL);
                    wall.setPosition(j * BLOCK_SIZE, i * BLOCK_SIZE);

                    Rectangle rect = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
                    rect.setFill(Color.GREY);
                    wall.setGraphics(rect);

                    addEntities(wall);
                }

                aiGrid[j][i] = new AStarNode(j, i, 0, c == '1' ? 1 : 0);
            }
        }
    }

    private void initPlayer() {
        player = new Entity(Type.PLAYER);
        player.setPosition(300 - ENTITY_SIZE / 2, 300 - ENTITY_SIZE / 2);

        Rectangle rect = new Rectangle(ENTITY_SIZE, ENTITY_SIZE);
        rect.setFill(Color.BLUE);
        player.setGraphics(rect);

        addEntities(player);
    }

    Entity e = new Entity(Type.ENEMY);

    private void initEnemies() {
        e.setPosition(45, 45);

        Rectangle rect = new Rectangle(30, 30);
        rect.setFill(Color.RED);
        e.setGraphics(rect);

        e.addControl(smartAI);

        addEntities(e);

        ///////////////////////////////////////

        Entity e2 = new Entity(Type.ENEMY);
        e2.setPosition(525, 45);

        Rectangle rect2 = new Rectangle(30, 30);
        rect2.setFill(Color.GREEN);
        e2.setGraphics(rect2);

        e2.addControl(randomAI);

        addEntities(e2);
    }

    @Override
    protected void initUI(Pane uiRoot) {}

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

        addKeyTypedBinding(KeyCode.SPACE, () -> {
            System.out.println(saveScreenshot());
        });
    }

    @Override
    protected void onUpdate(long now) {
        moveEntity(player, action);

        if (player.getTranslateX() >= 600) {
            player.setTranslateX(0);
        }

        if (player.getTranslateX() + ENTITY_SIZE <= 0) {
            player.setTranslateX(600 - ENTITY_SIZE);
        }

    }

    private boolean canMove(Entity entity, Action move) {
        entity.translate(move.dx, move.dy);

        boolean isPlayer = entity.isType(Type.PLAYER);
        boolean collision = getEntities(Type.WALL, isPlayer ? Type.ENEMY : Type.PLAYER).stream()
                .anyMatch(e -> e.getBoundsInParent().intersects(entity.getBoundsInParent()));

        if (!collision && !isPlayer) {
            collision = entity.getTranslateX() < 0 || entity.getTranslateX() + 40 > 600
                    || entity.getTranslateY() < 0 || entity.getTranslateY() + 40 > 600;
        }

        entity.translate(-move.dx, -move.dy);
        return !collision;
    }

    private void moveEntity(Entity entity, Action move) {
        if (move == Action.NONE || !canMove(entity, move))
            return;

        entity.translate(move.dx, move.dy);
    }

    private void moveEntity(Entity entity, Point2D position) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.016 * 8 * 2), entity);
        tt.setToX(position.getX());
        tt.setToY(position.getY());
        tt.setInterpolator(Interpolator.LINEAR);
        tt.play();
    }

    long last, last2 = 0;
    Action move = Action.NONE;

    private Control randomAI = (entity, now) -> {
        moveEntity(entity, move);

        if (now - last2 < 1 * SECOND)
            return;

        move = Action.values()[random.nextInt(5)];

        last2 = now;
    };

    private Control smartAI = (entity, now) -> {
        if (now - last < 0.016 * 8 * 2 * SECOND)
            return;

        int x = Math.abs(Math.min(14, (int)(player.getTranslateX() / 40)));
        int y = Math.abs(Math.min(14, (int)(player.getTranslateY() / 40)));
        target = aiGrid[x][y];

        x = Math.abs(Math.min(14, (int)(e.getTranslateX() / 40)));
        y = Math.abs(Math.min(14, (int)(e.getTranslateY() / 40)));
        start = aiGrid[x][y];

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                aiGrid[j][i].setHCost(Math.abs(target.getX() - i) + Math.abs(target.getY() - j));
            }
        }

        List<AStarNode> path = ai.getPath(aiGrid, start, target);
        if (path != null && path.size() > 0) {
            x = path.get(0).getX() * 40;
            y = path.get(0).getY() * 40;

            moveEntity(entity, new Point2D(x + 5, y + 5));
        }
        last = now;
    };

    public static void main(String[] args) {
        launch(args);
    }
}
