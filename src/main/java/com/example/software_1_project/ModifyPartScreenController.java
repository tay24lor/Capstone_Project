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
import model.Inventory;
import model.OutSourcedPart;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModifyPartScreenController implements Initializable {
    public RadioButton inHouseButton;
    public RadioButton outsourcedButton;
    public Label makeIDLabel;
    public static InHousePart iHmodPart;
    public static OutSourcedPart oSmodPart;
    private static Part modPart;
    public TextField partID;
    public TextField nameField;
    public TextField invField;
    public TextField priceField;
    public TextField minField;
    public TextField maxField;
    public TextField machineOrCompanyField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (modPart.getClass().equals(InHousePart.class))
            inHouseButton.fire();
        else if (modPart.getClass().equals(OutSourcedPart.class))
            outsourcedButton.fire();
        if (inHouseButton.isSelected()) {
            iHmodPart = (InHousePart) modPart;
            machineOrCompanyField.setText(String.valueOf(iHmodPart.getMachineCode()));
        }
        else if (outsourcedButton.isSelected()) {
            oSmodPart = (OutSourcedPart) modPart;
            machineOrCompanyField.setText(oSmodPart.getCompanyName());
        }
        partID.setText(String.valueOf(modPart.getId()));
        nameField.setText(modPart.getName());
        invField.setText(String.valueOf(modPart.getStock()));
        priceField.setText(String.valueOf(modPart.getPrice()));
        minField.setText(String.valueOf(modPart.getMin()));
        maxField.setText(String.valueOf(modPart.getMax()));
    }

    public static void sendPartData(Part selectedItem) {
        modPart = selectedItem;
    }

    public void onClick2Exit(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 883, 400);
        stage.setTitle("Main Screen");
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
        if (inHouseButton.isSelected()) {
            iHmodPart = new InHousePart(0, "", 0, 0, 0, 0);
            Inventory.updatePart(Inventory.getAllParts().indexOf(modPart), iHmodPart);
            iHmodPart.setId(Integer.parseInt(partID.getText()));
            iHmodPart.setName(nameField.getText()); iHmodPart.setStock(Integer.parseInt(invField.getText()));
            iHmodPart.setPrice(Double.parseDouble(priceField.getText())); iHmodPart.setMin(Integer.parseInt(minField.getText()));
            iHmodPart.setMax(Integer.parseInt(maxField.getText()));
            iHmodPart.setMachineCode(Integer.parseInt(machineOrCompanyField.getText()));

        } else if (outsourcedButton.isSelected()) {
            oSmodPart = new OutSourcedPart(modPart.getId(), "", 0, 0, 0, 0);
            Inventory.updatePart(Inventory.getAllParts().indexOf(modPart), oSmodPart);
            oSmodPart.setId(Integer.parseInt(partID.getText()));
            oSmodPart.setName(nameField.getText()); oSmodPart.setStock(Integer.parseInt(invField.getText()));
            oSmodPart.setPrice(Double.parseDouble(priceField.getText())); oSmodPart.setMin(Integer.parseInt(minField.getText()));
            oSmodPart.setMax(Integer.parseInt(maxField.getText()));
            oSmodPart.setCompanyName(machineOrCompanyField.getText());
        }
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 883, 400);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    

}
