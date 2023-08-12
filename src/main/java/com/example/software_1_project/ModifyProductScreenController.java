package com.example.software_1_project;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModifyProductScreenController implements Initializable {

    private static Product prod;
    private final Product product = new Product(0,"",0.00,0,0,0);
    public TextField modProdIDField;
    public TextField modProdNameField;
    public TextField modProdStockField;
    public TextField modProdPriceField;
    public TextField modProdMaxField;
    public TextField modProdMinField;
    public TableView<Part> modProdPartTable = new TableView<>();
    public TableColumn<Part, Integer> modProdPartIDCol;
    public TableColumn<Part, String> modProdPartNameCol;
    public TableColumn<Part, Integer> modProdPartStockCol;
    public TableColumn<Part, Double> modProdPartCostCol;
    public TableView<Part> modAscPartTable = new TableView<>();
    public TableColumn<Part, Integer> modAscPartIDCol;
    public TableColumn<Part, String> modAscPartNameCol;
    public TableColumn<Part, Integer> modAscPartStockCol;
    public TableColumn<Part, Double> modAscPartCostCol;
    public TextField modProdPartSearch;
    public Button modProdPartSearchButton;
    public Alert alert = new Alert(Alert.AlertType.NONE);
    public Button modProdSaveButton;
    public Label noPartToRemoveLabel;
    public Label noPartToAddLabel;

    public static void sendProdData(Product selectedItem) {
        prod = selectedItem;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modProdIDField.setText(String.valueOf(prod.getId()));
        modProdNameField.setText(prod.getName());
        modProdStockField.setText(String.valueOf(prod.getStock()));
        modProdPriceField.setText(String.valueOf(prod.getPrice()));
        modProdMaxField.setText(String.valueOf(prod.getMax()));
        modProdMinField.setText(String.valueOf(prod.getMin()));

        modProdPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProdPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProdPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProdPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        modAscPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modAscPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modAscPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modAscPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        modProdPartTable.setItems(Inventory.getAllParts());
        modAscPartTable.setItems(prod.getAllAssociatedParts());
    }
    public void onClick2Cancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 883, 400);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    public void onClick2SaveModifyProduct(ActionEvent actionEvent) throws IOException {
        if (validateFields()) {
            product.setId(Integer.parseInt(modProdIDField.getText()));
            product.setName(modProdNameField.getText());
            product.setStock(Integer.parseInt(modProdStockField.getText()));
            product.setPrice(Double.parseDouble(modProdPriceField.getText()));
            product.setMax(Integer.parseInt(modProdMaxField.getText()));
            product.setMin(Integer.parseInt(modProdMinField.getText()));

            for (Part part : prod.getAllAssociatedParts()) {
                product.addAssociatePart(part);
            }
            Inventory.updateProduct(Inventory.getAllProducts().indexOf(prod), product);
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 883, 400);
            stage.setTitle("Main Screen");
            stage.setScene(scene);
            stage.show();
        }
        else {
            sendExceptionWarning();
            modProdSaveButton.setOnAction(e -> {
                try {
                    onClick2SaveModifyProduct(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    public void modProdAddAscPart() {
        Part selectedPart = modProdPartTable.getSelectionModel().getSelectedItem();
        if (!(selectedPart == null)) {
            noPartToAddLabel.setText("");
            prod.addAssociatePart(selectedPart);
            Inventory.getAllParts().remove(selectedPart);
            setTables();
        }
        else {
            noPartToAddLabel.setText("No part selected...");
        }
    }

    public void modProdRemovePart() {
        noPartToRemoveLabel.setText("");
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        Part selectedPart = modAscPartTable.getSelectionModel().getSelectedItem();
        if (!(selectedPart == null)) {

            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                Inventory.getAllParts().add(selectedPart);
                prod.deleteAssociatedPart(selectedPart);
                setTables();
            }
            else {
                noPartToRemoveLabel.setText("Part was not removed.");
            }
        }
        else {
            noPartToRemoveLabel.setText("No part selected...");
        }
    }
    public void setTables() {
        modAscPartTable.setItems(prod.getAllAssociatedParts());
        modProdPartTable.setItems(Inventory.getAllParts());
    }

    public void displayModProdPartSearch() {
        ObservableList<Part> partSearchList;
        String search = modProdPartSearch.getText();

        if (!search.isEmpty()) {

            partSearchList = Inventory.lookupPart(search);

            try {
                Part p = Inventory.lookupPart(Integer.parseInt(search));
                if (!(p == null) && !(partSearchList.contains(p))) {
                    partSearchList.add(p);
                }
            } catch (NumberFormatException ignored) {}

            modProdPartTable.setItems(partSearchList);

            if (partSearchList.isEmpty()) {
                sendWarning();
            }
        }
        else {
            modProdPartSearch.clear();
            modProdPartTable.setItems(Inventory.getAllParts());
        }
    }

    public void sendWarning() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        EventHandler<ActionEvent> searchWarning = actionEvent -> {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("****** No Matches Found ******");
            alert.show();
        };
        modProdPartSearchButton.setOnAction(searchWarning);
        modProdPartSearchButton.fire();
        modProdPartSearch.clear();
        modAscPartTable.setItems(Inventory.getAllParts());
    }
    private boolean validateFields() {
        try {
            Double.parseDouble(modProdPriceField.getText());
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Price must be a decimal number ******");
            modProdPriceField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        try {
            Integer.parseInt(modProdStockField.getText());
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Inventory must be a whole number ******");
            modProdStockField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        try {
            Integer.parseInt(modProdMaxField.getText());
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Maximum field must be a whole number ******");
            modProdMaxField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        try {
            Integer.parseInt(modProdMinField.getText());
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Minimum field must be a whole number ******");
            modProdMinField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        if (Integer.parseInt(modProdMaxField.getText()) < Integer.parseInt(modProdMinField.getText())) {
            modProdMaxField.setBorder(Border.stroke(Paint.valueOf("red")));
            alert.setContentText("****** Maximum is lower than minumum ******");
            return false;
        }
        if (Integer.parseInt(modProdStockField.getText()) > Integer.parseInt(modProdMaxField.getText()) ||
                Integer.parseInt(modProdStockField.getText()) < Integer.parseInt(modProdMinField.getText())) {
            modProdStockField.setBorder(Border.stroke(Paint.valueOf("red")));
            alert.setContentText("****** Inventory is outside the max/min range ******");
            return false;
        }
        return true;
    }
    public void sendExceptionWarning() {
        EventHandler<ActionEvent> fieldWarning = actionEvent -> {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.show();
        };
        modProdSaveButton.setOnAction(fieldWarning);
        modProdSaveButton.fire();
        alert.setOnCloseRequest(e -> clearFields());

    }
    private void clearFields() {
        modProdNameField.setBorder(Border.EMPTY);
        modProdStockField.setBorder(Border.EMPTY);
        modProdPriceField.setBorder(Border.EMPTY);
        modProdMaxField.setBorder(Border.EMPTY);
        modProdMinField.setBorder(Border.EMPTY);
    }
}
