package com.example.software_1_project;

import Database.PartDAO;
import Database.ProductDAO;
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
import javafx.stage.Stage;
import model.InHousePart;
import model.OutSourcedPart;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
    public TextField productSearch;
    public Button prodSearchButton;
    public Button deleteButton;
    public Button sampleButton;
    public Button createAcctButton;
    Alert alert = new Alert(Alert.AlertType.NONE);

    /**
     * Initialize preloads the tables with data if first time launching the program.
     * BUG FIX: At first I tried to set the 'firstTime' variable from true to false in the initialize method, however
     * this caused it to always be set to true or always set to false. So the fields would always populate or never
     * populate. To fix this I moved the variable to MainScreen.java and only allowed that class to get and set it.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatePartTable();
        generateProductTable();
        System.out.println(PartDAO.getParts().size());

        if (LoginScreenController.adminStatus()) {
            System.out.println("Admin");
            createAcctButton.setDisable(false);
        }
        else {
            System.out.println("Standard");
            createAcctButton.setDisable(true);
        }
    }

    /**
     * Create part table columns and populate with data.
     */
    private void generatePartTable() {
        partTable.getItems().clear();
        PartDAO.getParts().clear();
        PartDAO.setParts();
        System.out.println("SIZE in main screen: " + PartDAO.getParts().size());

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        invLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partTable.setItems(PartDAO.getParts());
    }

    /**
     * Create product table columns and populate with data.
     */
    private void generateProductTable() {
        prodTable.getItems().clear();
        ProductDAO.getProducts().clear();
        ProductDAO.setProducts();

        prodIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        prodTable.setItems(ProductDAO.getProducts());
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
     * FUTURE ENHANCEMENT: Search by machine code or company name.
     */
    public void displayPartSearch() {
        ObservableList<Part> partSearchList;
        String search = partSearch.getText();
        if (!search.isEmpty()) {

            partSearchList = PartDAO.lookupPart(search);

            try {
                Part p = PartDAO.lookupPart(Integer.parseInt(search));
                if (!(p == null) && !(partSearchList.contains(p))) {
                    partSearchList.add(p);
                }
            } catch (NumberFormatException ignored) {}

            partTable.setItems(partSearchList);

            if (partSearchList.isEmpty()) {
                sendWarning(partSearchButton, partSearch, 1);
                partTable.setItems(PartDAO.getParts());
            }
        }
        else {
            partTable.setItems(PartDAO.getParts());
        }
    }

    /**
     * Method used to search for products.
     * FUTURE ENHANCEMENT: The ability to search for products by parts associated.
     */
    public void displayProdSearch() {
        ObservableList<Product> prodSearchList;
        String search = productSearch.getText();

        if (!search.isEmpty()) {

            prodSearchList = ProductDAO.lookupProduct(search);

            try {
                Product p = ProductDAO.lookupProduct(Integer.parseInt(search));
                if (!(p == null) && !(prodSearchList.contains(p))) {
                    prodSearchList.add(p);
                }
            } catch (NumberFormatException ignored) {}

            prodTable.setItems(prodSearchList);

            if (prodSearchList.isEmpty()) {
                sendWarning(prodSearchButton, productSearch, 2);
                prodTable.setItems(ProductDAO.getProducts());
            }
        }
        else {
            productSearch.clear();
            prodTable.setItems(ProductDAO.getProducts());
        }
    }

    /**
     * Method for deleting parts.
     * FUTURE ENHANCEMENT: Allowing the user to select multiple parts and delete them all with one click.
     */
    public boolean onClick2DeletePart() throws SQLException {
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete this part?");

        Part selectedItem = partTable.getSelectionModel().getSelectedItem();
        if (checkObjectSelected(selectedItem)) {
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                partTable.getItems().remove(selectedItem);
                warningLabel.setText("");
                PartDAO.deletePart(selectedItem.getId());
                generatePartTable();
                return true;
            }
            else {
                warningLabel.setText("Part was not deleted.");
            }
        }
        else {
            warningLabel.setText("Please select an item to delete.");
        }
        return false;
    }

    /**
     * Method for deleting products.
     * BUG FIX: Deleting a product caused the associated parts to be deleted as well. This was fixed by checking if
     * the selected item's associated parts list was empty or not. If not, the user is given a warning that the
     * product has parts associated and cannot be deleted.
     */
    public boolean onClick2DeleteProduct() throws SQLException {
        Product selectedItem = prodTable.getSelectionModel().getSelectedItem();

        if (checkObjectSelected(selectedItem)) {

            if ( (PartDAO.getAsscParts(selectedItem).isEmpty()) ) {
                alert.setAlertType(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to delete this product?");
                alert.showAndWait();

                if (alert.getResult() == ButtonType.OK) {
                    prodTable.getItems().remove(selectedItem);
                    warningLabel.setText("Product successfully deleted.");
                    ProductDAO.delete(selectedItem.getId());
                    return true;
                }
                else {
                    warningLabel.setText("Product was not deleted.");
                }
            }
            else {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("Product could not be deleted because it has parts associated with it.");
                alert.show();
            }
        }
        else {
            warningLabel.setText("Please select an item to delete.");
        }
        return false;
    }

    /**
     * Method to create warning when no search matches are found.
     */
    public void sendWarning(Button button, TextField textField, int b) {
        EventHandler<ActionEvent> searchWarning = actionEvent -> {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("****** No Matches Found ******");
            alert.show();
        };
        button.setOnAction(searchWarning);
        button.fire();
        textField.clear();
        if (b == 1)
            button.setOnAction(e -> displayPartSearch());
        else if (b == 2)
            button.setOnAction(e -> displayProdSearch());
    }

    /**
     * Exits the program.
     */
    @FXML
    protected void onExitButtonClick(ActionEvent actionEvent) throws IOException {
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to exit?");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("loginScreen.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * NEW FUNCTION
     * Opens reports page
     * @param actionEvent action event
     */
    public void onClick2ViewReports(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("reportsScreen.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 740, 580);
        stage.setScene(scene);
        stage.show();
    }

    public void loadSamples() throws SQLException {
        if (PartDAO.getParts().isEmpty() && ProductDAO.getProducts().isEmpty()) {
            InHousePart part1 = new InHousePart(1, "Pedals", 1.00, 1, 1, 20, 0);
            InHousePart part2 = new InHousePart(2, "Chains", 1.00, 1, 1, 20, 0);
            OutSourcedPart part3 = new OutSourcedPart(3, "Seats", 1.00, 1, 1, 20, 0);
            OutSourcedPart part4 = new OutSourcedPart(4, "Handlebars", 1.00, 1, 1, 20, 0);

            part1.setMachineCode(101);
            part2.setMachineCode(102);

            part3.setCompanyName("Bike Co.");
            part4.setCompanyName("Chains & Things");

            PartDAO.insert(part1, part1.getMachineCode(), "");
            PartDAO.insert(part2, part2.getMachineCode(), "");
            PartDAO.insert(part3, 0, part3.getCompanyName());
            PartDAO.insert(part4, 0, part4.getCompanyName());

            Product prod1 = new Product(1, "Adult Bike", 200.00, 20, 1, 35/*, ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))*/);
            Product prod2 = new Product(2, "Kid Bike", 100.00, 10, 1, 20/*, ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))*/);

            ProductDAO.insert(prod1);
            ProductDAO.insert(prod2);

            generatePartTable();
            generateProductTable();
        }
        else {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Tables must be empty.");
            alert.showAndWait();
        }
    }

    public void onClick2CreateAcct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("createAcctScreen.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 740, 580);
        stage.setScene(scene);
        stage.show();
    }
}
