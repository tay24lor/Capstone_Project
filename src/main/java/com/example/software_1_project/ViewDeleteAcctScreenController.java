package com.example.software_1_project;

import Database.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ViewDeleteAcctScreenController implements Initializable {
    public TableView<User> viewAcctsTable;
    public TableColumn<User, String> userCol;
    public TableColumn<User, String> passCol;
    public Button deleteAcctButton;
    public Label confirmationLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        passCol.setCellValueFactory(new PropertyValueFactory<>("pass"));

        try {
            viewAcctsTable.setItems(UserDAO.getUsers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAcct() throws SQLException {
        User selectedUser = viewAcctsTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete the selected user?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            UserDAO.delete(selectedUser);
            viewAcctsTable.getItems().remove(selectedUser);
            confirmationLabel.setText("Account successfully deleted.");
        }
        else {
            confirmationLabel.setText("Account was not deleted.");
        }
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1016, 639);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }
}
