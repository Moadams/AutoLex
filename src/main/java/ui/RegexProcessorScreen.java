package main.java.ui;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import main.java.controller.FileProcessor;
import main.java.controller.RegexProcessor;
import main.java.controller.TextProcessor;

public class RegexProcessorScreen{
    private TextField searchField;
    private TextField replacementField;
    private TextField frequencyField;
    private TextField sentenceLimitField;
    private TextField filterField;
    private ComboBox<String> actionDropdown;
    private ComboBox<String> regexDropdown;
    private HBox findReplaceLayout;
    private VBox regexLayout;
    private VBox frequencyLayout;
    private VBox sentenceLimitLayout;
    private VBox filterLayout;
    private TextArea inputArea;
    private TextArea resultArea;
    private CheckBox useCustomRegex;
    private TextField customRegexField;

    private final RegexProcessor regexProcessor;
    private final TextProcessor textProcessor;
    private final FileProcessor fileProcessor;

    private static final Map<String, String> regexTemplates = new LinkedHashMap<>() {{
        put("Extract Emails", "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        put("Extract Phone Numbers", "\\+?\\d[\\d\\s()-]{8,}");
        put("Remove HTML Tags", "<[^>]+>");
        put("Extract Dates (dd/mm/yyyy)", "\\b\\d{2}/\\d{2}/\\d{4}\\b");
        put("Extract Capitalized Words", "\\b[A-Z][a-z]+\\b");
        put("Extract Numbers", "\\b\\d+\\b");
        put("Remove Special Characters", "[^a-zA-Z0-9\\s]");
    }};

    private static final List<String> regexActionsTemplate = List.of("Choose an action", "Perform special operation", "Find And Replace All","Word Count","Top Frequent Words","Summarize Text","Filter Lines by Keyword" );

    public RegexProcessorScreen(RegexProcessor regexProcessor, TextProcessor textProcessor, FileProcessor fileProcessor) {
        this.regexProcessor = regexProcessor;
        this.textProcessor = textProcessor;
        this.fileProcessor = fileProcessor;
    }

    public VBox getLayout(){
        VBox processorLayout = new VBox(10);
        processorLayout.setPadding(new Insets(20));
        Text title = new Text("Regex Text Processor");
        title.setFont(Font.font("Arial", 28));
        title.setFill(Color.web("#2c3e50"));

        inputArea = createInputArea();
        createActionDropdown();
        createFindReplaceLayout();
        createRegexLayout();
        createFrequencyLayout();
        createSentenceLimitLayout();
        createFilterLayout();
        resultArea = createResultArea();
        Button applyButton = createApplyButton();
        


        processorLayout.getChildren().addAll(title, inputArea, actionDropdown,frequencyLayout,filterLayout, sentenceLimitLayout, findReplaceLayout, regexLayout, applyButton, resultArea);
        updateUIBasedOnOperation();
        return processorLayout;
    }

    public TextArea createInputArea(){

        inputArea = new TextArea();
        inputArea.setPromptText("Enter or paste your text here...");
        inputArea.setWrapText(true);
        inputArea.setPrefHeight(200);

        return inputArea;
    }

    public TextArea createResultArea(){
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setPromptText("Results will appear here...");
        resultArea.setPrefHeight(200);
        return resultArea;
    }

    public Button createApplyButton(){
        Button applyButton = new Button("Apply");
        applyButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px;");

        applyButton.setOnAction(e -> {
            // get action
            String selectedAction = actionDropdown.getValue();
            
            switch (selectedAction) {
                case "Perform special operation":
                    findAllMatches();
                    break;
                case "Find And Replace All":
                    findAndReplaceAll();
                    break;
                case "Word Count":
                    performWordCount();
                    break;
                case "Top Frequent Words":
                    performTopFrequentWords();
                    break;
                case "Summarize Text":
                    performSummarizeText();
                    break;
                case "Filter Lines by Keyword":
                    filterLinesByKeyword();
                    break;

                default:
                    break;
            }
            
        });

        return applyButton;
    }

    

    public void uploadFile(){
        FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Text File");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );

            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                try {
                    String content = fileProcessor.readFile(selectedFile.getAbsolutePath());
                    inputArea.setText(content);
                }catch(IOException ex){
                    inputArea.setText("Error reading file: " + ex.getMessage());
                }
                
            }
    }

    

    public void createFindReplaceLayout(){
        findReplaceLayout = new HBox(10);

        searchField = new TextField();
        searchField.setPromptText("Enter search text here...");
        searchField.setPrefWidth(200);

        replacementField = new TextField();
        replacementField.setPromptText("Enter replacement text here...");
        replacementField.setPrefWidth(200);

        findReplaceLayout.getChildren().addAll(searchField, replacementField);
    }

