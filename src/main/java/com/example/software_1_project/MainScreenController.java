package com.example.software_1_project;

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
import model.InHousePart;
import model.Inventory;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static java.lang.System.out;

public class MainScreenController implements Initializable {
    public TableView<Part> partTable = new TableView<>();
    public TableColumn<Part, Integer> partIDCol;
    public TableColumn<Part, String> partNameCol;
    public TableColumn<Part, Integer> invLevelCol;
    public TableColumn<Part, Double> priceCol;
    public Label warningLabel;
    public TextField partSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!(Inventory.getAllParts() == null)) {
            out.println(Inventory.getAllParts().size());

            partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            invLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));


            partTable.setItems(Inventory.getAllParts());
            out.println(Inventory.getAllParts().size());

        }
    }

    @FXML
    protected void onHelloButtonClick() {
        System.exit(0);
    }

    public void onClick2AddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddPartScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 650, 600);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    public void onClick2AddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddProductScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 530);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    public void onClick2ModPart(ActionEvent actionEvent) throws IOException {
        InHousePart selectedItem = (InHousePart) partTable.getSelectionModel().getSelectedItem();
        if (checkSelected(selectedItem)) {   // Check if no item is selected before sending data to modify screen
            ModifyPartScreenController.sendData(selectedItem);
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ModifyPartScreen.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 650, 600);
            stage.setTitle("Add Part");
            stage.setScene(scene);
            stage.show();
        }
        else {
            warningLabel.setText("Please select an item to modify.");
        }

    }

    public void onClick2ModifyProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ModifyProductScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 530);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    public void onClick2Delete() {
        Part selectedItem = partTable.getSelectionModel().getSelectedItem();
        if (checkSelected(selectedItem)) {
            partTable.getItems().remove(selectedItem);
            Inventory.deletePart(selectedItem);
            warningLabel.setText("");
        }
        else {
            warningLabel.setText("Please select an item to delete.");
        }
    }

    public boolean checkSelected(Part part) {
        return part != null;
    }

    public void displayPartSearch() {
        FilteredList<Part> filteredList = new FilteredList<>(Inventory.getAllParts(), p -> true);

        partSearch.textProperty().addListener((observableValue, oldValue, newValue) -> filteredList.setPredicate(part -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            if (part.getName().contains(newValue)) {
                return true;
            }
            else return String.valueOf(part.getId()).equals(newValue);
        }));

        SortedList<Part> sortedData = new SortedList<>(filteredList);

        //sortedData.comparatorProperty().bind(Inventory.getAllParts().sorted().comparatorProperty());

        partTable.setItems(sortedData);
    }
}