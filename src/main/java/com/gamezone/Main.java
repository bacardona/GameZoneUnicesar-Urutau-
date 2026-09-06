package com.gamezone;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.ui.ConsoleUI;

import java.util.List;

/**
 * Entry point of the GameZone Unicesar application.
 * Wires together repositories, services, and the console UI,
 * and loads previously stored data before starting the menu.
 */
public class Main {

    /**
     * Starts the application: loads persisted data, builds the
     * dependency graph (repositories -> services -> UI), and
     * launches the main menu.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // --- Persistence layer ---
        ProductRepository productRepository = new ProductRepository(); // 🔶 confirmar nombre con Desarrollador 1
        PersonRepository personRepository = new PersonRepository();   // 🔶 confirmar nombre con Desarrollador 2
        SaleRepository saleRepository = new SaleRepository();

        // --- Initial data load (order matters: products and people first,
        // sales depend on them to resolve IDs) ---
        List<Product> products = productRepository.load();       // 🔶 confirmar firma con Desarrollador 1
        List<Customer> customers = personRepository.loadCustomers(); // 🔶 confirmar firma con Desarrollador 2
        List<Seller> sellers = personRepository.loadSellers();       // 🔶 confirmar firma con Desarrollador 2
        List<Sale> sales = saleRepository.load(customers, sellers, products);

        checkPreloadedSellers(sellers);

        // --- Service layer ---
        ProductService productService = new ProductService(productRepository, products); // 🔶 confirmar constructor
        PersonService personService = new PersonService(personRepository, customers, sellers); // 🔶 confirmar constructor
        SaleService saleService = new SaleService(saleRepository, productService, personService, sales);

        // --- UI layer ---
        ConsoleUI consoleUI = new ConsoleUI(personService, productService, saleService);
        consoleUI.showMainMenu();
    }

    /**
     * Verifies that the system starts with at least three preloaded sellers,
     * as required for the first execution of the application.
     *
     * @param sellers the list of sellers loaded at startup
     */
    private static void checkPreloadedSellers(List<Seller> sellers) {
        if (sellers.size() < 3) {
            System.out.println("WARNING: fewer than 3 preloaded sellers found ("
                    + sellers.size() + "). Please check data/sellers file.");
        }
    }
}