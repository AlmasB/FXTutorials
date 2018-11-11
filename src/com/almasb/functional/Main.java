package com.almasb.functional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class Main {

    public static void main(String[] args) {

        List<String> data = Arrays.asList(
                "Adam", "Bob", "Carol", "Dave", "Eve", "Francis", "Gregg", "Helen",
                "Ann", "Chris", "David", "Daniel", "George"
        );

        // filter
//        data.stream()
//                .filter(name -> name.startsWith("D"))
//                .forEach(name -> System.out.println(name));

        // map
//        data.stream()
//                .mapToInt(name -> name.length())
//                .forEach(numLetters -> System.out.println(numLetters));

        // collect
        Map<Character, List<String>> groupings = data.stream()
                .collect(Collectors.groupingBy(name -> name.charAt(0)));

        groupings.forEach((firstLetter, names) -> {
            System.out.println("" + firstLetter + ": " + names);
        });
    }
}
