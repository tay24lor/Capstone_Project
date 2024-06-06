package model;

import Database.PartDAO;
import Database.ProductDAO;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;


/** Product class.
 * @author Taylor Aubrey
 */
public class Product {
    //private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();


    private int id;
    private String name;
    private Double price;
    private int stock;
    private int min;
    private int max;
    private String dateTime;

    /**
     *
     * @param id id to set.
     * @param name name to set.
     * @param price price to set.
     * @param stock stock to set.
     * @param min minimum to set.
     * @param max maximum to set.
     */
    public Product(int id, String name, Double price, int stock, int min, int max, String dateTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.dateTime = dateTime;
    }

    /**
     *
     * @param id id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @param name name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param price price to set.
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     *
     * @param stock stock to set.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     *
     * @param min minimum to set.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     *
     * @param max maximum to set.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     *
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the price.
     */
    public Double getPrice() {
        return price;
    }

    /**
     *
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     *
     * @return the minimum.
     */
    public int getMin() {
        return min;
    }

    /**
     *
     * @return the maximum.
     */
    public int getMax() {
        return max;
    }

    /**
     *
     * @param part the part to associate with the product.
     */
    public void addAssociatePart(Part part) throws SQLException {
        PartDAO.updateProdId(part, this.getId());

    }

    /**
     *
     * @param part the part to remove from product.
     */
    public void deleteAssociatedPart(Part part) throws SQLException {
        PartDAO.updateProdId(part, 0);
    }

    /**
     *
     * @return the product's associated parts list.
     */
    public ObservableList<Part> getAllAssociatedParts() throws SQLException {
        return PartDAO.getAsscParts(this);
    }
    public String getDate() { return dateTime; }

    public void setDate(String dateTime) {
        this.dateTime = String.valueOf(dateTime);
    }
}