    public void createRegexLayout(){
        regexLayout = new VBox(10);

        regexDropdown = new ComboBox<>();
        regexDropdown.getItems().addAll(regexTemplates.keySet());
        regexDropdown.setPromptText("Choose a predefined text operation");
        regexDropdown.setPrefWidth(300);

        useCustomRegex = new CheckBox("Use custom regex");
        customRegexField = new TextField();
        customRegexField.setPromptText("Enter custom regex here...");
        customRegexField.setDisable(true);
        customRegexField.setPrefWidth(300);

        useCustomRegex.selectedProperty().addListener((obs, oldVal, newVal) -> {
            customRegexField.setDisable(!newVal);
        });

        regexLayout.getChildren().addAll(regexDropdown, useCustomRegex, customRegexField);
    }

    public VBox createFrequencyLayout(){
        frequencyLayout = new VBox(5);

        frequencyField = new TextField();
        frequencyField.setPromptText("E.g 10");
        frequencyField.setPrefWidth(200);

        frequencyLayout.getChildren().addAll(frequencyField);
        return frequencyLayout;
    }

    public VBox createSentenceLimitLayout(){
        sentenceLimitLayout = new VBox(5);;
        sentenceLimitField = new TextField();
        sentenceLimitField.setPromptText("Enter sentence limit. e.g 10");
        sentenceLimitField.setPrefWidth(200);
        sentenceLimitLayout.getChildren().addAll(sentenceLimitField);
        return sentenceLimitLayout;
    }

    public VBox createFilterLayout(){
        filterLayout = new VBox(5);
        filterField = new TextField();
        filterField.setPromptText("Enter filter keyword");
        filterField.setPrefWidth(200);
        filterLayout.getChildren().addAll(filterField);
        return filterLayout;
    }

    public void createActionDropdown(){
        actionDropdown = new ComboBox<>();
        actionDropdown.getItems().addAll(regexActionsTemplate);
        actionDropdown.setPrefWidth(200);
        actionDropdown.setValue("Choose an action");
        actionDropdown.setOnAction(e -> updateUIBasedOnOperation());
    }
   
    public void updateUIBasedOnOperation(){
        String selectedAction = actionDropdown.getValue();
        

        switch (selectedAction) {
            case "Choose an action":
                findReplaceLayout.setManaged(false);
                regexLayout.setManaged(false);
                findReplaceLayout.setVisible(false);
                regexLayout.setVisible(false);
                frequencyLayout.setManaged(false);
                frequencyLayout.setVisible(false);
                sentenceLimitLayout.setManaged(false);
                sentenceLimitLayout.setVisible(false);
                filterLayout.setManaged(false);
                filterLayout.setVisible(false);
                break;
            case "Perform special operation":
                findReplaceLayout.setManaged(false);
                regexLayout.setManaged(true);
                regexLayout.setVisible(true);
                findReplaceLayout.setVisible(false);
                frequencyLayout.setManaged(false);
                frequencyLayout.setVisible(false);
                sentenceLimitLayout.setManaged(false);
                sentenceLimitLayout.setVisible(false);
                filterLayout.setManaged(false);
                filterLayout.setVisible(false);
                break;
            case "Find And Replace All":
                findReplaceLayout.setManaged(true);
                regexLayout.setManaged(false);
                regexLayout.setVisible(false);
                findReplaceLayout.setVisible(true);
                frequencyLayout.setManaged(false);
                frequencyLayout.setVisible(false);
                sentenceLimitLayout.setManaged(false);
                sentenceLimitLayout.setVisible(false);
                filterLayout.setManaged(false);
                filterLayout.setVisible(false);
                break;
            case "Top Frequent Words":
                frequencyLayout.setManaged(true);
                frequencyLayout.setVisible(true);
                findReplaceLayout.setManaged(false);
                regexLayout.setManaged(false);
                regexLayout.setVisible(false);
                findReplaceLayout.setVisible(false);
                sentenceLimitLayout.setManaged(false);
                sentenceLimitLayout.setVisible(false);
                filterLayout.setManaged(false);
                filterLayout.setVisible(false);
                break;
            case "Summarize Text":
                frequencyLayout.setManaged(false);
                frequencyLayout.setVisible(false);
                findReplaceLayout.setManaged(false);
                regexLayout.setManaged(false);
                regexLayout.setVisible(false);
                findReplaceLayout.setVisible(false);
                sentenceLimitLayout.setManaged(true);
                sentenceLimitLayout.setVisible(true);
                filterLayout.setManaged(false);
                filterLayout.setVisible(false);
                break;

            case "Filter Lines by Keyword":
                frequencyLayout.setManaged(false);
                frequencyLayout.setVisible(false);
                findReplaceLayout.setManaged(false);
                regexLayout.setManaged(false);
                regexLayout.setVisible(false);
                findReplaceLayout.setVisible(false);
                sentenceLimitLayout.setManaged(false);
                sentenceLimitLayout.setVisible(false);
                filterLayout.setManaged(true);
                filterLayout.setVisible(true);
                break;
            default:
                frequencyLayout.setManaged(false);
                frequencyLayout.setVisible(false);
                findReplaceLayout.setManaged(false);
                regexLayout.setManaged(false);
                regexLayout.setVisible(false);
                findReplaceLayout.setVisible(false);
                sentenceLimitLayout.setManaged(false);
                sentenceLimitLayout.setVisible(false);
                filterLayout.setManaged(false);
                filterLayout.setVisible(false);
                break;
        }

    }

