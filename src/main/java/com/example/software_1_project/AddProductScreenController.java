package com.example.software_1_project;

import Database.PartDAO;
import Database.ProductDAO;
import javafx.collections.FXCollections;
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
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddProductScreenController implements Initializable {

    public TableView<Part> prodPartTable;
    public TableColumn<Part, Integer> partIDCol;
    public TableColumn<Part, String> partNameCol;
    public TableColumn<Part, Integer> invLevelCol;
    public TableColumn<Part, Double> priceCol;
    public TableView<Part> partsLinkedTable;
    public TableColumn<Part, Integer> linkPartIDCol;
    public TableColumn<Part, String> linkPartNameCol;
    public TableColumn<Part, String> linkInvLvlCol;
    public TableColumn<Part, String> linkCostCol;
    public TextField prodIDField;
    public TextField prodNameField;
    public TextField prodStockField;
    public TextField prodPriceField;
    public TextField prodMaxField;
    public TextField prodMinField;
    public TextField addProdPartSearch;
    public Button prodPartSearchButton;
    public Alert alert;
    public Button addProdSaveButton;
    public Label noPartToAddLabel;
    public Label noPartToRemoveLabel;
    public ObservableList<Part> partSearchList;

    private Product product = null;
    private final ObservableList<Part> assocParts = FXCollections.observableArrayList();
    private final ObservableList<Part> parts = FXCollections.observableArrayList();

    protected int x = 1016;
    protected int y = 639;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            product = new Product(ProductDAO.getLatestId(), "", 0.00, 0, 0, 0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        parts.addAll(PartDAO.getParts());
        System.out.println("Prod ID: " + product.getId());
        if (!(parts.isEmpty())) {
            partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            invLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

            linkPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            linkPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            linkInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            linkCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

            prodPartTable.setItems(parts);
        }
    }
    public void onClick2Cancel(ActionEvent actionEvent) throws IOException, SQLException {
        for (Part p : PartDAO.getAsscParts(product)) {
            PartDAO.updateProdId(p, -1);
        }
        setTables();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, x, y);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    public void onClick2Save(ActionEvent actionEvent) throws IOException, SQLException {
        String price = prodPriceField.getText();
        String stock = prodStockField.getText();
        String max = prodMaxField.getText();
        String min = prodMinField.getText();

        if (validateFields(price, stock, max, min)) {
            product.setId(ProductDAO.getLatestId());
            product.setName(prodNameField.getText()); product.setStock(Integer.parseInt(stock));
            product.setPrice(Double.parseDouble(price)); product.setMax(Integer.parseInt(max));
            product.setMin(Integer.parseInt(min));

            for (Part part : assocParts) {
                PartDAO.updateProdId(part, product.getId());
            }
            ProductDAO.insert(product);

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, x, y);
            stage.setTitle("Main Screen");
            stage.setScene(scene);
            stage.show();
        }
        else {
            sendExceptionWarning();
            addProdSaveButton.setOnAction(e -> {
                try {
                    onClick2Save(e);
                } catch (IOException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    public void add2PartsLinked() {
        Part selectedPart = prodPartTable.getSelectionModel().getSelectedItem();

        if (!(selectedPart == null)) {
            assocParts.add(selectedPart);
            parts.remove(selectedPart);
        }
        else {
            noPartToAddLabel.setText("No part selected...");
        }
        setTables();
    }
    public void removeAssocPart() throws SQLException {
        noPartToRemoveLabel.setText("");
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        Part selectedPart = partsLinkedTable.getSelectionModel().getSelectedItem();
        if (!(selectedPart == null)) {

            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                PartDAO.updateProdId(selectedPart, -1);
                parts.add(selectedPart);
                assocParts.remove(selectedPart);
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
        partsLinkedTable.setItems(assocParts);
        PartDAO.getParts().clear();
        PartDAO.setParts();
        prodPartTable.setItems(parts);
        partsLinkedTable.getSortOrder().add(linkPartIDCol);
        prodPartTable.getSortOrder().add(partIDCol);
        partsLinkedTable.sort();
        prodPartTable.sort();

    }

    public void displayProdPartSearch() {

        String search = addProdPartSearch.getText();

        if (!search.isEmpty()) {


            try {
                partSearchList = PartDAO.lookupPart(search);
                Part p = PartDAO.lookupPart(Integer.parseInt(search));
                if (!(p == null) && !(partSearchList.contains(p))) {
                    partSearchList.add(p);
                }
            } catch (NumberFormatException ignored) {}

            if (partSearchList.isEmpty()) {
                sendWarning();
            }
            else {
                prodPartTable.setItems(partSearchList);
            }
        }
        else {
            prodPartTable.setItems(PartDAO.getParts());
        }
    }

    public void sendWarning() {
        EventHandler<ActionEvent> searchWarning = actionEvent -> {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("****** No Matches Found ******");
            alert.show();
        };
        prodPartSearchButton.setOnAction(searchWarning);
        prodPartSearchButton.fire();
        addProdPartSearch.clear();
        prodPartSearchButton.setOnAction(a -> displayProdPartSearch());
    }
    public boolean validateFields(String price, String stock, String max, String min) {

        try {
            Double.parseDouble(price);
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Price must be a decimal number ******");
            prodPriceField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        try {
            Integer.parseInt(stock);
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Inventory must be a whole number ******");
            prodStockField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        try {
            Integer.parseInt(max);
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Maximum field must be a whole number ******");
            prodMaxField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        try {
            Integer.parseInt(min);
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Minimum field must be a whole number ******");
            prodMinField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        if (Integer.parseInt(max) < Integer.parseInt(min)) {
            alert.setContentText("****** Maximum is lower than minumum ******");
            prodMaxField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        return validateStockCount(stock, max, min);
    }

    public boolean validateStockCount(String stock, String max, String min) {
        if (Integer.parseInt(stock) > Integer.parseInt(max) ||
                Integer.parseInt(stock) < Integer.parseInt(min)) {
            alert.setContentText("****** Inventory is outside the max/min range ******");
            prodStockField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        return true;
    }

    public void sendExceptionWarning() {
        EventHandler<ActionEvent> fieldWarning = actionEvent -> {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.show();
        };
        addProdSaveButton.setOnAction(fieldWarning);
        addProdSaveButton.fire();
        alert.setOnCloseRequest(e -> clearFields());

    }
    public void clearFields() {
        prodNameField.setBorder(Border.EMPTY);
        prodStockField.setBorder(Border.EMPTY);
        prodPriceField.setBorder(Border.EMPTY);
        prodMaxField.setBorder(Border.EMPTY);
        prodMinField.setBorder(Border.EMPTY);
    }
}
