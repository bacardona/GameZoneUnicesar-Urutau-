package com.gamezone.model;

import java.time.LocalDate;

/**
 * Abstract base class representing a warranty coverage associated with a
 * product sold in a specific sale. The end date is calculated automatically
 * in the constructor by invoking the abstract duration method, so every
 * concrete subclass only needs to declare how many months it lasts.
 */
public abstract class Warranty {

    private String id;
    private Product product;
    private Sale sale;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Creates a new Warranty. The end date is computed automatically as
     * {@code startDate + getDurationInMonths()}, so this constructor relies
     * on the concrete subclass's {@link #getDurationInMonths()}
     * implementation, which must not depend on any subclass-specific field
     * (it must return a fixed value).
     *
     * @param id        unique identifier of the warranty
     * @param product   the product this warranty covers
     * @param sale      the sale in which the product was purchased
     * @param startDate the date the warranty coverage begins (the sale date)
     */
    public Warranty(String id, Product product, Sale sale, LocalDate startDate) {
        this.id = id;
        this.product = product;
        this.sale = sale;
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(getDurationInMonths());
    }

    /**
     * Returns the duration of this warranty type, in months. Implemented by
     * each concrete subclass with a fixed value (6 for basic, 12 for
     * extended).
     *
     * @return the warranty duration in months
     */
    public abstract int getDurationInMonths();

    /**
     * Returns the display name of this warranty type, in Spanish, since it
     * is shown to the end user.
     *
     * @return the warranty type name
     */
    public abstract String getWarrantyType();

    /**
     * Returns the additional cost, in pesos, that this warranty adds to the
     * sale total. Basic warranties are free; extended warranties add a
     * percentage of the covered product's price.
     *
     * @return the additional cost of this warranty
     */
    public abstract double getAdditionalCost();
}
