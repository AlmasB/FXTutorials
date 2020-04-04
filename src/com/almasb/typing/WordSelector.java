package com.almasb.typing;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class WordSelector {

    // load this from a file
    private String[] words = new String[] {
            "word",
            "selector",
            "compile",
            "integrated",
            "development",
            "environment"
    };

    // filter words that have been used
    public String getNextWord() {
        return words[(int)(Math.random() * words.length)];
    }
}
