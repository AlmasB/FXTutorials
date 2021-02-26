package com.almasb.chat;

import java.io.Serializable;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class DataPacket implements Serializable {

    private byte[] rawBytes;

    public DataPacket(byte[] rawBytes) {
        this.rawBytes = rawBytes;
    }

    public byte[] getRawBytes() {
        return rawBytes;
    }
}
