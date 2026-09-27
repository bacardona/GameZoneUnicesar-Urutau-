package com.gamezone.model;

/**
 * Represents a cable accessory.
 */
public class Cable extends Accessory {

    // Atributos propios de Cable
    private float meters;
    private String type;

    /**
     * Creates a new Cable.
     *
     * @param id       unique identifier
     * @param title    cable title
     * @param price    unit price
     * @param quantity quantity available in stock
     * @param meters   length in meters
     * @param type     connector type (HDMI, USB, optical, etc.)
     */
    public Cable(String id, String title, double price, int quantity,
                 float meters, String type) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        String description = getTitle() + " [Cable]";
        description += " - Length: " + meters + "m";
        description += ", Connector: " + type;
        description += ", Price: $" + getPrice();
        description += ", Stock: " + getQuantity();
        return description;
    }
}

