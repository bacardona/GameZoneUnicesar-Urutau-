package com.gamezone;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.ui.ConsoleUI;
import java.util.List;

public class Main {
    public static void main(String[] args) {
// --- Persistence + Service layers (Product and Person load themselves) --- //
        PersonRepository personRepository = new PersonRepository(); 
        PersonService personService = new PersonService(personRepository);
        ProductService productService = new ProductService(); 
        SaleRepository saleRepository = new SaleRepository();

// --- Obtain pre-filtered lists through the services --- // 
        List<Customer> customers = personService.listCustomers();
        List<Seller> sellers = personService.listSellers();
        List<Product> products = productService.listProducts();
        List<Sale> sales = saleRepository.load(customers, sellers, products);

        checkPreloadedSellers(sellers);

// --- Sale service ---
        SaleService saleService = new SaleService(saleRepository, productService, personService, sales);

        // --- UI layer ---
        ConsoleUI consoleUI = new ConsoleUI(personService, productService, saleService);
        consoleUI.showMainMenu();
    }

    private static void checkPreloadedSellers(List<Seller> sellers) {
        if (sellers.size() < 3) {
            System.out.println("WARNING: fewer than 3 preloaded sellers found ("
                    + sellers.size() + "). Please check data/people.txt file.");
        }
    }
}