package com.almasb.steg;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class Controller {

    private Model model;

    public Controller(Model model) {
        this.model = model;
    }

    private ImageView originalView, modifiedView;
    private TextField fieldMessage;

    public void injectUI(ImageView original, ImageView modified, TextField fieldMessage) {
        this.originalView = original;
        this.modifiedView = modified;
        this.fieldMessage = fieldMessage;
    }

    public void onEncode() {
        Image modified = model.encode(originalView.getImage(), fieldMessage.getText());
        modifiedView.setImage(modified);
    }

    public void onDecode() {
        String message = model.decode(modifiedView.getImage());
        System.out.println(message);
    }
}
