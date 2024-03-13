package com.alison.aac_app.sentence_logic;

import java.util.ArrayList;
import java.util.List;


public class BuildSentenceV2 {

    public ArrayList<String> narrowDownSentences(List<String> sentences, List<String> userList) {
        ArrayList<String> sentencesRes = new ArrayList<>();
        List<String> filteredSentences = filterSentences(sentences,userList);
        if (filteredSentences.isEmpty()) {
            sentencesRes.add("No matching sentences found.");
        } else {
            sentencesRes.addAll(filteredSentences);
        }
        return sentencesRes;
    }

    private static List<String> filterSentences(List<String> sentences, List<String> userWords) {
        List<String> filteredSentences = new ArrayList<>();

        for (String sentence : sentences) {
            boolean containsAllWords = true;
            for (String word : userWords) {
                if (!sentence.toLowerCase().contains(word.toLowerCase() + " ")) {
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