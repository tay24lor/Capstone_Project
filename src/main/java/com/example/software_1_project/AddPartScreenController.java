package com.example.software_1_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.IOException;

public class AddPartScreenController {
    public RadioButton inHouseButton;
    public RadioButton outsourcedButton;
    public Label makeIDLabel;

    public void onClick2Exit(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 820, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public void onClick2OutSrc() {
        outsourcedButton.setSelected(true);
        inHouseButton.setSelected(false);
        makeIDLabel.setText("Company Name");
    }

    public void onClick2InHouse() {
        inHouseButton.setSelected(true);
        outsourcedButton.setSelected(false);
        makeIDLabel.setText("Machine ID");
    }
}
