package com.example.software_1_project;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
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
    public TextField machineID_CompanyField;
    public ToggleGroup InOrOut;
    public Button addPartSaveButton;
    public Alert alert = new Alert(Alert.AlertType.NONE);

    public void onClick2OutSrc() {
        makeIDLabel.setText("Company Name");
    }
    public void onClick2InHouse() {
        makeIDLabel.setText("Machine ID");
    }
    public void onClick2Exit(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 883, 400);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }
    public void onClick2Save(ActionEvent actionEvent) throws IOException {
        Part part = null;
        if (inHouseButton.isSelected()) part = setIHStats();
        else if (outsourcedButton.isSelected()) part = setOSStats();

        assert part != null;
        if (validateFields(part)) {
            Inventory.addPart(part);
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 883, 400);
            stage.setTitle("Main Screen");
            stage.setScene(scene);
            stage.show();
        }
        else {
            sendWarning();
            addPartSaveButton.setOnAction(e -> {
                try {
                    onClick2Save(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    private InHousePart setIHStats() {
        InHousePart part = new InHousePart(0, "", 0.00, 0, 0, 0);
        part.setId(0);
        part.setName(nameField.getText()); part.setPrice(Double.parseDouble(priceField.getText()));
        part.setStock(Integer.parseInt(stockField.getText())); part.setMin(Integer.parseInt(minField.getText()));
        part.setMax(Integer.parseInt(maxField.getText())); part.setMachineCode(Integer.parseInt(machineID_CompanyField.getText()));
        generateID(part);
        return part;
    }
    private OutSourcedPart setOSStats() {
        OutSourcedPart part = new OutSourcedPart(0, "", 0.00, 0, 0, 0);
        part.setId(0);
        part.setName(nameField.getText()); part.setPrice(Double.parseDouble(priceField.getText()));
        part.setStock(Integer.parseInt(stockField.getText())); part.setMin(Integer.parseInt(minField.getText()));
        part.setMax(Integer.parseInt(maxField.getText())); part.setCompanyName(machineID_CompanyField.getText());
        generateID(part);
        return part;
    }

    private void generateID(Part part) {
        int id = 0;
        for (Part p : Inventory.getAllParts()) {
            id = p.getId();
        }
        id++;
        part.setId(id);
    }

    private boolean validateFields(Part part) {
        if (part.getMax() < part.getMin()) {
            maxField.setBorder(Border.stroke(Paint.valueOf("red")));
            alert.setContentText("****** Maximum is lower than minumum ******");
            return false;
        }
        if (part.getStock() > part.getMax() ||
            part.getStock() < part.getMin()) {
            stockField.setBorder(Border.stroke(Paint.valueOf("red")));
            alert.setContentText("****** Inventory is outside the max/min range ******");
            return false;
        }
        return true;
    }
    public void sendWarning() {
        EventHandler<ActionEvent> fieldWarning = actionEvent -> {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setX(IDField.getLayoutX());
            alert.setY(65);
            alert.show();
        };
        addPartSaveButton.setOnAction(fieldWarning);
        addPartSaveButton.fire();
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
}
