package com.gamezone.model;

import java.time.LocalDate;

/**
 * Abstract base class representing a promotional discount campaign offered
 * by GameZone Unicesar. Each concrete subclass implements its own discount
 * calculation strategy over a given sale.
 */
public abstract class Promotion {

    // Atributos comunes a todas las promociones
    private String id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Creates a new Promotion with its common attributes.
     *
     * @param id        unique identifier of the promotion
     * @param name      display name of the promotion
     * @param startDate date from which the promotion becomes valid
     * @param endDate   date until which the promotion remains valid
     */
    public Promotion(String id, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Checks whether this promotion is valid on the given date, meaning the
     * date falls within the promotion's start and end dates (inclusive).
     *
     * @param date the date to check
     * @return true if the promotion is active on that date
     */
    public boolean isActive(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Calculates the discount amount, in pesos, that this promotion would
     * grant to the given sale. Each subclass implements its own rule; the
     * rest of the system relies on this contract without needing to know
     * the concrete promotion type (polymorphism).
     *
     * @param sale the sale to evaluate
     * @return the discount amount in pesos
     */
    public abstract double calculateDiscount(Sale sale);
}