package com.gamezone.model;

/**
 * Represents a memory accessory (SD card, microSD card, or internal storage card).
 */
public class Memory extends Accessory {

    // Atributos propios de Memory
    private int capacityGb;
    private String memoryType;

    /**
     * Creates a new Memory accessory.
     *
     * @param id         unique identifier
     * @param title      memory title
     * @param price      unit price
     * @param quantity   quantity available in stock
     * @param capacityGb storage capacity in gigabytes
     * @param memoryType type of memory (SD, microSD, internal card)
     */
    public Memory(String id, String title, double price, int quantity,
                  int capacityGb, String memoryType) {
        super(id, title, price, quantity);
        this.capacityGb = capacityGb;
        this.memoryType = memoryType;
    }

    public int getCapacityGb() {
        return capacityGb;
    }

    public void setCapacityGb(int capacityGb) {
        this.capacityGb = capacityGb;
    }

    public String getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        String description = getTitle() + " [Memory]";
        description += " - Capacity: " + capacityGb + "GB";
        description += ", Type: " + memoryType;
        description += ", Price: $" + getPrice();
        description += ", Stock: " + getQuantity();
        description += ", Compatible consoles: "
                + (getCompatibleConsoleIds().isEmpty() ? "None" : String.join(", ", getCompatibleConsoleIds()));
        return description;
    }
}