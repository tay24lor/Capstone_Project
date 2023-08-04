package com.example.software_1_project;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.TilePane;
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
    public Button partSearchButton;
    public TilePane warningPane = new TilePane();
    public TextField productSearch;
    public Button prodSearchButton;

    /**
     * Initialize preloads the tables with data if first time launching the program.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Inventory.getAllParts().isEmpty() && Inventory.getAllProducts().isEmpty() && MainScreen.checkFirstTime()) {
            InHousePart part1 = new InHousePart(1, "Pedals", 1.00, 1, 1, 20);
            InHousePart part2 = new InHousePart(2, "Chains", 1.00, 1, 1, 20);
            OutSourcedPart part3 = new OutSourcedPart(3, "Seats", 1.00, 1, 1, 20);
            OutSourcedPart part4 = new OutSourcedPart(4, "Handlebars", 1.00, 1, 1, 20);

            part1.setMachineCode(101);
            part2.setMachineCode(102);

            part3.setCompanyName("Bike Co.");
            part4.setCompanyName("Chains & Things");

            Inventory.addPart(part1);
            Inventory.addPart(part2);
            Inventory.addPart(part3);
            Inventory.addPart(part4);

            Product prod1 = new Product(1, "Adult Bike", 200.00, 20, 1, 35);
            Product prod2 = new Product(2, "Kid Bike", 100.00, 10, 1, 20);

            Inventory.addProduct(prod1);
            Inventory.addProduct(prod2);
        }

        MainScreen.setFirstTime();
        generatePartTable();
        generateProductTable();
    }

    /**
     * Create part table columns and populate with data.
     */
    private void generatePartTable() {
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        invLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partTable.setItems(Inventory.getAllParts());
    }

    /**
     * Create product table columns and populate with data.
     */
    private void generateProductTable() {
        prodIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        prodTable.setItems(Inventory.getAllProducts());
    }

    /**
     * Method for opening the Add Part pane.
     */
    public void onClick2AddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddPartScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 650, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method for opening the Add Product pane.
     */
    public void onClick2AddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddProductScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 530);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method for opening the Modify Part pane.
     */
    public void onClick2ModPart(ActionEvent actionEvent) throws IOException {
        Part selectedItem = partTable.getSelectionModel().getSelectedItem();
        if (checkObjectSelected(selectedItem)) {   // Check if no item is selected before sending data to modify screen
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

    /**
     * Method for opening the Modify Product pane.
     */
    public void onClick2ModifyProduct(ActionEvent actionEvent) throws IOException {
        Product selectedItem = prodTable.getSelectionModel().getSelectedItem();
        if (checkObjectSelected(selectedItem)) {   // Check if no item is selected before sending data to modify screen
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

    /**
     * Method for confirming a Part or Product is selected when modifying or deleting.
     */
    public boolean checkObjectSelected(Object object) {
        return object != null;
    }

    /**
     * Method used to search for parts.
     */
    public void displayPartSearch() {
        ObservableList<Part> partSearchList;
        String search = partSearch.getText();
        System.out.println(search);
        if (!search.isEmpty()) {

            partSearchList = Inventory.lookupPart(search);

            try {
                Part p = Inventory.lookupPart(Integer.parseInt(search));
                if (!(p == null) && !(partSearchList.contains(p))) {
                    partSearchList.add(p);
                }
            } catch (NumberFormatException ignored) {}

            partTable.setItems(partSearchList);

            if (partSearchList.isEmpty()) {
                sendWarning(partSearchButton, partSearch);
                partTable.setItems(Inventory.getAllParts());
            }
        }
        else {
            partTable.setItems(Inventory.getAllParts());
        }
    }

    /**
     * Method used to search for products.
     */
    public void displayProdSearch() {
        ObservableList<Product> prodSearchList;
        String search = productSearch.getText();

        if (!search.isEmpty()) {

            prodSearchList = Inventory.lookupProduct(search);

            try {
                Product p = Inventory.lookupProduct(Integer.parseInt(search));
                if (!(p == null) && !(prodSearchList.contains(p))) {
                    prodSearchList.add(p);
                }
            } catch (NumberFormatException ignored) {}

            prodTable.setItems(prodSearchList);

            if (prodSearchList.isEmpty()) {
                sendWarning(prodSearchButton, productSearch);
                prodTable.setItems(Inventory.getAllProducts());
            }
        }
        else {
            productSearch.clear();
            prodTable.setItems(Inventory.getAllProducts());
        }
    }

    /**
     * Method for deleting parts.
     */
    public void onClick2DeletePart() {
        Part selectedItem = partTable.getSelectionModel().getSelectedItem();
        if (checkObjectSelected(selectedItem)) {
            partTable.getItems().remove(selectedItem);
            if (Inventory.deletePart(selectedItem))
                warningLabel.setText("");
        }
        else {
            warningLabel.setText("Please select an item to delete.");
        }

    }

    /**
     * Method for deleting products.
     */
    public void onClick2DeleteProduct() {
        Product selectedItem = prodTable.getSelectionModel().getSelectedItem();
        if (checkObjectSelected(selectedItem)) {
            if (Inventory.deleteProduct(selectedItem)) {
                for (Part p : selectedItem.getAllAssociatedParts()) {
                    Inventory.addPart(p);
                }
                selectedItem.getAllAssociatedParts().clear();
                prodTable.getItems().remove(selectedItem);
                warningLabel.setText("");
            }
        }
        else {
            warningLabel.setText("Please select an item to delete.");
        }
    }

    /**
     * Method to create warning when no search matches are found.
     */
    public void sendWarning(Button button, TextField textField) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        EventHandler<ActionEvent> searchWarning = actionEvent -> {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("****** No Matches Found ******");
            alert.show();
        };
        button.setOnAction(searchWarning);
        button.fire();
        textField.clear();
        button.setOnAction(e -> displayPartSearch());
    }

    /**
     * Exits the program.
     */
    @FXML
    protected void onExitButtonClick() { System.exit(0); }
}
