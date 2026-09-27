package com.gamezone.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a return (full or partial) of one or more products originally
 * purchased in a specific sale. A return is associated with its original
 * sale by reference: the sale is not modified or consumed by the return,
 * it is only read to validate and to build the receipt.
 */
public class Return {

    private String id;
    private LocalDate date;
    private Sale originalSale;
    private List<Product> returnedProducts;
    private String reason;
    private double refundAmount;

    /**
     * Creates a new Return. The refund amount starts at zero and must be
     * computed explicitly through {@link #calculateRefundAmount()}.
     *
     * @param id               unique identifier of the return
     * @param date             date the return was registered
     * @param originalSale     the sale this return references
     * @param returnedProducts the specific products being returned (a subset,
     *                         or all, of the products in the original sale)
     * @param reason           reason given for the return
     */
    public Return(String id, LocalDate date, Sale originalSale,
                  List<Product> returnedProducts, String reason) {
        this.id = id;
        this.date = date;
        this.originalSale = originalSale;
        this.returnedProducts = returnedProducts;
        this.reason = reason;
        this.refundAmount = 0.0;
    }

    /**
     * Returns the unique identifier of this return.
     *
     * @return the return id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the date this return was registered.
     *
     * @return the return date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the original sale this return references.
     *
     * @return the original sale
     */
    public Sale getOriginalSale() {
        return originalSale;
    }

    /**
     * Returns an unmodifiable view of the products being returned.
     *
     * @return the list of returned products
     */
    public List<Product> getReturnedProducts() {
        return Collections.unmodifiableList(returnedProducts);
    }

    /**
     * Returns the reason given for this return.
     *
     * @return the return reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Returns the refund amount calculated for this return.
     *
     * @return the refund amount, or 0.0 if it has not been calculated yet
     */
    public double getRefundAmount() {
        return refundAmount;
    }
}