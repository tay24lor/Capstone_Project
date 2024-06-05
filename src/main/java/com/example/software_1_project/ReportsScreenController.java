package com.example.software_1_project;

import Database.PartDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Part;

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
    public Tab allPartsTab;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatePartTable();
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
        Scene scene = new Scene(root, 883, 400);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }
}
