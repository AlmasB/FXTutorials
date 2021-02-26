package com.almasb.tutorial3;

public class Gun {

    private FireMode mode = FireMode.SINGLE;
    private Clip clip = new Clip();

    private void fireSingleBullet() {
        if (!clip.isEmpty())
            clip.incBullets(-1);
    }

    public void fire() {
        switch (mode) {
            case SINGLE:
                fireSingleBullet();
                break;
            case TRIPLE:
                for (int i = 0; i < 3; i++) {
                    fireSingleBullet();
                }
                break;
            case AUTOMATIC:
                while (!clip.isEmpty())
                    fireSingleBullet();
                break;
        }
    }

    public void reload() {
        clip = new Clip();
    }

    public void setFireMode(FireMode mode) {
        this.mode = mode;
    }

    public void printState() {
        System.out.println("FireMode is: " + mode.toString());
        System.out.println("Number of bullets: " + clip.getBullets());
    }

    public Clip getClip() {
        return clip;
    }
}