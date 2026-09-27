package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents the basic warranty automatically granted to every console sold.
 * It covers only manufacturing defects, lasts 6 months from the sale date,
 * and has no additional cost for the customer.
 */
public class BasicWarranty extends Warranty {

    /**
     * Creates a new BasicWarranty.
     *
     * @param id        unique identifier of the warranty
     * @param product   the console this warranty covers
     * @param sale      the sale in which the console was purchased
     * @param startDate the date the warranty coverage begins (the sale date)
     */
    public BasicWarranty(String id, Product product, Sale sale, LocalDate startDate) {
        super(id, product, sale, startDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDurationInMonths() {
        return 6;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWarrantyType() {
        return "Garantía Básica";
    }

    /**
     * {@inheritDoc}
     * The basic warranty is generated automatically and has no cost for
     * the customer.
     */
    @Override
    public double getAdditionalCost() {
        return 0.0;
    }
}