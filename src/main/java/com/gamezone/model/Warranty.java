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
     * Returns the unique identifier of this warranty.
     *
     * @return the warranty id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the product covered by this warranty.
     *
     * @return the covered product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Returns the sale in which the covered product was purchased.
     *
     * @return the associated sale
     */
    public Sale getSale() {
        return sale;
    }

    /**
     * Returns the date the warranty coverage begins.
     *
     * @return the start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the date the warranty coverage ends.
     *
     * @return the end date
     */
    public LocalDate getEndDate() {
        return endDate;
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
    
        /**
     * Checks whether this warranty is active on the given date, meaning the
     * date falls between the start and end dates (inclusive).
     *
     * @param date the date to check
     * @return true if the warranty is active on that date
     */
    public boolean isActive(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Builds a human-readable certificate describing this warranty: type,
     * covered product, associated sale, coverage period, and additional
     * cost. The text is in Spanish since it is user-facing.
     *
     * @return the formatted warranty certificate
     */
    public String generateWarrantyCertificate() {
        StringBuilder certificate = new StringBuilder();
        certificate.append("----- Certificado de Garantía -----\n");
        certificate.append("ID de garantía: ").append(id).append("\n");
        certificate.append("Tipo: ").append(getWarrantyType()).append("\n");
        certificate.append("Producto cubierto: ").append(product.getTitle()).append("\n");
        certificate.append("Venta asociada: ").append(sale.getId()).append("\n");
        certificate.append("Fecha de inicio: ").append(startDate).append("\n");
        certificate.append("Fecha de fin: ").append(endDate).append("\n");
        certificate.append("Costo adicional: $").append(getAdditionalCost()).append("\n");
        certificate.append("------------------------------------");
        return certificate.toString();
    }
}
