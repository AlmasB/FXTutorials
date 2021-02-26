package com.almasb;

import java.util.Scanner;

public class HangmanMain {

    // Lives: O O O
    // Input: x
    // Word: .ell.
    // Not Used: abcd.f..
    // --------------------------

    public static void main(String[] args) {
        System.out.println("Starting game");

        String notUsed = "abcdefghijklmnopqrstuvwxyz";

        String[] words = new String[] { "computer", "mouse", "screen", "display", "language" };

        String randomWord = words[(int) (Math.random() * words.length)];

        System.out.println("The word has " + randomWord.length() + " letters.");

        char[] letters = new char[randomWord.length()];

        for (int i = 0; i < letters.length; i++) {
            letters[i] = '.';
        }

        int lives = 3;

        Scanner scanner = new Scanner(System.in);

        while (lives > 0) {
            System.out.print("Lives: ");

            for (int i = 0; i < lives; i++) {
                System.out.print("O");
            }

            System.out.println();

            System.out.println("Input: ");

            String input = scanner.nextLine();

            char letter = input.charAt(0);

            boolean isGuessCorrect = false;

            for (int i = 0; i < randomWord.length(); i++) {
                char l = randomWord.charAt(i);

                if (l == letter) {
                    letters[i] = l;
                    isGuessCorrect = true;
                }
            }

            if (!isGuessCorrect) {
                lives = lives - 1;
            }

            boolean isGameFinished = true;

            System.out.print("Word: ");

            for (int i = 0; i < letters.length; i++) {
                if (letters[i] == '.') {
                    isGameFinished = false;
                }

                System.out.print(letters[i]);
            }

            System.out.println();

            notUsed = notUsed.replace(letter, '.');

            System.out.println("Not used: " + notUsed);

            System.out.println("--------------------------");

            if (isGameFinished) {
                System.out.println("You won!");
                break;
            }
        }

        if (lives == 0) {
            System.out.println("You lost! The word was: " + randomWord);
        }

        System.out.println("Exiting game");
    }
}
