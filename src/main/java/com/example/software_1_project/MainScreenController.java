package com.example.software_1_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static java.lang.System.out;

public class MainScreenController implements Initializable {
    private static InHousePart inHousePart;
    public TableView<Object> partTable = new TableView<>();
    public TableColumn<InHousePart, Integer> partIDCol;
    public TableColumn<InHousePart, String> partNameCol;
    public TableColumn<InHousePart, Integer> invLevelCol;
    public TableColumn<InHousePart, Double> priceCol;
    public static int idNum;
    private static final ObservableList<InHousePart> list = FXCollections.observableArrayList();
    public Label warningLabel;
    public TextField partSearch;

    public static void passData(InHousePart part) {
        inHousePart = new InHousePart(part.getId(), part.getName(), part.getPrice(), part.getStock(), part.getMin(), part.getMax(), part.getMachineCode());

        if (list.size() != 0) {
            for (InHousePart p : list) {
                idNum = p.getId() + 1;
            }
        }
        else {
            idNum = 1;
        }

        part.setId(idNum);

        addToList(part);
    }

    private static void addToList(InHousePart inHP) {
        list.add(inHP);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!(inHousePart == null)) {
            out.println(list.size());

            partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            invLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

            for (InHousePart part : list) {
                partTable.getItems().add(part);
            }
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
        InHousePart selectedItem = (InHousePart) partTable.getSelectionModel().getSelectedItem();
        if (checkSelected(selectedItem)) {
            partTable.getItems().remove(selectedItem);
            list.remove(selectedItem);
            warningLabel.setText("");
        }
        else {
            warningLabel.setText("Please select an item to delete.");
        }
    }

    public boolean checkSelected(InHousePart part) {
        return part != null;
    }

    public void displayText(ActionEvent actionEvent) {
        FilteredList<InHousePart> filteredList = new FilteredList<>(list, p -> true);

        partSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //if ()
        });
    }
}