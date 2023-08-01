package com.example.software_1_project;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    public TableView<Part> partTable = new TableView<>();
    public TextField partSearch;
    public TableColumn<Part, Integer> partIDCol;
    public TableColumn<Part, String> partNameCol;
    public TableColumn<Part, Integer> invLevelCol;
    public TableColumn<Part, Double> priceCol;
    public TableView<Product> prodTable = new TableView<>();
    public TableColumn<Product, Integer> prodIDCol;
    public TableColumn<Product, String> prodNameCol;
    public TableColumn<Product, Integer> prodInvCol;
    public TableColumn<Product, Double> prodCostCol;
    public Label warningLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Inventory.getAllParts().isEmpty()) {
            InHousePart part1 = new InHousePart(1, "IH_Part1", 1.00, 1, 1, 20);
            InHousePart part2 = new InHousePart(2, "IH_Part2", 1.00, 1, 1, 20);
            OutSourcedPart part3 = new OutSourcedPart(3, "OSPart1", 1.00, 1, 1, 20);
            OutSourcedPart part4 = new OutSourcedPart(4, "OSPart2", 1.00, 1, 1, 20);

            part1.setMachineCode(101);
            part2.setMachineCode(102);

            part3.setCompanyName("ABC");
            part4.setCompanyName("DEF");

            Inventory.addPart(part1);
            Inventory.addPart(part2);
            Inventory.addPart(part3);
            Inventory.addPart(part4);
        }

        generatePartTable();
        if (!(Inventory.getAllProducts() == null)) {
            generateProductTable();
        }
    }

    private void generatePartTable() {
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        invLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partTable.setItems(Inventory.getAllParts());
    }
    private void generateProductTable() {
        prodIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        prodTable.setItems(Inventory.getAllProducts());
    }
    @FXML
    protected void onExitButtonClick() {
        System.exit(0);
    }
    public void onClick2AddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddPartScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 650, 600);
        stage.setScene(scene);
        stage.show();
    }
    public void onClick2AddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddProductScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 530);
        stage.setScene(scene);
        stage.show();
    }
    public void onClick2ModPart(ActionEvent actionEvent) throws IOException {
        Part selectedItem = partTable.getSelectionModel().getSelectedItem();
        if (checkPartSelected(selectedItem)) {   // Check if no item is selected before sending data to modify screen
            ModifyPartScreenController.sendPartData(selectedItem);
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ModifyPartScreen.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 650, 600);
            stage.setScene(scene);
            stage.show();
        }
        else {
            warningLabel.setText("Please select an item to modify.");
        }
    }
    public void onClick2ModifyProduct(ActionEvent actionEvent) throws IOException {
        Product selectedItem = prodTable.getSelectionModel().getSelectedItem();
        if (checkProdSelected(selectedItem)) {   // Check if no item is selected before sending data to modify screen
            ModifyProductScreenController.sendProdData(selectedItem);
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ModifyProductScreen.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 900, 530);
            stage.setScene(scene);
            stage.show();
        }
        else {
            warningLabel.setText("Please select an item to modify.");
        }
    }
    public boolean checkPartSelected(Part part) {
        return part != null;
    }
    public boolean checkProdSelected(Product prod) { return prod != null; }
    public void displayPartSearch() {
        ObservableList<Part> partSearchList = FXCollections.observableArrayList();

        if (!partSearch.getText().isEmpty()) {
            for (int i = 0; i < Inventory.getAllParts().size(); i++) {
                if (Integer.parseInt(partSearch.getText()) == Objects.requireNonNull(Inventory.lookupPart(i + 1)).getId()) {
                    partSearchList.add(Inventory.lookupPart(i+1));
                }
            }
            partTable.setItems(partSearchList);
        }
        else {
            partSearch.clear();
            partTable.setItems(Inventory.getAllParts());
        }

/*  FilteredList<Part> filteredList = new FilteredList<>(Inventory.getAllParts(), p -> true);

        partSearch.textProperty().addListener((observableValue, oldValue, newValue) -> filteredList.setPredicate(part -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            if (part.getName().toLowerCase().contains(newValue)) {
                return true;
            }
            else return Inventory.lookupPart(part.getId());//String.valueOf(part.getId()).equals(newValue);
        }));

        SortedList<Part> sortedData = new SortedList<>(filteredList);
        partTable.setItems(sortedData);*/
    }
    public void onClick2DeletePart() {
        Part selectedItem = partTable.getSelectionModel().getSelectedItem();
        if (checkPartSelected(selectedItem)) {
            partTable.getItems().remove(selectedItem);
            Inventory.deletePart(selectedItem);
            warningLabel.setText("");
        }
        else {
            warningLabel.setText("Please select an item to delete.");
        }
    }
    public void onClick2DeleteProduct() {
        Product selectedItem = prodTable.getSelectionModel().getSelectedItem();
        if (checkProdSelected(selectedItem)) {
            for (Part p : selectedItem.getAllAssociatedParts()) {
                Inventory.addPart(p);
            }

            selectedItem.getAllAssociatedParts().clear();
            prodTable.getItems().remove(selectedItem);
            Inventory.deleteProduct(selectedItem);
            warningLabel.setText("");
        }
        else {
            warningLabel.setText("Please select an item to delete.");
        }
    }
}