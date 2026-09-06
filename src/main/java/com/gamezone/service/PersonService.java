package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Seller;
import com.gamezone.model.Person;
import com.gamezone.persistence.PersonRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Contiene las reglas de negocio relacionadas con las personas.
 */
public class PersonService {

    private PersonRepository personRepository;

    /**
     * Crea un servicio de personas utilizando un repositorio.
     *
     * @param personRepository repositorio utilizado para almacenar las personas
     */
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Registra un nuevo cliente.
     *
     * @param customer cliente que se desea registrar
     */
    public void registerCustomer(Customer customer) {

        validateCustomer(customer);

        for (Person person : personRepository.findAll()) {

            if (person.getID().equals(customer.getID())) {
                throw new IllegalArgumentException("ID already exists.");
            }
        }

        personRepository.save(customer);
    }

    /**
     * Valida los datos de un cliente antes de registrarlo.
     *
     * @param customer cliente cuyos datos se desean validar
     * @throws IllegalArgumentException si alguno de los datos no cumple con las
     * reglas de validación
     */
    private void validateCustomer(Customer customer) {

        if (customer.getID() == null || customer.getID().isBlank()) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }

        if (customer.getName() == null || customer.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }

        if (!customer.getName().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            throw new IllegalArgumentException("Name cannot contain numbers or special characters.");
        }

        if (customer.getPhone() == null || customer.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone cannot be empty.");
        }

        if (!customer.getPhone().matches("\\d+")) {
            throw new IllegalArgumentException("Phone must contain only digits.");
        }

        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        if (!customer.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Email format is invalid.");
        }
    }

    /**
     * Carga las personas almacenadas previamente.
     */
    public void loadPeople() {
        personRepository.load();
    }

    /**
     * Obtiene todos los clientes registrados.
     *
     * @return lista de clientes
     */
    public List<Customer> listCustomers() {

        List<Customer> customers = new ArrayList<>();

        for (Person person : personRepository.findAll()) {

            if (person instanceof Customer) {
                customers.add((Customer) person);
            }
        }

        return customers;
    }

    /**
     * Obtiene todos los vendedores registrados.
     *
     * @return lista de vendedores
     */
    public List<Seller> listSellers() {

        List<Seller> sellers = new ArrayList<>();

        for (Person person : personRepository.findAll()) {

            if (person instanceof Seller) {
                sellers.add((Seller) person);
            }
        }

        return sellers;
    }

}
