
package com.gamezone.model;
/**
 * Abstract base class representing a generic product sold by GameZoneUnicesar.
 */
public abstract class Product {

    // Atributos comunes a todos los productos
    private String id;
    private String title;
    private double price;
    private int quantity;

    /**
     * Creates a new Product with its common attributes.
     *
     * @param id       unique identifier of the product
     * @param title    title of the product
     * @param price    unit price of the product
     * @param quantity quantity currently available in stock
     */
    public Product(String id, String title, double price, int quantity) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        
    }
    // Getters y setters, uno por cada atributo

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    /**
     * Builds a full description of the product. Each subclass implements
     * this method in its own way (polymorphism).
     *
     * @return a textual description of the product
     */
    public abstract String getDescription();
}
    
