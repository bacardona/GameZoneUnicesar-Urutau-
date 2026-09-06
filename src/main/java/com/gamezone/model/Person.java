package com.gamezone.model;

/**
 * Representa la información común de todas las personas
 * que participan en el sistema GameZone.
 */
public abstract class Person {

    private String id;
    private String name;
    private String phone;

    /**
     * Crea una persona con su información básica.
     *
     * @param id identificación de la persona
     * @param name nombre de la persona
     * @param phone teléfono de contacto de la persona
     */
    public Person(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    /**
     * Obtiene la identificación de la persona.
     *
     * @return identificación de la persona
     */
    public String getID() {
        return id;
    }

    /**
     * Modifica la identificación de la persona.
     *
     * @param id nueva identificación
     */
    public void setID(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la persona.
     *
     * @return nombre de la persona
     */
    public String getName() {
        return name;
    }

    /**
     * Modifica el nombre de la persona.
     *
     * @param name nuevo nombre
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el teléfono de la persona.
     *
     * @return teléfono de contacto
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Modifica el teléfono de la persona.
     *
     * @param phone nuevo teléfono de contacto
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}