package com.gamezone.model;

/**
 * Represents a customer in the GameZone system. A customer inherits common
 * person information and includes an email address.
 */
public class Customer extends Person {

    private String email;

    /**
     * Creates a customer with their basic information and email address.
     *
     * @param id customer's identification
     * @param name customer's name
     * @param phone customer's contact phone number
     * @param email customer's email address
     */
    public Customer(String id, String name, String phone, String email) {
        super(id, name, phone);
        this.email = email;
    }

    /**
     * Gets the customer's email address.
     *
     * @return customer's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the customer's email address.
     *
     * @param email new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
