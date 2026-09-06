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
 * Handles the persistence of people in the GameZone system. It stores and
 * retrieves customers and sellers from a text file.
 */
public class PersonRepository {

    private List<Person> people;
    private final String filePath = "data/people.txt";

    /**
     * Creates an empty people repository and loads previously stored data from
     * the file.
     */
    public PersonRepository() {
        people = new ArrayList<>();

        // Crea la carpeta data si todavía no existe.
        File folder = new File("data");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        load();
    }

    /**
     * Saves a person to the repository and appends their information to the
     * data file.
     *
     * @param person person to be saved
     */
    public void save(Person person) {
        people.add(person);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

            // Guarda la información dependiendo del tipo de persona.
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
            System.out.println("Error saving person: " + e.getMessage());
        }
    }

    /**
     * Loads people previously stored in the data file. Each record is converted
     * into the corresponding Customer or Seller object.
     */
    public void load() {
        people.clear();

        File file = new File(filePath);

        // Si el archivo no existe, no hay datos que cargar.
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\\|");

                // Reconstruye un Customer usando los datos almacenados.
                if (data[0].equals("CUSTOMER")) {

                    Customer customer = new Customer(
                            data[1],
                            data[2],
                            data[3],
                            data[4]
                    );

                    people.add(customer);

                    // Reconstruye un Seller usando los datos almacenados.
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
            System.out.println("Error loading people: " + e.getMessage());
        }
    }

    /**
     * Gets all people stored in the repository.
     *
     * @return list containing all stored people
     */
    public List<Person> findAll() {
        return people;
    }
}
