package com.almasb.datavis;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class Entry {

    private String word;
    private int frequency;

    public Entry(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public int getFrequency() {
        return frequency;
    }
}
