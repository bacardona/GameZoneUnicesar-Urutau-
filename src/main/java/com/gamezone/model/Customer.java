package com.gamezone.model;

/**
 * Representa un cliente de GameZone.
 */
public class Customer extends Person {

    private String email;

    /**
     * Crea un cliente con su información básica y correo electrónico.
     *
     * @param id identificación del cliente
     * @param name nombre del cliente
     * @param phone teléfono de contacto del cliente
     * @param email correo electrónico del cliente
     */
    public Customer(String id, String name, String phone, String email) {
        super(id, name, phone);
        this.email = email;
    }

    /**
     * Obtiene el correo electrónico del cliente.
     *
     * @return correo electrónico del cliente
     */
    public String getEmail() {
        return email;
    }

    /**
     * Modifica el correo electrónico del cliente.
     *
     * @param email nuevo correo electrónico
     */
    public void setEmail(String email) {
        this.email = email;
    }
}