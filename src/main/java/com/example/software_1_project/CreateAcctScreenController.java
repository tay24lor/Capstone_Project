package com.example.software_1_project;

import Database.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class CreateAcctScreenController {
    public TextField newAcctUserField;
    public TextField newAcctPassField;

    public void goBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1016, 639);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    public void saveNewAccount() throws SQLException {
        UserDAO.insert(newAcctUserField.getText(), newAcctPassField.getText());
    }
}
