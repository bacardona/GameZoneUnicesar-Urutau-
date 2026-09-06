package com.gamezone.ui;

import com.gamezone.model.Customer;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

import java.util.Scanner;

/**
 * Console-based user interface for the GameZone system.
 * Displays the main menu and delegates each operation to the
 * corresponding service (PersonService, ProductService, SaleService).
 **/

public class ConsoleUI {

    private final Scanner scanner;
    private final PersonService personService;
    private final ProductService productService;
    private final SaleService saleService;

    /**
     * Creates a new ConsoleUI.
     
     * @param personService service used to manage customers and sellers
     * @param productService service used to manage products
     * @param saleService service used to manage sales
     */
    
    public ConsoleUI(PersonService personService, ProductService productService, SaleService saleService) {
        this.scanner = new Scanner(System.in);
        this.personService = personService;
        this.productService = productService;
        this.saleService = saleService;
    }

    /**
     * Displays the main menu in a loop until the user chooses to exit.
     **/
    
    public void showMainMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n===== GameZone Unicesar =====");
            System.out.println("--- Products ---");
            System.out.println("1. Register a new video game");
            System.out.println("2. Register a new console");
            System.out.println("3. List inventory");
            System.out.println("--- People ---");
            System.out.println("4. Register a new customer");
            System.out.println("5. List customers");
            System.out.println("6. List sellers");
            System.out.println("--- Sales ---");
            System.out.println("7. Register a new sale");
            System.out.println("8. View full sales history");
            System.out.println("9. View sales history by customer");
            System.out.println("10. View sales history by seller");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1", "2", "3" -> showProductMenu(option);
                case "4", "5", "6" -> showPersonMenu(option);
                case "7", "8", "9", "10" -> showSaleMenu(option);
                case "0" -> {
                    System.out.println("Closing GameZone Unicesar. See you soon!");
                    running = false;
                }
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }
    
    /**
     * Displays the product submenu and executes the selected operation.
     *
     * @param option the menu option selected by the user
    **/
    
    private void showProductMenu(String option) {
        switch (option) {
            case "1" -> registerVideoGame();
            case "2" -> registerConsole();
            case "3" -> listProducts();
        }
    }

    /**
     * Prompts the user for video game data and registers it through ProductService.
    **/
    
    private void registerVideoGame() {
        System.out.println("\n--- Register Video Game ---");
        System.out.print("ID: ");
        String id = scanner.nextLine();
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        System.out.print("Platform: ");
        String platform = scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();
        System.out.print("Age rating: ");
        String ageRating = scanner.nextLine();

        productService.registerVideoGame(id, title, price, quantity, platform, genre, ageRating);
        System.out.println("Video game registered successfully.");
    }

    /**
     * Prompts the user for console data and registers it through ProductService.
    **/
    
    private void registerConsole() {
        System.out.println("\n--- Register Console ---");
        System.out.print("ID: ");
        String id = scanner.nextLine();
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        System.out.print("Brand: ");
        String brand = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();
        System.out.print("Generation: ");
        String generation = scanner.nextLine();

        productService.registerConsole(id, title, price, quantity, brand, model, generation);
        System.out.println("Console registered successfully.");
    }

    /**
     * Prints the description of every product currently in inventory.
    **/
    
    private void listProducts() {
        System.out.println("\n--- Product Inventory ---");
        productService.listProducts().forEach(p -> System.out.println(p.getDescription()));
    }

    /**
     * Displays the person submenu and executes the selected operation.
     *
     * @param option the menu option selected by the user
    **/
    
    private void showPersonMenu(String option) {
        switch (option) {
            case "4" -> registerCustomer();
            case "5" -> listCustomers();
            case "6" -> listSellers();
        }
    }

    /**
     * Prompts the user for customer data, builds a Customer object,
     * and registers it through PersonService.
    **/
    
    private void registerCustomer() {
    System.out.println("\n--- Register Customer ---");
    System.out.print("ID: ");
    String id = scanner.nextLine();
    System.out.print("Name: ");
    String name = scanner.nextLine();
    System.out.print("Phone: ");
    String phone = scanner.nextLine();
    System.out.print("Email: ");
    String email = scanner.nextLine();

    Customer customer = new Customer(id, name, phone, email);
    personService.registerCustomer(customer);
    System.out.println("Customer registered successfully.");
    }

    /**
     * Prints the id and name of every registered customer.
    **/
    
    private void listCustomers() {
        System.out.println("\n--- Customers ---");
        personService.listCustomers().forEach(c -> System.out.println(c.getID() + " - " + c.getName()));
    }

    /**
     * Prints the id and name of every registered seller.
    **/
    
    private void listSellers() {
        System.out.println("\n--- Sellers ---");
        personService.listSellers().forEach(s -> System.out.println(s.getID() + " - " + s.getName()));
    }

    /**
     * Displays the sale submenu and executes the selected operation.
     *
     * @param option the menu option selected by the user
    **/
    
    private void showSaleMenu(String option) {
        switch (option) {
            case "7" -> registerSale();
            case "8" -> viewFullHistory();
            case "9" -> viewHistoryByCustomer();
            case "10" -> viewHistoryBySeller();
        }
    }

    /**
     * Prompts the user for a customer id, a seller id, and one or more
     * product ids, then attempts to register the sale through SaleService,
     * printing a friendly message if any business rule is violated.
    **/
    
    private void registerSale() {
        System.out.println("\n--- Register Sale ---");
        System.out.print("Customer ID: ");
        String customerId = scanner.nextLine();
        System.out.print("Seller ID: ");
        String sellerId = scanner.nextLine();

        java.util.List<String> productIds = new java.util.ArrayList<>();
        boolean addingProducts = true;
        while (addingProducts) {
            System.out.print("Product ID (empty to finish): ");
            String productId = scanner.nextLine();
            if (productId.isBlank()) {
                addingProducts = false;
            } else {
                productIds.add(productId);
            }
        }

        try {
            var sale = saleService.registerSale(customerId, sellerId, productIds);
            System.out.println("Sale registered. Total: " + sale.calculateTotal());
        } catch (IllegalArgumentException e) {
            System.out.println("Could not register sale: " + e.getMessage());
        }
    }

    /**
     * Prints the complete sales history.
    **/
    
    private void viewFullHistory() {
        System.out.println("\n--- Full Sales History ---");
        saleService.getAllSales().forEach(this::printSale);
    }

    /**
     * Prompts for a customer id and prints that customer's purchase history.
    **/
    
    private void viewHistoryByCustomer() {
        System.out.print("Customer ID: ");
        String customerId = scanner.nextLine();
        saleService.getSalesByCustomer(customerId).forEach(this::printSale);
    }

    /**
     * Prompts for a seller id and prints the sales that seller attended.
    **/
    
    private void viewHistoryBySeller() {
        System.out.print("Seller ID: ");
        String sellerId = scanner.nextLine();
        saleService.getSalesBySeller(sellerId).forEach(this::printSale);
    }

    /**
     * Prints a single sale in a readable, one-line format, used by all
     * three history views to avoid repeating the same formatting logic.
     *
     * @param sale the sale to print
    **/
    
    private void printSale(com.gamezone.model.Sale sale) {
        System.out.println("Sale " + sale.getId() + " | Customer: " + sale.getCustomer().getName()
                + " | Seller: " + sale.getSeller().getName() + " | Total: " + sale.calculateTotal());
    }
}