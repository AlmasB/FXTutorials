package com.almasb.tutorial28;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityType;
import com.almasb.fxgl.physics.PhysicsEntity;
import com.almasb.fxgl.physics.PhysicsManager;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class PhysicsSimulation extends GameApplication {

    private enum Type implements EntityType {
        PLAYER, PUCK, SCREEN, NON_PHYSICS_OBJECT
    }

    private PhysicsEntity puck;
    private PhysicsEntity player;

    private Entity object;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Mallet - Puck Simulation");
    }

    @Override
    protected void initAssets() throws Exception {}

    @Override
    protected void initGame(Pane gameRoot) {
        physicsManager.setGravity(0, 0);

        player = new PhysicsEntity(Type.PLAYER);
        player.setPosition(100, 300);
        player.setGraphics(new Rectangle(40, 60));
        player.setBodyType(BodyType.KINEMATIC);
        player.setUsePhysics(true);

        FixtureDef fd = new FixtureDef();
        fd.density = 0.005f;
        fd.friction = 0.05f;
        fd.restitution = 0.5f;
        fd.shape = new CircleShape();
        fd.shape.setRadius(PhysicsManager.toMeters(30));

        puck = new PhysicsEntity(Type.PUCK);
        puck.setPosition(300, 100);
        puck.setGraphics(new Circle(30));
        //puck.setUsePhysics(true);
        puck.setFixtureDef(fd);
        puck.setBodyType(BodyType.DYNAMIC);

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

        object = new Entity(Type.NON_PHYSICS_OBJECT);
        object.setPosition(100, 200);
        object.setGraphics(new Rectangle(40, 60));
        object.setUsePhysics(true);

        addEntities(puck, player, top, bot, left, right, object);

        addCollisionHandler(Type.PLAYER, Type.NON_PHYSICS_OBJECT, (p, o) -> {
            System.out.println(p.getPosition() + " " + o.getPosition());
        });

        addCollisionHandler(Type.PLAYER, Type.PUCK, (p, o) -> {
            System.out.println(p.getPosition() + " " + o.getPosition());
        });
    }

    @Override
    protected void initUI(Pane uiRoot) {}

    @Override
    protected void initInput() {
        addKeyPressBinding(KeyCode.D, () -> {
            object.translate(5, 0);
            player.setLinearVelocity(new Point2D(5, 0));
        });
    }

    @Override
    protected void onUpdate(long now) {
        if (mouse.leftPressed) {
            player.setLinearVelocity(new Point2D(mouse.x, mouse.y).subtract(player.getPosition()).multiply(0.1));
        }
        else if (mouse.rightPressed) {
            removeEntity(puck);
        }
        else {
            player.setLinearVelocity(new Point2D(0, 0));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
