package com.gamezone;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.PromotionRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.WarrantyRepository;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.SaleService;
import com.gamezone.service.WarrantyService;
import com.gamezone.persistence.ReturnRepository;
import com.gamezone.service.ReturnService;
import com.gamezone.ui.ConsoleUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point of the GameZone Unicesar application. Wires together
 * repositories, services, and the console UI, and loads previously stored data
 * before starting the menu.
 */
public class Main {

    /**
     * Starts the application: builds the dependency graph (repositories ->
     * services -> UI) using constructor injection only, loads previously stored
     * data, and launches the main menu.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        PersonRepository personRepository = new PersonRepository();
        PersonService personService = new PersonService(personRepository);

        ProductService productService = new ProductService();

        AccessoryRepository accessoryRepository = new AccessoryRepository();
        AccessoryService accessoryService = new AccessoryService(accessoryRepository);

        PromotionRepository promotionRepository = new PromotionRepository();
        PromotionService promotionService = new PromotionService(promotionRepository);

        SaleRepository saleRepository = new SaleRepository();

        // Load people and sellable items (products and accessories).
        List<Customer> customers = personService.listCustomers();
        List<Seller> sellers = personService.listSellers();

        List<Product> sellableItems = new ArrayList<>(productService.listProducts());
        sellableItems.addAll(accessoryService.listAllAccessories());

        // Sales must be loaded before WarrantyService, which resolves
        // stored sale identifiers through the sale repository.
        List<Sale> sales = saleRepository.load(customers, sellers, sellableItems);

        checkPreloadedSellers(sellers);

        // No circular dependency: WarrantyService depends only on repositories
        // and ProductService, so SaleService can receive it by constructor.
        WarrantyRepository warrantyRepository = new WarrantyRepository();
        WarrantyService warrantyService = new WarrantyService(
                warrantyRepository, saleRepository, productService);

        SaleService saleService = new SaleService(
                saleRepository,
                productService,
                accessoryService,
                promotionService,
                warrantyService,
                personService,
                sales);

        ReturnRepository returnRepository = new ReturnRepository(saleService, productService);
        ReturnService returnService = new ReturnService(returnRepository, saleService, productService);
        
        ConsoleUI consoleUI = new ConsoleUI(
                personService,
                productService,
                accessoryService,
                promotionService,
                warrantyService,
                saleService);

        consoleUI.showMainMenu();
    }

    /**
     * Verifies that the system starts with at least three preloaded sellers, as
     * required for the first execution of the application.
     *
     * @param sellers the list of sellers loaded at startup
     */
    private static void checkPreloadedSellers(List<Seller> sellers) {
        if (sellers.size() < 3) {
            System.out.println("WARNING: fewer than 3 preloaded sellers found ("
                    + sellers.size()
                    + "). Please check data/people.txt file.");
        }
    }
}
