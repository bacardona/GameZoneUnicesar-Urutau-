package com.gamezone.service;

import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.model.Customer;
import com.gamezone.model.Seller;
import com.gamezone.model.Person;
import com.gamezone.persistence.PersonRepository;
import com.gamezone.validation.PersonValidator;
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
     * for duplicate IDs. The checks are delegated to PersonValidator.
     *
     * @param customer customer to be registered
     * @throws InvalidDataException if the customer or any of its data is invalid
     * @throws com.gamezone.exceptions.BusinessRuleException if another person
     *         already uses the same ID
     */
    public void registerCustomer(Customer customer) {

        if (customer == null) {
            throw new InvalidDataException("cliente", "es obligatorio");
        }

        PersonValidator.validatePersonData(
                customer.getID(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone());

        PersonValidator.validateUniquePerson(
                personRepository.findAll(),
                customer.getID());

        personRepository.save(customer);
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
