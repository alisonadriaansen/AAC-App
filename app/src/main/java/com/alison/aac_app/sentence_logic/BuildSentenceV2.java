package com.alison.aac_app.sentence_logic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BuildSentenceV2 {

    public ArrayList<String> narrowDownSentences(List<String> sentences, List<String> userList) {
        ArrayList<String> sentencesRes = new ArrayList<>();
        List<String> filteredSentences = filterSentences(sentences,userList);
        if (filteredSentences.isEmpty()) {
            sentencesRes.add("No sentences found.");
        } else {
            sentencesRes.addAll(filteredSentences);
        }
        return sentencesRes;
    }

    private static List<String> filterSentences(List<String> sentences, List<String> userWords) {
        List<String> filteredSentences = new ArrayList<>();

        List<String> lowercaseUserWords = userWords.stream()
                .map(String::toLowerCase).collect(Collectors.toList());

        for (String sentence : sentences) {
            boolean containsAllWords = true;
            String lowercaseSentence = sentence.toLowerCase();
            String[] wordsInSentence = lowercaseSentence.split("\\s+");
            for (String word : lowercaseUserWords) {
                boolean containsWord = false;
                for (String sentenceWord : wordsInSentence) {
                    if (sentenceWord.equals(word)) {
                        containsWord = true;
                        break;
                    }
                }
                if (!containsWord) {
                    containsAllWords = false;
                    break;
                }
            }
            if (containsAllWords) {
                filteredSentences.add(sentence);
            }
        }
        return filteredSentences;
    }


}