package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    public static Part lookupPart(int partID) {
        for (Part part : allParts) {
            if (partID == part.getId()) {
                return part;
            }
            else {
                System.out.println("NA");
            }
        }
        return null;
    }

    public static Product lookupProduct(int productID) {
        for (Product product : allProducts) {
            if (productID == product.getId()) {
                return product;
            }
            else {
                System.out.println("NA");
            }
        }
        return null;
    }


    //public static ObservableList<Part> lookupPart(String partName) {}
    //public static ObservableList<Product> lookupProduct(String productName) {}
    //public static void updatePart(int index, Part selectedPart) {}
    //public static void updateProduct(int index, Product newProduct) {}
    public static boolean deletePart(Part selectedPart) {
        allParts.removeIf(selectedPart::equals);
        return true;
    }
    //public static void boolean deleteProduct(Product selectedProduct) {}
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    //public static ObservableList<Product> getAllProducts() {}

}


