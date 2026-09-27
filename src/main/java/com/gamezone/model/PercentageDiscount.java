package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a promotion that applies a flat percentage discount over the
 * total of a sale, regardless of which products it contains.
 */
public class PercentageDiscount extends Promotion {

    // Porcentaje de descuento (entre 0 y 100)
    private double percentage;

    /**
     * Creates a new PercentageDiscount.
     *
     * @param id         unique identifier
     * @param name       display name of the promotion
     * @param startDate  date from which the promotion becomes valid
     * @param endDate    date until which the promotion remains valid
     * @param percentage discount percentage, between 0 and 100
     */
    public PercentageDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                               double percentage) {
        super(id, name, startDate, endDate);
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    /**
     * {@inheritDoc}
     * Applies the configured percentage over the full total of the sale.
     */
    @Override
    public double calculateDiscount(Sale sale) {
        return sale.calculateTotal() * (percentage / 100.0);
    }
}