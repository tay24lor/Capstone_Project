package com.example.software_1_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.InHousePart;
import model.Inventory;
import model.OutSourcedPart;

import java.io.IOException;
import java.util.Objects;

public class AddPartScreenController {
    public RadioButton inHouseButton;
    public RadioButton outsourcedButton;
    public Label makeIDLabel;
    public TextField IDField;
    public TextField nameField;
    public TextField stockField;
    public TextField priceField;
    public TextField minField;
    public TextField maxField;
    public TextField machineIDField;
    public ToggleGroup InOrOut;

    public AddPartScreenController() {

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
        makeIDLabel.setText("Company Name");
    }
    public void onClick2InHouse() {
        makeIDLabel.setText("Machine ID");
    }
    public void onClick2Save(ActionEvent actionEvent) throws IOException {
        if (inHouseButton.isSelected()) setIHStats();
        else if (outsourcedButton.isSelected()) setOSStats();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 820, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private void setIHStats() {
        InHousePart part = new InHousePart(0, "", 0.00, 0, 0, 0);
        part.setId(1); part.setName(nameField.getText()); part.setPrice(Double.parseDouble(priceField.getText()));
        part.setStock(Integer.parseInt(stockField.getText())); part.setMin(Integer.parseInt(minField.getText()));
        part.setMax(Integer.parseInt(maxField.getText())); part.setMachineCode(Integer.parseInt(machineIDField.getText()));
        Inventory.addPart(part);
    }
    private void setOSStats() {
        OutSourcedPart part = new OutSourcedPart(0, "", 0.00, 0, 0, 0);
        part.setId(1); part.setName(nameField.getText()); part.setPrice(Double.parseDouble(priceField.getText()));
        part.setStock(Integer.parseInt(stockField.getText())); part.setMin(Integer.parseInt(minField.getText()));
        part.setMax(Integer.parseInt(maxField.getText())); part.setCompanyName(machineIDField.getText());
        Inventory.addPart(part);
    }
}
