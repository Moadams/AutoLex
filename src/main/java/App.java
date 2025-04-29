package main.java;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.ui.MainScreen;
import main.java.ui.WelcomeScreen;

public class App extends Application{
    Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        this.stage = stage;
        WelcomeScreen welcomeScreen = new WelcomeScreen();
        welcomeScreen.startHomeScreen(stage, this);
    }

    public void startMainScreen(){
        MainScreen mainScreen = new MainScreen();
        mainScreen.startMainScreen(stage);
    }
}