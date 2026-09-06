package com.gamezone.persistence;

import com.gamezone.model.Person;
import com.gamezone.model.Customer;
import com.gamezone.model.Seller;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Gestiona la persistencia de las personas del sistema GameZone.
 */
public class PersonRepository {

    private List<Person> people;
    private final String filePath = "data/people.txt";

    /**
     * Crea un repositorio vacío de personas.
     */
    public PersonRepository() {
        people = new ArrayList<>();

        File folder = new File("data");

        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        load();
    }

    /**
     * Guarda una persona en el repositorio.
     *
     * @param person persona que se desea guardar
     */
    public void save(Person person) {
        people.add(person);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

            if (person instanceof Customer) {
                Customer customer = (Customer) person;

                writer.write("CUSTOMER|" + customer.getID() + "|"
                        + customer.getName() + "|"
                        + customer.getPhone() + "|"
                        + customer.getEmail());

            } else if (person instanceof Seller) {
                Seller seller = (Seller) person;

                writer.write("SELLER|" + seller.getID() + "|"
                        + seller.getName() + "|"
                        + seller.getPhone() + "|"
                        + seller.getEmployeeCode() + "|"
                        + seller.getShift());
            }

            writer.newLine();

        } catch (IOException e) {
            System.out.println("Error al guardar la persona: " + e.getMessage());
        }
    }
    
    /**
 * Carga las personas almacenadas en el archivo.
 */
public void load() {

    people.clear();

    File file = new File(filePath);

    if (!file.exists()) {
        return;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

        String line;

        while ((line = reader.readLine()) != null) {

            String[] data = line.split("\\|");

            if (data[0].equals("CUSTOMER")) {

                Customer customer = new Customer(
                        data[1],
                        data[2],
                        data[3],
                        data[4]
                );

                people.add(customer);

            } else if (data[0].equals("SELLER")) {

                Seller seller = new Seller(
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        data[5]
                );

                people.add(seller);
            }
        }

    } catch (IOException e) {
        System.out.println("Error al cargar las personas: " + e.getMessage());
    }
}
    
    

    /**
     * Obtiene todas las personas almacenadas.
     *
     * @return lista de personas
     */
    public List<Person> findAll() {
        return people;
    }
}
