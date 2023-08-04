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
    public TextField modProdIDField;
    public TextField modProdNameField;
    public TextField modProdInvField;
    public TextField modProdPriceField;
    public TextField modProdMaxField;
    public TextField modProdMinField;
    public TableView<Part> modProdPartTable = new TableView<>();
    public TableColumn<Part, Integer> modProdPartIDCol;
    public TableColumn<Part, String> modProdPartNameCol;
    public TableColumn<Part, Integer> modProdPartInvCol;
    public TableColumn<Part, Double> modProdPartCostCol;
    public TableView<Part> modAscPartTable = new TableView<>();
    public TableColumn<Part, Integer> modAscPartIDCol;
    public TableColumn<Part, String> modAscPartNameCol;
    public TableColumn<Part, Integer> modAscPartInvCol;
    public TableColumn<Part, Double> modAscPartCostCol;
    public TextField modProdPartSearch;
    public Button modProdPartSearchButton;

    public static void sendProdData(Product selectedItem) {
        prod = selectedItem;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modProdIDField.setText(String.valueOf(prod.getId()));
        modProdNameField.setText(prod.getName());
        modProdInvField.setText(String.valueOf(prod.getStock()));
        modProdPriceField.setText(String.valueOf(prod.getPrice()));
        modProdMaxField.setText(String.valueOf(prod.getMax()));
        modProdMinField.setText(String.valueOf(prod.getMin()));

        modProdPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProdPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProdPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProdPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        modAscPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modAscPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modAscPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
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
        prod.setName(modProdNameField.getText());
        prod.setStock(Integer.parseInt(modProdInvField.getText()));
        prod.setPrice(Double.parseDouble(modProdPriceField.getText()));
        prod.setMax(Integer.parseInt(modProdMaxField.getText()));
        prod.setMin(Integer.parseInt(modProdMinField.getText()));
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 883, 400);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    public void modProdAddAscPart() {
        Part selectedPart = modProdPartTable.getSelectionModel().getSelectedItem();
        if (!(selectedPart == null)) {
            prod.addAssociatePart(selectedPart);
            Inventory.getAllParts().remove(selectedPart);
            setTables();
        }
    }

    public void modProdRemovePart() {
        Part selectedPart = modAscPartTable.getSelectionModel().getSelectedItem();
        if (!(selectedPart == null)) {
            Inventory.getAllParts().add(selectedPart);
            prod.deleteAssociatedPart(selectedPart);
            setTables();
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
}
