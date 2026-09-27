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
}