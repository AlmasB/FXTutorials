package com.almasb.chat;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class Encryptor {

    public byte[] enc(byte[] input) {
        byte[] output = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = (byte) (input[i] + 1);
        }

        return output;
    }

    public byte[] dec(byte[] input) {
        byte[] output = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = (byte) (input[i] - 1);
        }

        return output;
    }
}
