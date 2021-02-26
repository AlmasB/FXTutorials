package com.almasb.steg;

import javafx.scene.image.Image;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public interface Encoder {

    Image encode(Image image, String message);
}
