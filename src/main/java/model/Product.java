package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    private int id;
    private String name;
    private Double price;
    private int stock;
    private int min;
    private int max;

    // Constructor
    public Product(int id, String name, Double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Double getPrice() {
        return price;
    }
    public int getStock() {
        return stock;
    }
    public int getMin() {
        return min;
    }
    public int getMax() {
        return max;
    }

    public void addAssociatePart(Part part) {
        associatedParts.add(part);
    }
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        associatedParts.remove(selectedAssociatedPart);
        return false;
    }
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
