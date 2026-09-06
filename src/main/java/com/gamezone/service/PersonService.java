package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Seller;
import com.gamezone.model.Person;
import com.gamezone.persistence.PersonRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the business rules related to people in the GameZone system.
 */
public class PersonService {

    private PersonRepository personRepository;

    /**
     * Creates a people service using a repository.
     *
     * @param personRepository repository used to store people
     */
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Registers a new customer after validating their information and checking
     * for duplicate IDs.
     *
     * @param customer customer to be registered
     */
    public void registerCustomer(Customer customer) {

        // Valida los datos del cliente antes de guardarlo.
        validateCustomer(customer);

        // Comprueba que el ID del cliente no esté registrado.
        for (Person person : personRepository.findAll()) {

            if (person.getID().equals(customer.getID())) {
                throw new IllegalArgumentException("ID already exists.");
            }
        }

        personRepository.save(customer);
    }

    /**
     * Validates the customer's information before registration.
     *
     * @param customer customer whose information will be validated
     * @throws IllegalArgumentException if any customer data is invalid
     */
    private void validateCustomer(Customer customer) {

        // Comprueba que el ID no esté vacío.
        if (customer.getID() == null || customer.getID().isBlank()) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }

        // Comprueba que el nombre no esté vacío.
        if (customer.getName() == null || customer.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }

        // Comprueba que el nombre solamente contenga letras y espacios.
        if (!customer.getName().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            throw new IllegalArgumentException(
                    "Name cannot contain numbers or special characters.");
        }

        // Comprueba que el teléfono no esté vacío.
        if (customer.getPhone() == null || customer.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone cannot be empty.");
        }

        // Comprueba que el teléfono solamente contenga números.
        if (!customer.getPhone().matches("\\d+")) {
            throw new IllegalArgumentException("Phone must contain only digits.");
        }

        // Comprueba que el correo no esté vacío.
        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        // Comprueba que el correo tenga un formato básico válido.
        if (!customer.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Email format is invalid.");
        }
    }

    /**
     * Loads people previously stored in the repository.
     */
    public void loadPeople() {
        personRepository.load();
    }

    /**
     * Gets all registered customers.
     *
     * @return list containing the registered customers
     */
    public List<Customer> listCustomers() {

        List<Customer> customers = new ArrayList<>();

        // Recorre las personas y selecciona únicamente los clientes.
        for (Person person : personRepository.findAll()) {

            if (person instanceof Customer) {
                customers.add((Customer) person);
            }
        }

        return customers;
    }

    /**
     * Gets all registered sellers.
     *
     * @return list containing the registered sellers
     */
    public List<Seller> listSellers() {

        List<Seller> sellers = new ArrayList<>();

        // Recorre las personas y selecciona únicamente los vendedores.
        for (Person person : personRepository.findAll()) {

            if (person instanceof Seller) {
                sellers.add((Seller) person);
            }
        }

        return sellers;
    }
}
