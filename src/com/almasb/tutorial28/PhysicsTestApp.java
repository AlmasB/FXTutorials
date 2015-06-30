package com.almasb.tutorial28;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.GameSettings;
import com.almasb.fxgl.asset.Texture;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityType;
import com.almasb.fxgl.physics.PhysicsEntity;
import com.almasb.fxgl.physics.PhysicsManager;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PhysicsTestApp extends GameApplication {

    private enum Type implements EntityType {
        PLAYER, BALL, BULLET, SCREEN, NON_PHYSICS_OBJECT, PLATFORM
    }

    private PhysicsEntity ball;
    private PhysicsEntity player;

    private Entity object;

    private Texture ballTexture;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Mallet - Puck Simulation");
    }

    @Override
    protected void initAssets() throws Exception {
        ballTexture = assetManager.loadTexture("ball.png");
    }

    @Override
    protected void initGame(Pane gameRoot) {
        physicsManager.setGravity(0, 0);

        initScreenBounds();
        initPlayer();
        initBall();
        initDummy();
        initPlatform();

        addCollisionHandler(Type.PLAYER, Type.NON_PHYSICS_OBJECT, (p, o) -> {
            System.out.println(p.getTypeAsString() + " collided with " + o.getTypeAsString());
        });

        addCollisionHandler(Type.PLAYER, Type.BALL, (p, b) -> {
            System.out.println(p.getTypeAsString() + " collided with " + b.getTypeAsString());
        });

        addCollisionHandler(Type.BULLET, Type.BALL, (bullet, ball) -> {
            //removeEntity(bullet);
        });

        addCollisionHandler(Type.BULLET, Type.SCREEN, (bullet, ball) -> {
            removeEntity(bullet);
        });
    }

    private void initScreenBounds() {
        PhysicsEntity top = new PhysicsEntity(Type.SCREEN);
        top.setPosition(0, -10);
        top.setGraphics(new Rectangle(getWidth(), 10));

        PhysicsEntity bot = new PhysicsEntity(Type.SCREEN);
        bot.setPosition(0, getHeight());
        bot.setGraphics(new Rectangle(getWidth(), 10));

        PhysicsEntity left = new PhysicsEntity(Type.SCREEN);
        left.setPosition(-10, 0);
        left.setGraphics(new Rectangle(10, getHeight()));

        PhysicsEntity right = new PhysicsEntity(Type.SCREEN);
        right.setPosition(getWidth(), 0);
        right.setGraphics(new Rectangle(10, getHeight()));

        addEntities(top, bot, left, right);
    }

    private void initPlayer() {
        player = new PhysicsEntity(Type.PLAYER);
        player.setPosition(100, 300);
        player.setGraphics(new Rectangle(40, 60));
        player.setBodyType(BodyType.KINEMATIC);
        player.setUsePhysics(true);

        addEntities(player);
    }

    private void initBall() {
        FixtureDef fd = new FixtureDef();
        fd.restitution = 0.5f;
        fd.density = 0.05f;
        fd.shape = new CircleShape();
        fd.shape.setRadius(PhysicsManager.toMeters(30));

        ballTexture.setFitWidth(60);
        ballTexture.setFitHeight(60);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;

        ball = new PhysicsEntity(Type.BALL);
        ball.setPosition(300, 100);
        ball.setGraphics(ballTexture);
        ball.setFixtureDef(fd);
        ball.setBodyDef(bodyDef);

        addEntities(ball);
    }

    private void initDummy() {
        Rectangle rect = new Rectangle(40, 60);
        rect.setFill(Color.GREEN);

        object = new Entity(Type.NON_PHYSICS_OBJECT);
        object.setPosition(100, 200);
        object.setGraphics(rect);
        object.setUsePhysics(true);

        addEntities(object);
    }

    private void initPlatform() {
        PhysicsEntity platform = new PhysicsEntity(Type.PLATFORM);
        platform.setPosition(300, 300);
        platform.setGraphics(new Rectangle(100, 10));

        addEntities(platform);
    }

    private void shoot() {
        Point2D vel = new Point2D(mouse.x, mouse.y).subtract(player.getPosition()).multiply(0.05);

        PhysicsEntity bullet = new PhysicsEntity(Type.BULLET);
        bullet.setGraphics(new Rectangle(10, 1));

        FixtureDef fd = new FixtureDef();
        fd.density = 1f;
        bullet.setFixtureDef(fd);

        BodyDef bodyDef = new BodyDef();
        bodyDef.bullet = true;
        bodyDef.angle = -(float)(Math.atan2(vel.getY(), vel.getX()));
        bodyDef.type = BodyType.DYNAMIC;
        bullet.setBodyDef(bodyDef);

        bullet.setPosition(player.getPosition().add(20, 30).add(vel.normalize().multiply(40)));

        addEntities(bullet);

        bullet.setLinearVelocity(vel);
    }

    @Override
    protected void initUI(Pane uiRoot) {}

    @Override
    protected void initInput() {
        addKeyPressBinding(KeyCode.D, () -> {
            object.translate(5, 0);
            player.setLinearVelocity(new Point2D(5, 0));
        });

        addKeyTypedBinding(KeyCode.F, () -> {
            shoot();
        });
    }

    @Override
    protected void onUpdate(long now) {
        if (mouse.leftPressed) {
            player.setLinearVelocity(new Point2D(mouse.x, mouse.y).subtract(player.getPosition()).multiply(0.1));
        }
        else {
            player.setLinearVelocity(new Point2D(0, 0));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
