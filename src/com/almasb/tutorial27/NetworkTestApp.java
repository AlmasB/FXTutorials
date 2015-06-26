package com.almasb.tutorial27;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import com.almasb.fxgl.FXGLLogger;
import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityType;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Server;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class NetworkTestApp extends GameApplication {

    private enum Type implements EntityType {
        PLAYER1, PLAYER2
    }

    private Server server = new Server();
    private Client client = new Client("127.0.0.1");

    private boolean isHost = false;
    private boolean isConnected = false;

    private Entity player1 = new Entity(Type.PLAYER1);
    private Entity player2 = new Entity(Type.PLAYER2);

    private Map<KeyCode, Boolean> keys = new HashMap<>();

    private Queue<RequestMessage> requestQueue = new ConcurrentLinkedQueue<>();
    private Queue<DataMessage> updateQueue = new ConcurrentLinkedQueue<>();

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(600);
        settings.setHeight(600);
    }

    @Override
    protected void initAssets() throws Exception {}

    @Override
    protected void initGame(Pane gameRoot) {
        initNetworking();

        player1.setPosition(100, 100);
        player1.setGraphics(new Rectangle(40, 40));

        player2.setPosition(500, 500);
        player2.setGraphics(new Rectangle(40, 40));

        addEntities(player1, player2);
    }

    @Override
    protected void initUI(Pane uiRoot) {}

    private void initNetworking() {
        if (isHost) {
            server.addParser(RequestMessage.class, data -> requestQueue.offer(data));
            server.addParser(String.class, data -> isConnected = true);
            server.start();
        }
        else {
            client.addParser(DataMessage.class, data -> updateQueue.offer(data));

            try {
                client.connect();
                client.send("Hi");
            }
            catch (Exception e) {
                log.severe(FXGLLogger.errorTraceAsString(e));
                exit();
            }
        }
    }

    @Override
    protected void initInput() {
        if (isHost) {
            addKeyPressBinding(KeyCode.W, () -> {
                player1.translate(0, -5);
            });
            addKeyPressBinding(KeyCode.S, () -> {
                player1.translate(0, 5);
            });
            addKeyPressBinding(KeyCode.A, () -> {
                player1.translate(-5, 0);
            });
            addKeyPressBinding(KeyCode.D, () -> {
                player1.translate(5, 0);
            });
        }
        else {
            initKeys(KeyCode.W, KeyCode.S, KeyCode.A, KeyCode.D, KeyCode.ESCAPE);
        }
    }

    private void initKeys(KeyCode... codes) {
        for (KeyCode k : codes) {
            keys.put(k, false);
            addKeyPressBinding(k, () -> keys.put(k, true));
        }
    }

    @Override
    protected void onUpdate(long now) {
        if (isHost) {
            if (!isConnected)
                return;

            RequestMessage data = requestQueue.poll();
            if (data != null) {
                for (KeyCode key : data.keys) {
                    if (key == KeyCode.W) {
                        player2.translate(0, -5);
                    }
                    else if (key == KeyCode.S) {
                        player2.translate(0, 5);
                    }
                    else if (key == KeyCode.A) {
                        player2.translate(-5, 0);
                    }
                    else if (key == KeyCode.D) {
                        player2.translate(5, 0);
                    }
                    else if (key == KeyCode.ESCAPE) {
                        exit();
                    }
                }
            }

            try {
                server.send(new DataMessage(player1.getTranslateX(), player1.getTranslateY(),
                        player2.getTranslateX(), player2.getTranslateY()));
            }
            catch (Exception e) {
                log.warning("Failed to send message: " + e.getMessage());
                exit();
            }
        }
        else {
            DataMessage data = updateQueue.poll();
            if (data != null) {
                player1.setPosition(data.x1, data.y1);
                player2.setPosition(data.x2, data.y2);
            }

            KeyCode[] codes = keys.keySet()
                    .stream()
                    .filter(k -> keys.get(k))
                    .collect(Collectors.toList())
                    .toArray(new KeyCode[0]);

            try {
                client.send(new RequestMessage(codes));

                if (keys.get(KeyCode.ESCAPE)) {
                    exit();
                }
            }
            catch (Exception e) {
                log.warning("Failed to send messsage: " + e.getMessage());
                exit();
            }

            keys.forEach((key, value) -> keys.put(key, false));
        }
    }

    @Override
    protected void onExit() {
        if (isHost) {
            server.stop();
        }
        else {
            client.disconnect();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
