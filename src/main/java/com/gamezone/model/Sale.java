package com.gamezone.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Sale {

    private String id;
    private Date date;
    private Customer customer;
    private Seller seller;
    private List<SaleItem> items;

    public Sale(String id, Date date, Customer customer, Seller seller, List<SaleItem> items) {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.seller = seller;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Seller getSeller() {
        return seller;
    }

    public List<SaleItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double calculateTotal() {
        double total = 0.0;
        for (SaleItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }
}