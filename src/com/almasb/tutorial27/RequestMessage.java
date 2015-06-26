package com.almasb.tutorial27;

import javafx.scene.input.KeyCode;

public class RequestMessage implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public final KeyCode[] keys;

    public RequestMessage(KeyCode... keys) {
        this.keys = keys;
    }
}
