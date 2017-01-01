package com.almasb.tutorial3;

public class Clip {
    private static final int MAX_BULLETS = 15;
    private int bullets = MAX_BULLETS;

    public void incBullets(int value) {
        bullets += value;
    }

    public int getBullets() {
        return bullets;
    }

    public boolean isEmpty() {
        return bullets == 0;
    }
}