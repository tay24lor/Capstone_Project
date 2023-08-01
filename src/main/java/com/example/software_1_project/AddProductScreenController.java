package com.example.software_1_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AddProductScreenController implements Initializable {

    public TableView<Part> prodPartTable;
    public TableColumn<Part, Integer> partIDCol;
    public TableColumn<Part, String> partNameCol;
    public TableColumn<Part, Integer> invLevelCol;
    public TableColumn<Part, Double> priceCol;
    public TextField prodIDField;
    public TextField prodNameField;
    public TextField prodStockField;
    public TextField prodPriceField;
    public TextField prodMaxField;
    public TextField prodMinField;
    private final Product product = new Product(0, "", 0.00, 0, 0, 0);
    public TableView<Part> partsLinkedTable;
    public TableColumn<Part, Integer> linkPartIDCol;
    public TableColumn<Part, String> linkPartNameCol;
    public TableColumn<Part, Integer> linkInvLvlCol;
    public TableColumn<Part, Double> linkCostCol;
    //private final ObservableList<Part> partsLinkedList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!(Inventory.getAllParts() == null)) {
            partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            invLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

            linkPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            linkPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            linkInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            linkCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

            prodPartTable.setItems(Inventory.getAllParts());
        }
    }
    public void onClick2Cancel(ActionEvent actionEvent) throws IOException {
        for (Part p : product.getAllAssociatedParts()) {
            Inventory.addPart(p);
        }

        product.getAllAssociatedParts().clear();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 883, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public void onClick2Save(ActionEvent actionEvent) throws IOException {
        product.setName(prodNameField.getText()); product.setStock(Integer.parseInt(prodStockField.getText()));
        product.setPrice(Double.parseDouble(prodPriceField.getText())); product.setMax(Integer.parseInt(prodMaxField.getText()));
        product.setMin(Integer.parseInt(prodMinField.getText()));
        generateID(product);
        Inventory.addProduct(product);

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 883, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private void generateID(Product product) {
        int id = 0;
        for (Product p : Inventory.getAllProducts()) {
            id = p.getId();
        }
        id++;
        product.setId(id);
    }

    public void add2PartsLinked() {
        Part selectedPart = prodPartTable.getSelectionModel().getSelectedItem();
        if (!(selectedPart == null)) {
            product.addAssociatePart(selectedPart);
            Inventory.getAllParts().remove(selectedPart);
            setTables();
        }
    }
    public void removeAssocPart() {
        Part selectedPart = partsLinkedTable.getSelectionModel().getSelectedItem();
        if (!(selectedPart == null)) {
            Inventory.getAllParts().add(selectedPart);
            product.deleteAssociatedPart(selectedPart);
            setTables();
        }
    }
    public void setTables() {
        partsLinkedTable.setItems(product.getAllAssociatedParts());
        prodPartTable.setItems(Inventory.getAllParts());
    }
}
