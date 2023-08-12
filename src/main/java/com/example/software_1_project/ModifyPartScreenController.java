package com.example.software_1_project;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
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
    public TextField stockField;
    public TextField priceField;
    public TextField minField;
    public TextField maxField;
    public TextField machineID_CompanyField;
    public Alert alert = new Alert(Alert.AlertType.NONE);
    public Button modPartSaveButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (modPart.getClass().equals(InHousePart.class))
            inHouseButton.fire();
        else if (modPart.getClass().equals(OutSourcedPart.class))
            outsourcedButton.fire();
        if (inHouseButton.isSelected()) {
            iHmodPart = (InHousePart) modPart;
            machineID_CompanyField.setText(String.valueOf(iHmodPart.getMachineCode()));
        }
        else if (outsourcedButton.isSelected()) {
            oSmodPart = (OutSourcedPart) modPart;
            machineID_CompanyField.setText(oSmodPart.getCompanyName());
        }
        partID.setText(String.valueOf(modPart.getId()));
        nameField.setText(modPart.getName());
        stockField.setText(String.valueOf(modPart.getStock()));
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
        Part part = null;

        if (validateFields()) {
            if (inHouseButton.isSelected()) part = setIHStats();
            else if (outsourcedButton.isSelected()) part = setOSStats();
            Inventory.updatePart(Inventory.getAllParts().indexOf(modPart), part);
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 883, 400);
            stage.setTitle("Main Screen");
            stage.setScene(scene);
            stage.show();
        }
        else {
            sendWarning();
            modPartSaveButton.setOnAction(e -> {
                try {
                    onClick2Mod(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }
    private boolean validateFields() {
        try {
            Double.parseDouble(priceField.getText());
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Price must be a decimal number ******");
            priceField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        try {
            Integer.parseInt(stockField.getText());
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Inventory must be a whole number ******");
            stockField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        try {
            Integer.parseInt(maxField.getText());
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Maximum field must be a whole number ******");
            maxField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        try {
            Integer.parseInt(minField.getText());
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Minimum field must be a whole number ******");
            minField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        if (Integer.parseInt(maxField.getText()) < Integer.parseInt(minField.getText())) {
            maxField.setBorder(Border.stroke(Paint.valueOf("red")));
            alert.setContentText("****** Maximum is lower than minumum ******");
            return false;
        }
        if (inHouseButton.isSelected()) {
            try {
                Integer.parseInt(machineID_CompanyField.getText());
            } catch (NumberFormatException ex) {
                alert.setContentText("****** Machine ID must be a whole number ******");
                machineID_CompanyField.setBorder(Border.stroke(Paint.valueOf("red")));
                return false;
            }
        }
        if (Integer.parseInt(stockField.getText()) > Integer.parseInt(maxField.getText()) ||
                Integer.parseInt(stockField.getText()) < Integer.parseInt(minField.getText())) {
            stockField.setBorder(Border.stroke(Paint.valueOf("red")));
            alert.setContentText("****** Inventory is outside the max/min range ******");
            return false;
        }
        return true;
    }
    public void sendWarning() {
        EventHandler<ActionEvent> fieldWarning = actionEvent -> {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.show();
        };
        modPartSaveButton.setOnAction(fieldWarning);
        modPartSaveButton.fire();
        alert.setOnCloseRequest(e -> clearFields());
    }
    private void clearFields() {
        nameField.setBorder(Border.EMPTY);
        stockField.setBorder(Border.EMPTY);
        priceField.setBorder(Border.EMPTY);
        maxField.setBorder(Border.EMPTY);
        minField.setBorder(Border.EMPTY);
        machineID_CompanyField.setBorder(Border.EMPTY);
    }

    private InHousePart setIHStats() {
        InHousePart part = new InHousePart(0, "", 0.00, 0, 0, 0);
        part.setId(modPart.getId());
        part.setName(nameField.getText()); part.setPrice(Double.parseDouble(priceField.getText()));
        part.setStock(Integer.parseInt(stockField.getText())); part.setMin(Integer.parseInt(minField.getText()));
        part.setMax(Integer.parseInt(maxField.getText())); part.setMachineCode(Integer.parseInt(machineID_CompanyField.getText()));
        return part;
    }
    private OutSourcedPart setOSStats() {
        OutSourcedPart part = new OutSourcedPart(0, "", 0.00, 0, 0, 0);
        part.setId(modPart.getId());
        part.setName(nameField.getText()); part.setPrice(Double.parseDouble(priceField.getText()));
        part.setStock(Integer.parseInt(stockField.getText())); part.setMin(Integer.parseInt(minField.getText()));
        part.setMax(Integer.parseInt(maxField.getText())); part.setCompanyName(machineID_CompanyField.getText());
        return part;
    }
}
