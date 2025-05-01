package main.java.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexProcessor{
    /**
     * Finds all matches of a given regex pattern in the input text
     *
     * @param text the input text to search in
     * @param regex the regex pattern to search for
     * @return a list of strings containing all matches of the regex pattern in the input text
     */
    public List<String> findMatches(String text, String regex){
        ArrayList<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        
        while(matcher.find()){
            matches.add(matcher.group());
        }
        return matches;
    }

    /**
     * Replaces all matches of a given regex pattern in the input text with a given replacement
     *
     * @param inputText the input text to replace matches in
     * @param regex the regex pattern to search for
     * @param replacement the replacement for the matches
     * @return the modified input text with all matches replaced
     */
    public String replaceMatches(String inputText, String regex, String replacement){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(inputText).replaceAll(replacement);
    }

    /**
     * Finds out if a given regex pattern matches any substring in the input text
     *
     * @param text the input text to search in
     * @param regex the regex pattern to search for
     * @return true if a match is found, false otherwise
     */
    public boolean isMatchFound(String text, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    /**
     * Checks if a given regex pattern is valid
     *
     * @param pattern the regex pattern to check
     * @return true if the pattern is valid, false otherwise
     */
    public boolean isValidRegex(String pattern) {
        try {
            Pattern.compile(pattern);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }
}