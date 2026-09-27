package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a promotion that applies a percentage discount only to the
 * products of a specific category within a sale (video games or consoles).
 */
public class CategoryDiscount extends Promotion {

    // Porcentaje de descuento (entre 0 y 100)
    private double percentage;
    // Categoría objetivo: "VIDEOGAME" o "CONSOLE"
    private String targetCategory;

    /**
     * Creates a new CategoryDiscount.
     *
     * @param id             unique identifier
     * @param name           display name of the promotion
     * @param startDate      date from which the promotion becomes valid
     * @param endDate        date until which the promotion remains valid
     * @param percentage     discount percentage, between 0 and 100
     * @param targetCategory the category this promotion applies to
     *                       ("VIDEOGAME" or "CONSOLE")
     */
    public CategoryDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                             double percentage, String targetCategory) {
        super(id, name, startDate, endDate);
        this.percentage = percentage;
        this.targetCategory = targetCategory;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getTargetCategory() {
        return targetCategory;
    }

    public void setTargetCategory(String targetCategory) {
        this.targetCategory = targetCategory;
    }
}