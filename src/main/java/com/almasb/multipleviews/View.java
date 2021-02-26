package com.almasb.multipleviews;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public enum View {
    LOGIN("login.fxml"),
    MAIN("main.fxml"),
    ABOUT("about.fxml");

    private String fileName;

    View(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
