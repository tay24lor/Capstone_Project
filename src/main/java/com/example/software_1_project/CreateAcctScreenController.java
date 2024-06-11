package com.example.software_1_project;

import Database.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class CreateAcctScreenController {
    public TextField newAcctUserField;
    public TextField newAcctPassField;
    public Alert alert;

    public void goBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1016, 639);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    public void saveNewAccount(ActionEvent actionEvent) throws SQLException, IOException {
        alert = new Alert(Alert.AlertType.WARNING);
        if (newAcctUserField.getText().isEmpty()) {
            alert.setContentText("Please enter a user name.");
            alert.showAndWait();
        }
        else if (newAcctPassField.getText().isEmpty()) {
            alert.setContentText("Please enter a password.");
            alert.showAndWait();
        }
        else {
            if (validate(newAcctUserField.getText(), newAcctPassField.getText())) {
                UserDAO.insert(newAcctUserField.getText(), newAcctPassField.getText());
                goBack(actionEvent);
            }
        }
    }

    private boolean validate(String name, String pass) throws SQLException {
        alert.setAlertType(Alert.AlertType.WARNING);
        for (User user : UserDAO.getUsers()) {
            if (Objects.equals(name, user.getName())) {
                alert.setContentText("User name already exists.");
                alert.showAndWait();
                return false;
            }
            else if (Objects.equals(pass, user.getPass())) {
                alert.setContentText("Password is not unique.");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }
}
