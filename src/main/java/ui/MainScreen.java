package main.java.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.controller.RegexProcessor;


public class MainScreen{
    private RegexProcessor regexProcessor = new RegexProcessor();

    public void startMainScreen(Stage stage){
        stage.setTitle("AutoLex - Text Processor");
        VBox mainLayout = new VBox(10);
        mainLayout.setAlignment(Pos.CENTER);
        

        // create and use the menu bar
        AppMenuBar appMenuBar = new AppMenuBar();
        MenuBar menuBar = appMenuBar.createMenuBar(stage);
        
        // Combine in BorderPane
        BorderPane rootLayout = new BorderPane();
        rootLayout.setTop(menuBar);
        rootLayout.setCenter(mainLayout);


        TabPane tabPane = new TabPane();

        // Add Regex Tab
        RegexProcessorScreen regexUI = new RegexProcessorScreen(regexProcessor);
        Tab regexTab = new Tab("Regex Engine", regexUI.getLayout());
        

        tabPane.getTabs().addAll(regexTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // Prevent closing
        mainLayout.getChildren().add(tabPane);

        Scene mainScene = new Scene(rootLayout, 1200, 600);
        stage.setScene(mainScene);
    }
}