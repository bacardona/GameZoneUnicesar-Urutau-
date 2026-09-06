package com.gamezone.model;

/**
 * Represents the common information shared by all people in the GameZone
 * system.
 */
public abstract class Person {

    private String id;
    private String name;
    private String phone;

    /**
     * Creates a person with their basic information.
     *
     * @param id person's identification
     * @param name person's name
     * @param phone person's contact phone number
     */
    public Person(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    /**
     * Gets the person's identification.
     *
     * @return person's identification
     */
    public String getID() {
        return id;
    }

    /**
     * Updates the person's identification.
     *
     * @param id new identification
     */
    public void setID(String id) {
        this.id = id;
    }

    /**
     * Gets the person's name.
     *
     * @return person's name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the person's name.
     *
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the person's phone number.
     *
     * @return person's contact phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Updates the person's phone number.
     *
     * @param phone new contact phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
