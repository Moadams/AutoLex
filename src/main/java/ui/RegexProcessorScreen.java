package main.java.ui;

// JavaFX Imports
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
// External Imports
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Internal Imports
import main.java.controller.FileProcessor;
import main.java.controller.RegexProcessor;
import main.java.controller.TextProcessor;
import main.java.utils.LoggerUtility;

public class RegexProcessorScreen{

    // UI Components
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

    // Dependencies
    private final RegexProcessor regexProcessor;
    private final TextProcessor textProcessor;
    private final FileProcessor fileProcessor;
    private final LoggerUtility loggerUtility;

    // Regex Templates
    private static final Map<String, String> regexTemplates = new LinkedHashMap<>() {{
        put("Extract Emails", "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        put("Extract Phone Numbers", "\\+?\\d[\\d\\s()-]{8,}");
        put("Extract Dates (dd/mm/yyyy)", "\\b\\d{2}/\\d{2}/\\d{4}\\b");
        put("Extract Numbers", "\\b\\d+\\b");
    }};

    // Actions Templates
    private static final List<String> regexActionsTemplate = List.of("Choose an action", "Perform special operation", "Find And Replace All","Word Count","Top Frequent Words","Summarize Text","Filter Lines by Keyword" );

    public RegexProcessorScreen(RegexProcessor regexProcessor, TextProcessor textProcessor, FileProcessor fileProcessor) {
        this.regexProcessor = regexProcessor;
        this.textProcessor = textProcessor;
        this.fileProcessor = fileProcessor;
        this.loggerUtility = new LoggerUtility(fileProcessor);
    }

    public VBox getLayout(){
        
        VBox processorLayout = new VBox(10);
        processorLayout.setPadding(new Insets(20));

        Text title = new Text("Regex Processor");
        title.setStyle("-fx-font-size: 24px; -fx-fill: white; -fx-font-weight: bold;");
        title.setFont(Font.font("Arial", 28));
        title.setFill(Color.web("#2c3e50"));

        StackPane titleContainer = new StackPane(title);
        titleContainer.setStyle("-fx-background-color: linear-gradient(to right, #4CAF50, #2E7D32); " + "-fx-padding: 20px; ");


        inputArea = createInputArea();
        createActionDropdown();
        createFindReplaceLayout();
        createRegexLayout();
        createFrequencyLayout();
        createSentenceLimitLayout();
        createFilterLayout();
        resultArea = createResultArea();

        Button applyButton = createApplyButton();

        processorLayout.getChildren().addAll(titleContainer, inputArea, actionDropdown,frequencyLayout,filterLayout, sentenceLimitLayout, findReplaceLayout, regexLayout, applyButton, resultArea);
        
        updateUIBasedOnOperation();
        return processorLayout;
    }

    // create ui components
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

    public void uploadFile(){
        FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Text File");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );

            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                try(BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    
                    inputArea.setText(content.toString());
                }catch(IOException ex){
                    loggerUtility.logError("Error reading file: ", ex);
                    inputArea.setText("Error reading file: " + ex.getMessage());
                }
                
            }
    }

    private void updateUIBasedOnOperation() {
        String selectedAction = actionDropdown.getValue();

        // All layout panes
        Pane[] allLayouts = {
            findReplaceLayout,
            regexLayout,
            frequencyLayout,
            sentenceLimitLayout,
            filterLayout
        };

        // Map operation to visible layouts
        Map<String, List<Pane>> layoutVisibilityMap = new HashMap<>();
        layoutVisibilityMap.put("Perform special operation", List.of(regexLayout));
        layoutVisibilityMap.put("Find And Replace All", List.of(findReplaceLayout));
        layoutVisibilityMap.put("Top Frequent Words", List.of(frequencyLayout));
        layoutVisibilityMap.put("Summarize Text", List.of(sentenceLimitLayout));
        layoutVisibilityMap.put("Filter Lines by Keyword", List.of(filterLayout));

        // Hide all layouts by default
        for (Pane layout : allLayouts) {
            setLayoutVisible(layout, false);
        }

        // Show layouts based on selected action
        List<Pane> visibleLayouts = layoutVisibilityMap.getOrDefault(selectedAction, Collections.emptyList());
        for (Pane layout : visibleLayouts) {
            setLayoutVisible(layout, true);
        }
    }

    // Utility method
    private void setLayoutVisible(Pane layout, boolean visible) {
        layout.setManaged(visible);
        layout.setVisible(visible);
    }


    public void findAllMatches(){
        String text = inputArea.getText();
        String regex = useCustomRegex.isSelected() ? customRegexField.getText() : regexTemplates.get(regexDropdown.getValue());
        
        // validate regex
        if (!regexProcessor.isValidRegex(regex)) {
            loggerUtility.logError("Invalid regex", null);
            resultArea.setText("Invalid regex. Please check your input.");
            return;
        }

        if (text.isEmpty() || regex == null || regex.isEmpty()) {
            resultArea.setText("Please enter text and select a valid regex.");
            return;
        }

        try {
            List<String> matches = regexProcessor.findMatches(text, regex);
            loggerUtility.logInfo( matches.size() + " matches found.");
            resultArea.setText(matches.size() > 0 ? String.join("\n", matches) : "No matches found.");
            
        } catch (Exception ex) {
            loggerUtility.logError("Invalid regex or error occurred", ex);
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
            loggerUtility.logInfo("Text modified");
            resultArea.setText(modifiedText);
            
        } catch (Exception ex) {
            loggerUtility.logError("Invalid regex or error occurred", ex);
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
    
    public void readMultipleFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Text Files");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            List<String> paths = selectedFiles.stream()
                                            .map(File::getAbsolutePath)
                                            .collect(Collectors.toList());
            try {
                List<String> fileContents = fileProcessor.batchReadMultipleFiles(paths);
                String combined = String.join("\n--- End of File ---\n", fileContents);
                inputArea.setText(combined); // show combined content in the text area
            } catch (IOException e) {
                inputArea.setText("Error reading files: " + e.getMessage());
            }
        } else {
            inputArea.setText("No files selected.");
        }
    }


}