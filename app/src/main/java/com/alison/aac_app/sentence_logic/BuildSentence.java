package com.alison.aac_app.sentence_logic;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BuildSentence {

    private static String narrowDownSentences(List<String> sentences) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder userSentence = new StringBuilder();

        System.out.println("Enter the word in first position: ");
        String userInput = scanner.nextLine().trim().toLowerCase();
        userSentence.append(userInput).append(" ");

        List<String> nextWords = narrowDownByWord(sentences, 0, userInput);
        System.out.println(nextWords);

        boolean empty = nextWords.isEmpty();
        int i = 1;
        while (i < sentences.size() && !empty) {
            System.out.println("Enter the word in position " + (i) + " (q to quit): ");
            userInput = scanner.nextLine().trim().toLowerCase();
            if (!userInput.equals("q")) {
                userSentence.append(userInput).append(" ");
            }
            else {
                break;
            }

            nextWords = narrowDownByWord(sentences, i, userInput);
            if (nextWords.isEmpty()) {
                System.out.println("No matching words found.");
                break;
            } else {
                System.out.println("Next words:");
                for (String word : nextWords) {
                    System.out.println(word);
                }
            }
            i = i + 1;
        }
        scanner.close();
        return userSentence.toString();
    }

    private static List<String> narrowDownByWord(List<String> sentences, int position, String userInput) {
        List<String> nextWords = new ArrayList<>();

        for (String sentence : sentences) {
            String[] words = sentence.split(" ");

            if (words.length > position + 1 && words[position].equalsIgnoreCase(userInput)) {
                nextWords.add(words[position + 1]);
            }
        }

        return nextWords;
    }

}

