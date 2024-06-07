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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
            if (Objects.equals(name, "admin")) {
                isAdmin = true;
                writeToLog("SUCCESS");
            }
            else {
                isAdmin = false;
            }
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1016, 639);
            stage.setTitle("Main Screen");
            stage.setScene(scene);
            stage.show();
        }
        else {
            writeToLog("FAIL");
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
    public void writeToLog(String status) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/login_activity.txt", true));
            writer.write("[Login Activity] [" + status + "] USERNAME: " + userField.getText() + " TIMESTAMP: " + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            writer.newLine();
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean adminStatus() { return isAdmin; }
}
