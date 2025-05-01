package main.java.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class AppMenuBar {   
    private RegexProcessorScreen screen;

    public AppMenuBar(RegexProcessorScreen screen) {
        this.screen = screen;
    }

    public MenuBar createMenuBar(Stage appStage) {
        MenuBar menuBar = new MenuBar();

        // Create Menus
        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("Help");

        // create menu items for file menu
        MenuItem openItem = new MenuItem("Open");
        MenuItem openMultipleItems = new MenuItem("Open Multiple");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e-> appStage.close());
        openItem.setOnAction(e-> screen.uploadFile());
        openMultipleItems.setOnAction(e-> screen.readMultipleFiles());
        saveItem.setOnAction(e-> screen.saveFile());

        // add menu items to file menu
        fileMenu.getItems().addAll(openItem,openMultipleItems, saveItem, exitItem);


        // create menu items for edit menu
        MenuItem aboutItem = new MenuItem("About");
        helpMenu.getItems().addAll(aboutItem);
        aboutItem.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About AutoLex");
            alert.setHeaderText("AutoLex - Text Processor");
            alert.setContentText("Version 1.0\nDeveloped by MikeAdams Solutions.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        });

        // Add Menus to MenuBar
        menuBar.getMenus().addAll(fileMenu, helpMenu);

        return menuBar;
    }
}