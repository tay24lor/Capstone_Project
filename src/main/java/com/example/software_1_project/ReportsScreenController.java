package com.example.software_1_project;

import Database.PartDAO;
import Database.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ReportsScreenController implements Initializable {
    public TableView<Part> reportsAllPartsTable = new TableView<>();
    public TableColumn<Part, Integer> repAllPartsIdCol;
    public TableColumn<Part, String> repAllPartsNameCol;
    public TableColumn<Part, Integer> repAllPartsStockCol;
    public TableColumn<Part, String> repAllPartsDateCol;

    public TableView<Part> repInHouseTable;
    public TableColumn<Part, Integer> repInHouseIdCol;
    public TableColumn<Part, String> repInHouseNameCol;
    public TableColumn<Part, Integer> repInHouseStockCol;
    public TableColumn<Part, Integer> repMachCodeCol;
    public TableColumn<Part, String> repInHouseDateCol;

    public TableView<Part> repOutSourceTable;
    public TableColumn<Part, Integer> repOutSourceIdCol;
    public TableColumn<Part, String> repOutSourceNameCol;
    public TableColumn<Part, Integer> repOutSourceStockCol;
    public TableColumn<Part, String> repCompNameCol;
    public TableColumn<Part, String> repOutSourceDateCol;
    public ComboBox<String> repComboBox = new ComboBox<>();
    public TabPane partsTabPane;
    public TabPane prodTabPane;
    public TableView<Product> repAllProdTable;
    public TableColumn<Product, Integer> repProdIdCol;
    public TableColumn<Product, String> repProdNameCol;
    public TableColumn<Product, Integer> repProdStockCol;
    public TableColumn<Product, Double> repProdCostCol;
    public TableColumn<Product, String> repProdDateCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<String> choices = FXCollections.observableArrayList();
        choices.add("Parts");
        choices.add("Products");

        repComboBox.setItems(choices);
        repComboBox.getSelectionModel().selectFirst();

        generatePartTable();
        generateProductTable();

        partsTabPane.setVisible(true);
        prodTabPane.setVisible(false);
    }

    private void generateProductTable() {
        repAllProdTable.getItems().clear();
        ProductDAO.getProducts().clear();
        ProductDAO.setProducts();

        repProdIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        repProdNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        repProdStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        repProdCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        repProdDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        repAllProdTable.setItems(ProductDAO.getProducts());
    }

    private void generatePartTable() {
        reportsAllPartsTable.getItems().clear();
        PartDAO.getParts().clear();
        PartDAO.setParts();

        repAllPartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        repAllPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        repAllPartsStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        repAllPartsDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        reportsAllPartsTable.setItems(PartDAO.getParts());
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1016, 639);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    public void fillInHouseTable() {
        ObservableList<Part> inHouseParts;
        repInHouseTable.getItems().clear();
        inHouseParts = PartDAO.getInHouseParts();

        repInHouseIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        repInHouseNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        repInHouseStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        repMachCodeCol.setCellValueFactory(new PropertyValueFactory<>("machineCode"));
        repInHouseDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        repInHouseTable.setItems(inHouseParts);

    }

    public void fillOutsourceTable() {
        ObservableList<Part> outSourcedParts;
        repOutSourceTable.getItems().clear();
        outSourcedParts = PartDAO.getOutSourcedParts();

        repOutSourceIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        repOutSourceNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        repOutSourceStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        repCompNameCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        repOutSourceDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        repOutSourceTable.setItems(outSourcedParts);
    }

    public void getReport() {
        String option = repComboBox.getValue();
        switch (option) {
            case "Parts" :
                partsTabPane.setVisible(true);
                prodTabPane.setVisible(false);
                break;
            case "Products" :
                partsTabPane.setVisible(false);
                prodTabPane.setVisible(true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + option);
        }
    }
}
