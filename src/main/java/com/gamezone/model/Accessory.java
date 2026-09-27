package com.gamezone.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract class representing a generic accessory sold by GameZone Unicesar.
 * Extends {@link Product} to reuse the common attributes and behavior of
 * every sellable item (id, title, price, quantity), and adds the notion of
 * console compatibility, which is shared by all accessory types even though
 * not every concrete accessory makes use of it (for example, cables never
 * declare compatible consoles).
 */
public abstract class Accessory extends Product {

    // List of console ids this accessory is compatible with.
    private List<String> compatibleConsoleIds;

    /**
     * Creates a new Accessory with its common attributes and an empty
     * compatibility list.
     *
     * @param id       unique identifier of the accessory
     * @param title    title of the accessory
     * @param price    unit price of the accessory
     * @param quantity quantity currently available in stock
     */
    public Accessory(String id, String title, double price, int quantity) {
        super(id, title, price, quantity);
        this.compatibleConsoleIds = new ArrayList<>();
    }

    /**
     * Returns an unmodifiable view of the console ids this accessory is
     * compatible with.
     *
     * @return the list of compatible console ids
     */
    public List<String> getCompatibleConsoleIds() {
        return Collections.unmodifiableList(compatibleConsoleIds);
    }

    /**
     * Replaces the full list of compatible console ids.
     *
     * @param compatibleConsoleIds the new list of compatible console ids
     */
    public void setCompatibleConsoleIds(List<String> compatibleConsoleIds) {
        this.compatibleConsoleIds = new ArrayList<>(compatibleConsoleIds);
    }

    /**
     * Adds a single console id to the compatibility list, if it is not
     * already present.
     *
     * @param consoleId the id of the compatible console
     */
    public void addCompatibleConsoleId(String consoleId) {
        if (consoleId != null && !compatibleConsoleIds.contains(consoleId)) {
            compatibleConsoleIds.add(consoleId);
        }
    }

    /**
     * Checks whether this accessory is compatible with the given console id.
     *
     * @param consoleId the id of the console to check
     * @return true if the accessory is compatible with that console
     */
    public boolean isCompatibleWith(String consoleId) {
        return compatibleConsoleIds.contains(consoleId);
    }

    /**
     * {@inheritDoc}
     * Base implementation shared by every accessory type: shows the
     * accessory's title, price, stock, and compatible consoles. Concrete
     * subclasses override this method to add their own specific attributes.
     */
    @Override
    public String getDescription() {
        String description = getTitle() + " [Accessory]";
        description += ", Price: $" + getPrice();
        description += ", Stock: " + getQuantity();
        description += ", Compatible consoles: "
                + (compatibleConsoleIds.isEmpty() ? "None" : String.join(", ", compatibleConsoleIds));
        return description;
    }
}
