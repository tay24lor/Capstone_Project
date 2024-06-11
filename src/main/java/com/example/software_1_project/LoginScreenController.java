package com.example.software_1_project;

import Database.SQLite;
import Database.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LoginScreenController {
    public Button clickToLogin;
    public TextField userField;
    public PasswordField passField;
    public Button closeButton;
    private static boolean isAdmin = false;

    Alert alert = new Alert(Alert.AlertType.NONE);

    public void login(ActionEvent actionEvent) throws SQLException, IOException {
        String name = userField.getText();
        String pass = passField.getText();

        if (UserDAO.check(name, pass)) {
            isAdmin = Objects.equals(name, "admin");
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1016, 639);
            stage.setTitle("Main Screen");
            stage.setScene(scene);
            stage.show();
        }
        else {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Username or Password not found.");
            alert.showAndWait();
        }
    }

    /**
     * Exit program and close connection to database.
     * @throws SQLException exception to throw
     */
    public void closeProgram() throws SQLException {
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to exit?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            SQLite.close();
            System.exit(0);
        }
    }

    public static boolean adminStatus() { return isAdmin; }
}
