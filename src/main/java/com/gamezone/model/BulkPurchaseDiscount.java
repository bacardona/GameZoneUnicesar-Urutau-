package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a promotion that applies a percentage discount over the total
 * of a sale, but only when the sale includes at least a minimum quantity
 * of products.
 */
public class BulkPurchaseDiscount extends Promotion {

    // Cantidad mínima de productos requerida para aplicar el descuento
    private int minQuantity;
    // Porcentaje de descuento (entre 0 y 100)
    private double percentage;

    /**
     * Creates a new BulkPurchaseDiscount.
     *
     * @param id          unique identifier
     * @param name        display name of the promotion
     * @param startDate   date from which the promotion becomes valid
     * @param endDate     date until which the promotion remains valid
     * @param minQuantity minimum number of products the sale must include
     * @param percentage  discount percentage, between 0 and 100
     */
    public BulkPurchaseDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                                 int minQuantity, double percentage) {
        super(id, name, startDate, endDate);
        this.minQuantity = minQuantity;
        this.percentage = percentage;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}