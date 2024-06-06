package com.example.software_1_project;

import Database.SQLite;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class ApplicationStart extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setOnCloseRequest(evt -> {
            evt.consume();

            shutdown(primaryStage);
        });
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("loginScreen.fxml")));
        primaryStage.setTitle("");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {
        SQLite.connect();
        launch();
        SQLite.close();
    }

    private void shutdown(Stage mainWindow) {
        Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            mainWindow.close();
        }
    }
}