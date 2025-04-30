package main.java.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class TextProcessor{
    /**
     * Counts the occurrences of each word in the given text.
     *
     * @param text the input text to process
     * @return a map where keys are words and values are their respective counts
     */
    public Map<String, Long> countWords(String text){
        return Arrays.stream(text.toLowerCase().split("\\W+")).collect(Collectors.groupingBy(w -> w, Collectors.counting()));
    }

    
    /**
     * Retrieves the top N most frequent words from the input text.
     *
     * @param inputText the text to analyze
     * @param topN the number of top frequent words to retrieve
     * @return a list of map entries where keys are words and values are their respective counts, sorted by frequency in descending order
     */

    public List<Map.Entry<String, Long>> getTopFrequentWords(String inputText, int topN) {
        return countWords(inputText).entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .collect(Collectors.toList());
    }

    /**
     * Summarizes the given text by extracting the top N sentences from it. The top N sentences are the first N sentences in the text.
     *
     * @param inputText the text to summarize
     * @param sentenceLimit the number of sentences to include in the summary
     * @return a string containing the summary text
     */
    public String summarizeText(String inputText, int sentenceLimit) {
        String[] sentences = inputText.split("(?<=[.!?])\\s+");
        return Arrays.stream(sentences)
                .limit(sentenceLimit)
                .collect(Collectors.joining(" "));
    }

    /**
     * Filters the lines in the input text that contain the given keyword.
     *
     * @param inputText the text to filter
     * @param keyword the keyword to search for
     * @return a list of lines that contain the keyword
     */
    public List<String> filterLines(String inputText, String keyword) {
        return Arrays.stream(inputText.split("\n"))
                .filter(line -> line.toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}