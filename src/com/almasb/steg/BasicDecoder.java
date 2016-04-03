package com.almasb.steg;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.util.Pair;

import java.util.stream.IntStream;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class BasicDecoder implements Decoder {
    @Override
    public String decode(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        PixelReader reader = image.getPixelReader();

        boolean[] bits = new boolean[width * height];

        IntStream.range(0, width * height)
                .mapToObj(i -> new Pair<>(i, reader.getArgb(i % width, i / width)))
                .forEach(pair -> {
                    String binary = Integer.toBinaryString(pair.getValue());
                    bits[pair.getKey()] = binary.charAt(binary.length() - 1) == '1';
                });

        // decode length
        int length = 0;
        for (int i = 0; i < 32; i++) {
            if (bits[i]) {
                length |= (1 << (31 - i));
            }
        }

        byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < 8; j++) {
                if (bits[32 + i*8 + j]) {
                    data[i] |= (1 << (7 - j));
                }
            }
        }

        return new String(data);
    }
}
