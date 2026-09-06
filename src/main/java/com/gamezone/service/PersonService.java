package com.gamezone.service;

import com.gamezone.model.Person;
import com.gamezone.persistence.PersonRepository;
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
     * Registra una nueva persona.
     *
     * @param person persona que se desea registrar
     */
    public void registerPerson(Person person) {
        personRepository.save(person);
    }
    
    /**
 * Carga las personas almacenadas previamente.
 */
public void loadPeople() {
    personRepository.load();
}

    /**
     * Obtiene todas las personas registradas.
     *
     * @return lista de personas registradas
     */
    public List<Person> listPeople() {
        return personRepository.findAll();
    }
}
