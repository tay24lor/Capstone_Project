package com.example.software_1_project;

import Database.PartDAO;
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
import model.OutSourcedPart;
import model.Part;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    protected int x = 1016;
    protected int y = 639;

    public void onClick2OutSrc() {
        makeIDLabel.setText("Company Name");
    }
    public void onClick2InHouse() {
        makeIDLabel.setText("Machine ID");
    }
    public void onClick2Exit(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, x, y);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }
    public void onClick2Save(ActionEvent actionEvent) throws IOException, SQLException {

        if (validateFields()) {
            Part part;
            if (inHouseButton.isSelected()) {
                part = setIHStats();
                PartDAO.insert(part, Integer.parseInt(machineID_CompanyField.getText()), "");

            }
            else if (outsourcedButton.isSelected()) {
                part = setOSStats();
                PartDAO.insert(part, 0, machineID_CompanyField.getText());

            }
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, x, y);
            stage.setTitle("Main Screen");
            stage.setScene(scene);
            stage.show();
        }
        else {
            sendWarning();
            addPartSaveButton.setOnAction(e -> {
                try {
                    onClick2Save(e);
                } catch (IOException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }
    private InHousePart setIHStats() throws SQLException {
        InHousePart part = new InHousePart(0, "", 0.00, 0, 0, 0, -1);
        part.setId(PartDAO.getLatestId());
        part.setName(nameField.getText()); part.setPrice(Double.parseDouble(priceField.getText()));
        part.setStock(Integer.parseInt(stockField.getText())); part.setMin(Integer.parseInt(minField.getText()));
        part.setMax(Integer.parseInt(maxField.getText())); part.setMachineCode(Integer.parseInt(machineID_CompanyField.getText()));
        part.setDate(ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return part;

    }
    private OutSourcedPart setOSStats() throws SQLException {
        OutSourcedPart part = new OutSourcedPart(0, "", 0.00, 0, 0, 0, -1);
        part.setId(PartDAO.getLatestId());
        part.setName(nameField.getText()); part.setPrice(Double.parseDouble(priceField.getText()));
        part.setStock(Integer.parseInt(stockField.getText())); part.setMin(Integer.parseInt(minField.getText()));
        part.setMax(Integer.parseInt(maxField.getText())); part.setCompanyName(machineID_CompanyField.getText());
        part.setDate(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        return part;
    }

    public boolean validateFields() {
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
