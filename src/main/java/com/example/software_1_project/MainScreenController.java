package com.example.software_1_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    public TableView partTable = new TableView<>();
    public TableColumn partIDCol;
    public TableColumn partNameCol;
    public TableColumn invLevelCol;
    public TableColumn priceCol;

    private static ObservableList<InHousePart> list = FXCollections.observableArrayList();

    public static void passData(InHousePart part) {
        inHousePart = new InHousePart(part.getId(), part.getName(), part.getPrice(), part.getStock(), part.getMin(), part.getMax());
        int i = 1;
        for (InHousePart p : list) {
            if (i <= p.getId()) i++;
        }

        part.setId(i);
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ModifyPartScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 650, 600);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
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
        partTable.getItems().remove(selectedItem);
        list.remove(selectedItem);
    }
}