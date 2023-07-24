package com.example.software_1_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.InHousePart;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModifyPartScreenController implements Initializable {
    public RadioButton inHouseButton;
    public RadioButton outsourcedButton;
    public Label makeIDLabel;
    public static InHousePart modPart;
    public TextField partID;
    public TextField nameField;
    public TextField invField;
    public TextField priceField;
    public TextField minField;
    public TextField maxField;
    public TextField machineIDField;
    private String id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partID.setText(String.valueOf(modPart.getId()));
        nameField.setText(modPart.getName());
        invField.setText(String.valueOf(modPart.getStock()));
        priceField.setText(String.valueOf(modPart.getPrice()));
        minField.setText(String.valueOf(modPart.getMin()));
        maxField.setText(String.valueOf(modPart.getMax()));
        machineIDField.setText(String.valueOf(modPart.getMachineCode()));
    }

    public static void sendData(InHousePart selectedItem) {
        modPart = selectedItem;
    }

    public void onClick2Exit(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
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

    public void onClick2Mod(ActionEvent actionEvent) throws IOException {
        modPart.setName(nameField.getText()); modPart.setPrice(Double.parseDouble(priceField.getText()));
        modPart.setStock(Integer.parseInt(invField.getText())); modPart.setMin(Integer.parseInt(minField.getText()));
        modPart.setMax(Integer.parseInt(maxField.getText())); modPart.setMachineCode(machineIDField.getText());
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 820, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


}
