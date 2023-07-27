package com.example.software_1_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModifyProductScreenController implements Initializable {

    private static Product prod;
    public TextField modProdIDField;
    public TextField modProdNameField;
    public TextField modProdInvField;
    public TextField modProdPriceField;
    public TextField modProdMaxField;
    public TextField modProdMinField;

    public static void sendProdData(Product selectedItem) {
        prod = selectedItem;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modProdIDField.setText(String.valueOf(prod.getId()));
        modProdNameField.setText(prod.getName());
        modProdInvField.setText(String.valueOf(prod.getStock()));
        modProdPriceField.setText(String.valueOf(prod.getPrice()));
        modProdMaxField.setText(String.valueOf(prod.getMax()));
        modProdMinField.setText(String.valueOf(prod.getMin()));
    }
    public void onClick2Cancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 883, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public void onClick2SaveModifyProduct(ActionEvent actionEvent) throws IOException {
        prod.setName(modProdNameField.getText());
        prod.setStock(Integer.parseInt(modProdInvField.getText()));
        prod.setPrice(Double.parseDouble(modProdPriceField.getText()));
        prod.setMax(Integer.parseInt(modProdMaxField.getText()));
        prod.setMin(Integer.parseInt(modProdMinField.getText()));
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 883, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
