package com.gamezone.model;

/**
 * Represents a game controller accessory.
 */
public class Controller extends Accessory {

    // Connection type of the controller: "Inalámbrico" or "Alámbrico".
    private String connectionType;

    /**
     * Creates a new Controller.
     *
     * @param id             unique identifier
     * @param title          controller title
     * @param price          unit price
     * @param quantity       quantity available in stock
     * @param connectionType connection type ("Inalámbrico" or "Alámbrico")
     */
    public Controller(String id, String title, double price, int quantity, String connectionType) {
        super(id, title, price, quantity);
        this.connectionType = connectionType;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        String description = getTitle() + " [Controller]";
        description += " - Connection: " + connectionType;
        description += ", Price: $" + getPrice();
        description += ", Stock: " + getQuantity();
        description += ", Compatible consoles: "
                + (getCompatibleConsoleIds().isEmpty() ? "None" : String.join(", ", getCompatibleConsoleIds()));
        return description;
    }
}