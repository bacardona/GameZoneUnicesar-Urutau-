package com.gamezone.model;

import java.time.LocalDate;
import java.util.Collections;
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

    public String getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Sale getOriginalSale() {
        return originalSale;
    }

    public List<Product> getReturnedProducts() {
        return Collections.unmodifiableList(returnedProducts);
    }

    public String getReason() {
        return reason;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    /**
     * Calculates the refund amount by summing the price of every returned
     * product, stores the result in the refundAmount attribute, and
     * returns it.
     *
     * @return the calculated refund amount
     */
    public double calculateRefundAmount() {
        double total = 0.0;
        for (Product product : returnedProducts) {
            total += product.getPrice();
        }
        this.refundAmount = total;
        return total;
    }

    /**
     * Builds a human-readable receipt describing this return: identifier,
     * date, reference to the original sale, returned products with their
     * prices, reason, and refunded amount. The receipt text is in Spanish
     * since it is user-facing.
     *
     * @return the formatted return receipt
     */
    public String generateReturnReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("----- Recibo de Devolución -----\n");
        receipt.append("ID de devolución: ").append(id).append("\n");
        receipt.append("Fecha: ").append(date).append("\n");
        receipt.append("Venta original: ").append(originalSale.getId()).append("\n");
        receipt.append("Productos devueltos:\n");
        for (Product product : returnedProducts) {
            receipt.append("  - ").append(product.getTitle())
                    .append(" - $").append(product.getPrice()).append("\n");
        }
        receipt.append("Motivo: ").append(reason).append("\n");
        receipt.append("Monto reembolsado: $").append(refundAmount).append("\n");
        receipt.append("---------------------------------");
        return receipt.toString();
    }
}