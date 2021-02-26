package com.almasb.compression;

import java.io.ByteArrayOutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class SimpleCompressor implements Compressor {
    @Override
    public byte[] compress(byte[] data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             DeflaterOutputStream deflater = new DeflaterOutputStream(out)) {

            deflater.write(data);
            deflater.finish();

            return out.toByteArray();
        } catch (Exception e) {
            System.out.println("error: " + e);
        }

        return new byte[0];
    }

    @Override
    public byte[] decompress(byte[] compressedData) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             InflaterOutputStream inflater = new InflaterOutputStream(out)) {

            inflater.write(compressedData);
            inflater.finish();

            return out.toByteArray();
        } catch (Exception e) {
            System.out.println("error: " + e);
        }

        return new byte[0];
    }
}
