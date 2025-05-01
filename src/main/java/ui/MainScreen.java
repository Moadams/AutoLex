package main.java.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.controller.FileProcessor;
import main.java.controller.RegexProcessor;
import main.java.controller.TextProcessor;


public class MainScreen{
    private RegexProcessor regexProcessor = new RegexProcessor();
    private TextProcessor textProcessor = new TextProcessor();
    private FileProcessor fileProcessor = new FileProcessor();

    public void startMainScreen(Stage stage){
        stage.setTitle("AutoLex - Text Processor");
        VBox mainLayout = new VBox(10);
        mainLayout.setAlignment(Pos.CENTER);
        
        RegexProcessorScreen regexUI = new RegexProcessorScreen(regexProcessor, textProcessor, fileProcessor);

        // create and use the menu bar
        AppMenuBar appMenuBar = new AppMenuBar(regexUI);
        MenuBar menuBar = appMenuBar.createMenuBar(stage);
        
        // Combine in BorderPane
        BorderPane rootLayout = new BorderPane();
        rootLayout.setTop(menuBar);
        rootLayout.setCenter(mainLayout);


        
        // Add Regex Tab
        TabPane tabPane = new TabPane();
        Tab regexTab = new Tab("Regex Engine", regexUI.getLayout());
        

        tabPane.getTabs().addAll(regexTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // Prevent closing
        mainLayout.getChildren().add(tabPane);

        Scene mainScene = new Scene(rootLayout, 1200, 600);
        stage.setScene(mainScene);
    }
}