    public void findAllMatches(){
        String text = inputArea.getText();
        String regex = useCustomRegex.isSelected() ? customRegexField.getText() : regexTemplates.get(regexDropdown.getValue());
        
        if (text.isEmpty() || regex == null || regex.isEmpty()) {
            resultArea.setText("Please enter text and select a valid regex.");
            return;
        }

        try {
            List<String> matches = regexProcessor.findMatches(text, regex);
            System.out.println(matches.size());
            resultArea.setText(matches.size() > 0 ? String.join("\n", matches) : "No matches found.");
            
        } catch (Exception ex) {
            resultArea.setText("Invalid regex or error occurred. Please check your input.");
        }
    }

    public void findAndReplaceAll(){
        String text = inputArea.getText();
        String maintext = searchField.getText();
        String replacement = replacementField.getText();

        if (text.isEmpty() || replacement.isEmpty()) {
            resultArea.setText("Please enter text, and enter a replacement.");
            return;
        }

        try {
            String modifiedText = regexProcessor.replaceMatches(text, maintext, replacement);
            resultArea.setText(modifiedText);
            
        } catch (Exception ex) {
            resultArea.setText("Invalid regex or error occurred. Please check your input.");
        }
    }

    public void performWordCount() {
        String text = inputArea.getText();
        if (text.isEmpty()) {
            resultArea.setText("Please enter some text.");
            return;
        }
    
        Map<String, Long> wordCounts = textProcessor.countWords(text);
    
        if (wordCounts.isEmpty()) {
            resultArea.setText("No words found.");
        } else {
            StringBuilder resultBuilder = new StringBuilder("Word Frequencies:\n");
            wordCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()) // sort by frequency
                .forEach(entry -> 
                    resultBuilder.append(entry.getKey())
                                 .append(": ")
                                 .append(entry.getValue())
                                 .append("\n"));
            resultArea.setText(resultBuilder.toString());
        }
    }

    public void performTopFrequentWords() {
        String text = inputArea.getText();
        if (text.isEmpty()) {
            resultArea.setText("Please enter text.");
            return;
        }
    
        int topN = 10; // default
        try {
            String topNInput = frequencyField.getText();
            if (!topNInput.isEmpty()) {
                topN = Integer.parseInt(topNInput);
                if (topN <= 0) throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            resultArea.setText("Invalid number for Top N. Please enter a positive integer.");
            return;
        }
    
        List<Map.Entry<String, Long>> topWords = textProcessor.getTopFrequentWords(text, topN);
    
        if (topWords.isEmpty()) {
            resultArea.setText("No words found.");
        } else {
            StringBuilder resultBuilder = new StringBuilder("Top " + topN + " Frequent Words:\n");
            for (Map.Entry<String, Long> entry : topWords) {
                resultBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            resultArea.setText(resultBuilder.toString());
        }
    }

    public void performSummarizeText() {
        String text = inputArea.getText();
        if (text.isEmpty()) {
            resultArea.setText("Please enter text.");
            return;
        }
    
        int sentenceLimit = 3; // Default
        try {
            String limitInput = sentenceLimitField.getText();
            if (!limitInput.isEmpty()) {
                sentenceLimit = Integer.parseInt(limitInput);
                if (sentenceLimit <= 0) throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            resultArea.setText("Invalid sentence limit. Please enter a positive number.");
            return;
        }
    
        String summary = textProcessor.summarizeText(text, sentenceLimit);
        resultArea.setText(summary.isEmpty() ? "No sentences found." : summary);
    }
    
    public void filterLinesByKeyword() {
        String text = inputArea.getText();
        String keyword = filterField.getText();
    
        if (text.isEmpty() || keyword == null || keyword.isEmpty()) {
            resultArea.setText("Please enter both text and keyword.");
            return;
        }
    
        List<String> filtered = textProcessor.filterLines(text, keyword);
        resultArea.setText(filtered.isEmpty() ? "No matching lines found." : String.join("\n", filtered));
    }
    
    public void saveFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Output File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("output.txt");

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                fileProcessor.writeFile(file.getAbsolutePath(), resultArea.getText());
            } catch (IOException ex) {
                resultArea.setText("Failed to save file: " + ex.getMessage());
            }
        }
    }
        
}