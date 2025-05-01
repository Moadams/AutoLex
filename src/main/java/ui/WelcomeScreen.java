package main.java.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.App;

public class WelcomeScreen {

    public void startHomeScreen(Stage homeStage, App mainApp) {
        homeStage.setTitle("AutoLex - Text Processor");

        // Background Pane
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #d7ebff, #b0d0f0);");

        // Card-like VBox
        VBox mainLayout = new VBox(15);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0.3, 0, 5);"
        );

        // Logo Title
        Text logo = new Text("AutoLex");
        logo.setFont(Font.font("Verdana", 78));
        logo.setFill(Color.web("#2c3e50"));
        logo.setStyle("-fx-font-weight: semi-bold;");


        // Subintro
        Text subIntro = new Text("Automate and streamline your text processing workflow.");
        subIntro.setFont(Font.font("Arial", 16));
        subIntro.setFill(Color.web("#555"));

        // CTA Button
        Button startButton = new Button("Get Started");
        startButton.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 20;" +
            "-fx-background-color: #3498db;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 2;"
        );
        startButton.setOnAction(e -> {
            mainApp.startMainScreen();
        });

        mainLayout.getChildren().addAll(logo, subIntro, startButton);
        root.getChildren().add(mainLayout);

        Scene homeScene = new Scene(root, 1200, 600);
        homeStage.setScene(homeScene);
        homeStage.show();
    }
}
