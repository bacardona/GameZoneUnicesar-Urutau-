
package com.gamezone.model;

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
}
    
