
package com.gamezone.model;

/**
 * Represents a game console product.
 */
public class Console extends Product {

    // Atributos propios de Console
    private String brand;
    private String model;
    private String generation;

    /**
     * Creates a new Console.
     *
     * @param id         unique identifier
     * @param title      console title
     * @param price      unit price
     * @param quantity   quantity available in stock
     * @param brand      manufacturer brand
     * @param model      specific model
     * @param generation hardware generation
     */
    public Console(String id, String title, double price, int quantity,
                    String brand, String model, String generation) {
        super(id, title, price, quantity);
        this.brand = brand;
        this.model = model;
        this.generation = generation;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }
     /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        String descripcion = getTitle() + " [Console]";
        descripcion = descripcion + " - Brand: " + brand;
        descripcion = descripcion + ", Model: " + model;
        descripcion = descripcion + ", Generation: " + generation;
        descripcion = descripcion + ", Price: $" + getPrice();
        descripcion = descripcion + ", Stock: " + getQuantity();
        return descripcion;
    }
}