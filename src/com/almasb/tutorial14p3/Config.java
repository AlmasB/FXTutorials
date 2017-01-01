package com.almasb.tutorial14p3;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public final class Config {

    private static Config instance = new Config();

    public static Config getInstance() {
        return instance;
    }

    private Config() {}

    static {
        FXMLLoader loader = new FXMLLoader(instance.getClass().getResource("config.fxml"));
        loader.setController(instance);
        try {
            loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private int appWidth;
    @FXML
    private int appHeight;
    @FXML
    private int blockSize;
    @FXML
    private int playerSize;

    public int getAppWidth() {
        return appWidth;
    }

    public int getAppHeight() {
        return appHeight;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getPlayerSize() {
        return playerSize;
    }
}
