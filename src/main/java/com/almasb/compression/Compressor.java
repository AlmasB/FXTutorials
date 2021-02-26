package com.almasb.compression;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public interface Compressor {

    byte[] compress(byte[] data);

    byte[] decompress(byte[] compressedData);
}
