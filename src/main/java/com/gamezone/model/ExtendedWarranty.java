package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents the extended warranty optionally requested by the customer at
 * sale time. It covers manufacturing defects and accidental damage, lasts
 * 12 months from the sale date, and costs an additional 10% of the covered
 * product's price.
 */
public class ExtendedWarranty extends Warranty {

    // Porcentaje de costo adicional sobre el precio del producto cubierto
    private static final double ADDITIONAL_COST_PERCENTAGE = 0.10;

    /**
     * Creates a new ExtendedWarranty.
     *
     * @param id        unique identifier of the warranty
     * @param product   the console this warranty covers
     * @param sale      the sale in which the console was purchased
     * @param startDate the date the warranty coverage begins (the sale date)
     */
    public ExtendedWarranty(String id, Product product, Sale sale, LocalDate startDate) {
        super(id, product, sale, startDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDurationInMonths() {
        return 12;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWarrantyType() {
        return "Garantía Extendida";
    }

    /**
     * {@inheritDoc}
     * The extended warranty costs 10% of the covered product's price.
     */
    @Override
    public double getAdditionalCost() {
        return getProduct().getPrice() * ADDITIONAL_COST_PERCENTAGE;
    }
}