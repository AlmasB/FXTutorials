package com.almasb.tutorial13;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WordReader {
    private static final String fileName = "/res/words.txt";

    private ArrayList<String> words = new ArrayList<String>();

    public WordReader() {
        try (InputStream in = getClass().getResourceAsStream(fileName);
                BufferedReader bf = new BufferedReader(new InputStreamReader(in))) {

            String line = "";
            while ((line = bf.readLine()) != null)
                words.add(line);
        }
        catch (Exception e) {
            System.out.println("Couldn't find/read file: " + fileName);
            System.out.println("Error message: " + e.getMessage());
        }
    }

    public String getRandomWord() {
        if (words.isEmpty()) return "NO-DATA";
        return words.get((int)(Math.random()*words.size()));
    }
}
