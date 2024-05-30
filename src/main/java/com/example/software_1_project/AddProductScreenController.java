package com.example.software_1_project;

import Database.PartDAO;
import Database.ProductDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.util.*;

public class AddProductScreenController implements Initializable {

    private final Product product = new Product(0, "", 0.00, 0, 0, 0);
    public TableView<Part> prodPartTable;
    public TableColumn<Part, Integer> partIDCol;
    public TableColumn<Part, String> partNameCol;
    public TableColumn<Part, Integer> invLevelCol;
    public TableColumn<Part, Double> priceCol;
    public TableView<List<StringProperty>> partsLinkedTable;
    public TableColumn<List<StringProperty>, String> linkPartIDCol;
    public TableColumn<List<StringProperty>, String> linkPartNameCol;
    public TableColumn<List<StringProperty>, String> linkInvLvlCol;
    public TableColumn<List<StringProperty>, String> linkCostCol;
    public TextField prodIDField;
    public TextField prodNameField;
    public TextField prodStockField;
    public TextField prodPriceField;
    public TextField prodMaxField;
    public TextField prodMinField;
    public TextField addProdPartSearch;
    public Button prodPartSearchButton;
    public Alert alert = new Alert(Alert.AlertType.NONE);
    public Button addProdSaveButton;
    public Label noPartToAddLabel;
    public Label noPartToRemoveLabel;
    public ObservableList<Part> partSearchList;
    public ObservableList<List<StringProperty>> data = FXCollections.observableArrayList();

    public int linkedParts = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!(PartDAO.getParts() == null)) {
            partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            invLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

            /*linkPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            linkPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            linkInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            linkCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));*/

            linkPartIDCol.setCellValueFactory(data -> data.getValue().get(0));
            linkPartNameCol.setCellValueFactory(data -> data.getValue().get(1));
            linkInvLvlCol.setCellValueFactory(data -> data.getValue().get(2));
            linkCostCol.setCellValueFactory(data -> data.getValue().get(3));

            prodPartTable.setItems(PartDAO.getParts());
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
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    public void onClick2Save(ActionEvent actionEvent) throws IOException, SQLException {
        if (validateFields()) {
            product.setId(ProductDAO.getProducts().size() + 1);
            product.setName(prodNameField.getText()); product.setStock(Integer.parseInt(prodStockField.getText()));
            product.setPrice(Double.parseDouble(prodPriceField.getText())); product.setMax(Integer.parseInt(prodMaxField.getText()));
            product.setMin(Integer.parseInt(prodMinField.getText()));

            ProductDAO.insert(product);

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 883, 400);
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

    private void generateID(Product product) {
        int id = 0;
        for (Product p : Inventory.getAllProducts()) {
            id = p.getId();
        }
        id++;
        product.setId(id);
    }

    public void add2PartsLinked() throws SQLException {
        Part selectedPart = prodPartTable.getSelectionModel().getSelectedItem();

        List<StringProperty> row = new ArrayList<>();

        if (!(selectedPart == null)) {

            noPartToAddLabel.setText("");
            linkedParts++;
            PartDAO.updateStock(selectedPart.getId(), selectedPart.getStock() - 1);

            row.add(0, new SimpleStringProperty(String.valueOf(selectedPart.getId())));
            row.add(1, new SimpleStringProperty(selectedPart.getName()));
            row.add(2, new SimpleStringProperty(String.valueOf(linkedParts)));
            row.add(3, new SimpleStringProperty(String.valueOf(selectedPart.getPrice())));

            //int total = selectedPart.getStock() + linkedParts;

            data.add(row);

            product.addAssociatePart(selectedPart);

            //Inventory.getAllParts().remove(selectedPart);
            setTables();
        }
        else {
            noPartToAddLabel.setText("No part selected...");
        }
    }
    public void removeAssocPart() {
        /*noPartToRemoveLabel.setText("");
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        Part selectedPart = partsLinkedTable.getSelectionModel().getSelectedItem();
        if (!(selectedPart == null)) {

            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                Inventory.getAllParts().add(selectedPart);
                product.deleteAssociatedPart(selectedPart);
                setTables();
            }
            else {
                noPartToRemoveLabel.setText("Part was not removed.");
            }
        }
        else {
            noPartToRemoveLabel.setText("No part selected...");
        }*/
    }
    public void setTables() {
        partsLinkedTable.setItems(data);
        PartDAO.getParts().clear();
        PartDAO.setParts();
        prodPartTable.setItems(PartDAO.getParts());
    }

    public void displayProdPartSearch() {

        String search = addProdPartSearch.getText();

        if (!search.isEmpty()) {


            try {
                partSearchList = Inventory.lookupPart(search);
                Part p = Inventory.lookupPart(Integer.parseInt(search));
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
            prodPartTable.setItems(Inventory.getAllParts());
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
    private boolean validateFields() {
        try {
            Double.parseDouble(prodPriceField.getText());
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Price must be a decimal number ******");
            prodPriceField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        try {
            Integer.parseInt(prodStockField.getText());
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Inventory must be a whole number ******");
            prodStockField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        try {
            Integer.parseInt(prodMaxField.getText());
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Maximum field must be a whole number ******");
            prodMaxField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        try {
            Integer.parseInt(prodMinField.getText());
        } catch (NumberFormatException ex) {
            alert.setContentText("****** Minimum field must be a whole number ******");
            prodMinField.setBorder(Border.stroke(Paint.valueOf("red")));
            return false;
        }
        if (Integer.parseInt(prodMaxField.getText()) < Integer.parseInt(prodMinField.getText())) {
            prodMaxField.setBorder(Border.stroke(Paint.valueOf("red")));
            alert.setContentText("****** Maximum is lower than minumum ******");
            return false;
        }
        if (Integer.parseInt(prodStockField.getText()) > Integer.parseInt(prodMaxField.getText()) ||
                Integer.parseInt(prodStockField.getText()) < Integer.parseInt(prodMinField.getText())) {
            prodStockField.setBorder(Border.stroke(Paint.valueOf("red")));
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
        addProdSaveButton.setOnAction(fieldWarning);
        addProdSaveButton.fire();
        alert.setOnCloseRequest(e -> clearFields());

    }
    private void clearFields() {
        prodNameField.setBorder(Border.EMPTY);
        prodStockField.setBorder(Border.EMPTY);
        prodPriceField.setBorder(Border.EMPTY);
        prodMaxField.setBorder(Border.EMPTY);
        prodMinField.setBorder(Border.EMPTY);
    }
}
