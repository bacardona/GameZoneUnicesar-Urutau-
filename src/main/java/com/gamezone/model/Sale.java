package com.gamezone.model;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Represents a sale transaction made in the store.
 * A sale involves one customer, one seller, and a list of purchased products.
 **/

public class Sale {
    private String id;
    private Date date;
    private Customer customer;
    private Seller seller;
    private List<Product> products;
    
    /**
     * Creates a new Sale.
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
    }
    
    /**
     * Returns the unique identifier of the sale.
     
     * @return the sale id
    **/
    
    public String getId() {
        return id;
        
    /**
     * Returns the date the sale was made.
     
     * @return the sale date
    **/
    
    }
    public Date getDate() {
        return date;
    }
    
    /**
     * Returns the customer who made the purchase.
     
     * @return the customer of this sale
    **/
    
    public Customer getCustomer() {
        return customer;
    }
    
     /**
     * Returns the seller who attended the sale.
     
     * @return the seller of this sale
     **/
    
    public Seller getSeller() {
        return seller;
    }
    
    /**
     * Returns an unmodifiable view of the products included in this sale,
     * preventing external code from altering the sale's contents.
     
     * @return the list of products purchased in this sale
     **/
    
    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }
    
    /**
     * Calculates the total price of the sale by summing the price of each product.
     * If the same product was bought more than once, it must appear repeated
     * in the products list.
     *
     * @return the total amount of the sale
     **/
    
    public double calculateTotal() {
        double total = 0.0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }
}