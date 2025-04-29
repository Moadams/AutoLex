package main.java.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class MainScreen{
    public void startMainScreen(Stage stage){
        stage.setTitle("AutoLex - Text Processor");
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0.3, 0, 5);"
        );

        // create and use the menu bar
        AppMenuBar appMenuBar = new AppMenuBar();
        MenuBar menuBar = appMenuBar.createMenuBar(stage);
        

        // Combine in BorderPane
        BorderPane rootLayout = new BorderPane();
        rootLayout.setTop(menuBar);
        rootLayout.setCenter(mainLayout);

        Scene mainScene = new Scene(rootLayout, 1200, 600);
        stage.setScene(mainScene);
    }
}