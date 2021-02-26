package com.almasb.nativewindow;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class NativeWindow {

    static {
        System.loadLibrary("SDL2");
        System.loadLibrary("native-window");
    }

    private int x;
    private int y;
    private int w;
    private int h;

    public NativeWindow(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void show() {
        new Thread(() -> {
            show(x, y, w, h);
        }).start();
    }

    private native void show(int x, int y, int w, int h);

    public native void fillRect(int x, int y, int w, int h);
}
