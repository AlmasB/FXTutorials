package com.almasb.datavis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class WordExtractor implements DataExtractor {

    private Path file;

    public WordExtractor(Path file) {
        this.file = file;
    }

    @Override
    public List<Entry> extract() {
        try {
            Map<String, Long> map = Files.readAllLines(file)
                    .stream()
                    .flatMap(line -> Arrays.stream(line.split(" +")))
                    .map(word -> word.toLowerCase())
                    .filter(word -> isWord(word))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            List<Entry> entries = new ArrayList<>();

            map.forEach((word, freq) -> entries.add(new Entry(word, freq.intValue())));

            return entries;

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private boolean isWord(String word) {
        if (word.length() < 4)
            return false;

        if (word.isEmpty())
            return false;

        for (char c : word.toCharArray()) {
            if (!Character.isLetter(c))
                return false;
        }

        return true;
    }
}
