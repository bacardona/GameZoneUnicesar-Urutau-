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
        personRepository.save(customer);
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
