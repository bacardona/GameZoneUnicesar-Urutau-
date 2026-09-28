package com.gamezone.model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Represents a sale transaction made in the store.
 * A sale involves one customer, one seller, and a list of purchased products.
 * It also records the promotion applied to it (if any) and the discount granted.
 **/

public class Sale {
    private String id;
    private Date date;
    private Customer customer;
    private Seller seller;
    private List<Product> products;
    private String appliedPromotionName;
    private double discountAmount;

    /**
     * Creates a new Sale with no promotion applied.
     * @param id the unique identifier of the sale
     * @param date the date the sale was made
     * @param customer the customer who made the purchase
     * @param seller the seller who attended the sale
     * @param products the list of products included in the sale
    **/

    public Sale(String id, Date date, Customer customer, Seller seller, List<Product> products) {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.seller = seller;
        this.products = products;
        this.appliedPromotionName = null;
        this.discountAmount = 0.0;
    }

    /**
     * Returns the unique identifier of the sale.
     *
     * @return the sale id
    **/

    public String getId() {
        return id;
    }

    /**
     * Returns the date the sale was made.
     *
     * @return the sale date
    **/

    public Date getDate() {
        return date;
    }

    /**
     * Returns the customer who made the purchase.
     *
     * @return the customer of this sale
    **/

    public Customer getCustomer() {
        return customer;
    }

     /**
     * Returns the seller who attended the sale.
     *
     * @return the seller of this sale
     **/

    public Seller getSeller() {
        return seller;
    }

    /**
     * Returns an unmodifiable view of the products included in this sale,
     * preventing external code from altering the sale's contents.
     *
     * @return the list of products purchased in this sale
     **/

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    /**
     * Returns the name of the promotion applied to this sale.
     *
     * @return the promotion name, or null if no promotion was applied
     **/

    public String getAppliedPromotionName() {
        return appliedPromotionName;
    }

    /**
     * Sets the name of the promotion applied to this sale.
     *
     * @param appliedPromotionName the promotion name, or null if none applies
     **/

    public void setAppliedPromotionName(String appliedPromotionName) {
        this.appliedPromotionName = appliedPromotionName;
    }

    /**
     * Returns the discount granted to this sale, in pesos.
     *
     * @return the discount amount (0 if no promotion was applied)
     **/

    public double getDiscountAmount() {
        return discountAmount;
    }

    /**
     * Sets the discount granted to this sale, in pesos.
     *
     * @param discountAmount the discount amount, which cannot be negative
     * @throws IllegalArgumentException if the amount is negative
     **/

    public void setDiscountAmount(double discountAmount) {
        if (discountAmount < 0) {
            throw new IllegalArgumentException("Discount amount cannot be negative.");
        }
        this.discountAmount = discountAmount;
    }

    /**
     * Calculates the subtotal of the sale by summing the price of each product,
     * BEFORE any discount. If the same product was bought more than once, it
     * must appear repeated in the products list. Promotions rely on this value
     * to compute their discount, so it never includes the discount itself.
     *
     * @return the subtotal of the sale
     **/


    //Aca se calcula el subtotal chicos (sin descuento)
    public double calculateTotal() {
        double total = 0.0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }

    /**
     * Calculates the final amount to pay: the subtotal minus the discount
     * granted by the applied promotion.
     *
     * @return the final total of the sale
     **/

    public double calculateFinalTotal() {
        return calculateTotal() - discountAmount;
    }

    /**
     * Builds a printable receipt showing the sale's items, the subtotal,
     * the discount (with the name of the applied promotion) and the final total.
     *
     * @return the receipt as text
     **/

    public String generateReceipt() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringBuilder receipt = new StringBuilder();

        receipt.append("========== RECEIPT ==========\n");
        receipt.append("Sale: ").append(id).append("\n");
        receipt.append("Date: ").append(dateFormat.format(date)).append("\n");
        receipt.append("Customer: ").append(customer.getName()).append("\n");
        receipt.append("Seller: ").append(seller.getName()).append("\n");
        receipt.append("Items:\n");
        for (Product product : products) {
            receipt.append(String.format(Locale.US, "  - %s: $%.2f%n", product.getTitle(), product.getPrice()));
        }
        receipt.append("-----------------------------\n");
        receipt.append(String.format(Locale.US, "Subtotal: $%.2f%n", calculateTotal()));
        if (appliedPromotionName != null && discountAmount > 0) {
            receipt.append(String.format(Locale.US, "Discount (%s): -$%.2f%n", appliedPromotionName, discountAmount));
        } else {
            receipt.append("Discount: none\n");
        }
        receipt.append(String.format(Locale.US, "TOTAL: $%.2f%n", calculateFinalTotal()));
        receipt.append("=============================");

        return receipt.toString();
    }

    /**
     * Checks whether this sale can still be returned, meaning today's date
     * falls within the 30 calendar days following the sale's date
     * (inclusive). Uses {@link ChronoUnit#DAYS} to compute the difference
     * between the sale date and today, after converting the legacy
     * {@code java.util.Date} into a {@code LocalDate}.
     *
     * @return true if the sale is still within the 30-day return window
     */
    public boolean canBeReturned() {
        LocalDate saleDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long daysSinceSale = ChronoUnit.DAYS.between(saleDate, LocalDate.now());
        return daysSinceSale >= 0 && daysSinceSale <= 30;
    }
}