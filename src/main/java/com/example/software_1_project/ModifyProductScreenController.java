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

/** This class controls the Modify Product screen. */
public class ModifyProductScreenController implements Initializable {

    /** The modified product. */
    private final Product NEWPRODUCT = new Product(0,"",0.00,0,0,0/*, ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))*/);

    /** The product to be modified. */
    private static Product oldProd;

    /** Product ID field. */
    public TextField modProdIDField;

    /** Product name field. */
    public TextField modProdNameField;

    /** Product stock level field. */
    public TextField modProdStockField;

    /** Product cost field. */
    public TextField modProdPriceField;

    /** Product maximum stock field. */
    public TextField modProdMaxField;

    /** Product minimum stock field. */
    public TextField modProdMinField;

    /** Table for all parts available. */
    public TableView<Part> modProdPartTable = new TableView<>();

    /** Part ID column. */
    public TableColumn<Part, Integer> modProdPartIDCol;

    /** Part name column. */
    public TableColumn<Part, String> modProdPartNameCol;

    /** Part stock level column. */
    public TableColumn<Part, Integer> modProdPartStockCol;

    /** Part cost column. */
    public TableColumn<Part, Double> modProdPartCostCol;

    /** Table for parts associated with current product. */
    public TableView<Part> modAscPartTable = new TableView<>();

    /** Associated part ID column. */
    public TableColumn<Part, Integer> modAscPartIDCol;

    /** Associated part name column. */
    public TableColumn<Part, String> modAscPartNameCol;

    /** Associated part stock level column. */
    public TableColumn<Part, Integer> modAscPartStockCol;

    /** Associated part cost column. */
    public TableColumn<Part, Double> modAscPartCostCol;

    /** Search bar for parts. */
    public TextField modProdPartSearch;

    /** Calls the search method. */
    public Button modProdPartSearchButton;

    /** Alert for search mismatches and */
    public Alert alert = new Alert(Alert.AlertType.NONE);

    /** Calls the save product method. */
    public Button modProdSaveButton;

    /** Warning label for no part selected alert. */
    public Label noPartToRemoveLabel;

    /** Warning label for no part selected alert. */
    public Label noPartToAddLabel;

    /** List for search results. */
    public ObservableList<Part> partSearchList = FXCollections.observableArrayList();
    private ObservableList<Part> parts = FXCollections.observableArrayList();

    private ObservableList<Part> assocParts = FXCollections.observableArrayList();

    protected int x = 1016;
    protected int y = 639;


    /** This method is used to send the selected product object from the main screen to the Modify Product screen. */
    public static void sendProdData(Product selectedItem) {
        oldProd = selectedItem;
    }

    /** This initializes the Modify Product fields with the selected product's data. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modProdIDField.setText(String.valueOf(oldProd.getId()));
        modProdNameField.setText(oldProd.getName());
        modProdStockField.setText(String.valueOf(oldProd.getStock()));
        modProdPriceField.setText(String.valueOf(oldProd.getPrice()));
        modProdMaxField.setText(String.valueOf(oldProd.getMax()));
        modProdMinField.setText(String.valueOf(oldProd.getMin()));

        modProdPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProdPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProdPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProdPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        modAscPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modAscPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modAscPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modAscPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        System.out.println("SIZE in modprod 1: " + parts.size());

        parts.clear();
        parts = PartDAO.getParts();
        System.out.println("SIZE in modprod 2: " + parts.size());

        modProdPartTable.getItems().clear();
        modProdPartTable.setItems(parts);

        modAscPartTable.getItems().clear();
        assocParts = PartDAO.getAsscParts(oldProd);
        modAscPartTable.setItems(assocParts);
    }

    /** Cancels product modification. */
    public void onClick2Cancel(ActionEvent actionEvent) throws IOException {
        parts.clear();
        assocParts.clear();
        modAscPartTable.getItems().clear();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, x, y);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    /** Saves product modification. */
    public void onClick2SaveModifyProduct(ActionEvent actionEvent) throws IOException, SQLException {
        if (validateFields()) {
            NEWPRODUCT.setId(Integer.parseInt(modProdIDField.getText()));
            NEWPRODUCT.setName(modProdNameField.getText());
            NEWPRODUCT.setStock(Integer.parseInt(modProdStockField.getText()));
            NEWPRODUCT.setPrice(Double.parseDouble(modProdPriceField.getText()));
            NEWPRODUCT.setMax(Integer.parseInt(modProdMaxField.getText()));
            NEWPRODUCT.setMin(Integer.parseInt(modProdMinField.getText()));

            ProductDAO.update(NEWPRODUCT);
            for (Part part : assocParts) {
                PartDAO.updateProdId(part, NEWPRODUCT.getId());
            }
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, x, y);
            stage.setTitle("Main Screen");
            stage.setScene(scene);
            stage.show();
        }
        else {
            sendExceptionWarning();
            modProdSaveButton.setOnAction(e -> {
                try {
                    onClick2SaveModifyProduct(e);
                } catch (IOException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    /** This method associates parts with the current product. */
    public void modProdAddAscPart() {
        Part selectedPart = modProdPartTable.getSelectionModel().getSelectedItem();
        if (!(selectedPart == null)) {
            noPartToAddLabel.setText("");
            assocParts.add(selectedPart);
            parts.remove(selectedPart);
            setTables();
        }
        else {
            noPartToAddLabel.setText("No part selected...");
        }
    }

    /** This method removes selected part association from the current product. */
    public void modProdRemovePart() throws SQLException {
        noPartToRemoveLabel.setText("");
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        Part selectedPart = modAscPartTable.getSelectionModel().getSelectedItem();
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

    /** This updates the tables with current inventory information. */
    public void setTables() {
        modAscPartTable.setItems(assocParts);
        modProdPartTable.setItems(parts);
    }

    /** This method displays part search results for the user. */
    public void displayModProdPartSearch() {

        String search = modProdPartSearch.getText();

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
                modProdPartTable.setItems(partSearchList);
            }
        }
        else {
            modProdPartTable.setItems(PartDAO.getParts());
        }
    }

    /** Alerts the user to search mismatches. */
    public void sendWarning() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        EventHandler<ActionEvent> searchWarning = actionEvent -> {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("****** No Matches Found ******");
            alert.show();
        };
        modProdPartSearchButton.setOnAction(searchWarning);
        modProdPartSearchButton.fire();
        modProdPartSearchButton.setOnAction(a -> displayModProdPartSearch());
    }

    /** This checks if product fields are valid. */
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

    /** This alerts the user to validation errors. */
    public void sendExceptionWarning() {
        EventHandler<ActionEvent> fieldWarning = actionEvent -> {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.show();
        };
        modProdSaveButton.setOnAction(fieldWarning);
        modProdSaveButton.fire();
        alert.setOnCloseRequest(e -> clearFields());
    }

    /** This clears the highlighting from invalid fields after the alert is acknowledged by the user. */
    private void clearFields() {
        modProdNameField.setBorder(Border.EMPTY);
        modProdStockField.setBorder(Border.EMPTY);
        modProdPriceField.setBorder(Border.EMPTY);
        modProdMaxField.setBorder(Border.EMPTY);
        modProdMinField.setBorder(Border.EMPTY);
    }
}
