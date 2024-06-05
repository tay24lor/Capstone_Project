package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Inventory class to manage parts and products.
 * @author Taylor Aubrey
 */
public class Inventory {

    /** List for parts. */
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();

    /** List for products. */
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *
     * @param newPart part to add.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     *
     * @param newProduct product to add.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     *
     * @param partID the id to search for.
     * @return the part with matching id or null if no match found.
     */
    public static Part lookupPart(int partID) {
        for (Part part : allParts) {
            if (partID == part.getId()) {
                return part;
            }
        }
        System.out.println("NA");
        return null;
    }

    /**
     *
     * @param productID the id to search for.
     * @return the product with matching id or null if no match found.
     */
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

    /**
     *
     * @param partName the part being searched for.
     * @return a list matches and partial matches.
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> results = FXCollections.observableArrayList();
        for (Part p : allParts) {
            if (p.getName().toLowerCase().contains(partName.toLowerCase())) {
                results.add(p);
            }
        }
        return results;
    }

    /**
     *
     * @param productName the product being searched for.
     * @return a list of matches and partial matches.
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> results = FXCollections.observableArrayList();
        for (Product p : allProducts) {
            if (p.getName().toLowerCase().contains(productName.toLowerCase())) {
                results.add(p);
            }
        }
        return results;
    }

    /**
     *
     * @param index the index of the old part.
     * @param selectedPart the modified part to replace the old one.
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.remove(index);
        allParts.add(index, selectedPart);
    }

    /**
     *
     * @param index the index of the old product.
     * @param newProduct the modified product to replace the old one.
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.remove(index);
        allProducts.add(index, newProduct);
    }

    /**
     * @param selectedPart the part to delete.
     * @return true if part is deleted.
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.removeIf(selectedPart::equals);
    }

    /**
     * @param selectedProduct the product to delete.
     * @return true if product is deleted.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.removeIf(selectedProduct::equals);
    }

    /**
     * @return the list allParts.
     */
    public static ObservableList<Part> getAllParts() { return allParts; }

    /**
     * @return the list allProducts.
     */
    public static ObservableList<Product> getAllProducts() { return allProducts; }

}


