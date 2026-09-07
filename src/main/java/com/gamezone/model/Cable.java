package com.gamezone.model;
  
/**
 * Represents a cable accesary
 */
public class Cable extends Accesory {

    // Atributos propios de Console
    private float meters;
    private String type;

    /**
     * Creates a new Accesory.
     *
     * @param id         unique identifier
     * @param title      console title
     * @param price      unit price
     * @param quantity   quantity available in stock
     * @param meters      Long in meters
     * @param type      specific type of cable
     */
    public Cable(String id, String title, double price, int quantity,
                    float meters, String type ) {
        super(id, title, price, quantity);
        this.meters = meters;
        this.type = type;
    }

    public float getMeters() {
        return meters;
    }

    public void setMeters(float meters) {
        this.meters = meters;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    

    }


