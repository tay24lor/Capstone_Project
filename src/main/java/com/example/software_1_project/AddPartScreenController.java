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
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.InHousePart;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddPartScreenController implements Initializable {
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

    private final InHousePart part;

    public AddPartScreenController() {
        part = new InHousePart(0, "",0.00,0,0,0, "");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
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
        part.setId(1); part.setName(nameField.getText()); part.setPrice(Double.parseDouble(priceField.getText()));
        part.setStock(Integer.parseInt(stockField.getText())); part.setMin(Integer.parseInt(minField.getText()));
        part.setMax(Integer.parseInt(maxField.getText())); part.setMachineCode(machineIDField.getText());
        MainScreenController.passData(part);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 820, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
