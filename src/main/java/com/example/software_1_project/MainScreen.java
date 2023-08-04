package com.example.software_1_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainScreen extends Application {
    private static boolean firstTime = true;
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        primaryStage.setTitle("");
        primaryStage.setScene(new Scene(root, 883, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static boolean checkFirstTime() {
        return firstTime;
    }
    public static void setFirstTime() {
        firstTime = false;
    }
}