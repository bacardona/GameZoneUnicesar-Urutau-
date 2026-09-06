package com.gamezone.ui;

import com.gamezone.model.Customer;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Console-based user interface for the GameZone system.
 * Displays the main menu and delegates each operation to the
 * corresponding service (PersonService, ProductService, SaleService).
 * Also validates raw user input before it is passed to any service.
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
    **/
    
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

//VALIDACIONES DE AYUDA
    
    /**
     * Repeatedly prompts the user until the entered value satisfies the given
     * validator, printing the error message on every invalid attempt.
     
     * @param prompt the message shown when asking for input
     * @param validator the rule the input must satisfy
     * @param errorMessage the message shown when the input is invalid
     * @return the first value entered that passes the validator
    **/
    
    private String readValidated(String prompt, Predicate<String> validator, String errorMessage) {
        System.out.print(prompt);
        String value = scanner.nextLine();
        while (!validator.test(value)) {
            System.out.print(errorMessage);
            value = scanner.nextLine();
        }
        return value;
    }

    /**
     * Checks whether a value contains digits only (used for IDs, phone, and employee codes).
    **/
    
    private boolean isNumeric(String value) {
        return value.matches("\\d+");
    }

    /**
     * Checks whether a value is exactly 10 digits long.
    **/
    
    private boolean isValidPhone(String value) {
        return value.matches("\\d{10}");
    }

    /**
     * Checks whether a value contains only letters and spaces, with at least one letter.
    **/
    
    private boolean isValidName(String value) {
        return value.matches("^(?=.*[A-Za-zÁÉÍÓÚáéíóúÑñ])[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$");
    }

    /**
     * Checks whether a value contains only letters, numbers, and spaces,
     * with at least one non-space character. Used for titles and for the
     * descriptive fields shared by video games and consoles.
    **/
    
    private boolean isAlphanumeric(String value) {
        return value.matches("^(?=.*[A-Za-z0-9])[A-Za-z0-9 ]+$");
    }

    /**
     * Checks whether a value contains an "@" symbol.
    **/
    
    private boolean isValidEmail(String value) {
        return value.contains("@");
    }

    /**
     * Reads a price from the console, repeating the prompt until the value
     * is a valid decimal number strictly greater than zero.
    **/
    
    private double readValidPrice() {
        while (true) {
            System.out.print("Price: ");
            String input = scanner.nextLine();
            try {
                double price = Double.parseDouble(input);
                if (price > 0) {
                    return price;
                }
                System.out.println("Price must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Please enter a valid number.");
            }
        }
    }

    /**
     * Reads a quantity from the console, repeating the prompt until the value
     * is a valid whole number strictly greater than zero.
    **/
    
    private int readValidQuantity() {
        while (true) {
            System.out.print("Quantity: ");
            String input = scanner.nextLine();
            try {
                int quantity = Integer.parseInt(input);
                if (quantity > 0) {
                    return quantity;
                }
                System.out.println("Quantity must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Please enter a whole number.");
            }
        }
    }

    //PRODUCTOS

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
     * Prompts the user for video game data, validating each field,
     * and registers it through ProductService.
    **/
    
    private void registerVideoGame() {
        System.out.println("\n--- Register Video Game ---");
        String id = readValidated("ID: ", this::isNumeric, "Invalid ID. Numbers only: ");
        String title = readValidated("Title: ", this::isAlphanumeric, "Invalid title. Letters and numbers only: ");
        double price = readValidPrice();
        int quantity = readValidQuantity();
        String platform = readValidated("Platform: ", this::isAlphanumeric, "Invalid platform. Letters and numbers only: ");
        String genre = readValidated("Genre: ", this::isAlphanumeric, "Invalid genre. Letters and numbers only: ");
        String ageRating = readValidated("Age rating: ", this::isAlphanumeric, "Invalid age rating. Letters and numbers only: ");

        productService.registerVideoGame(id, title, price, quantity, platform, genre, ageRating);
        System.out.println("Video game registered successfully.");
    }

    /**
     * Prompts the user for console data, validating each field,
     * and registers it through ProductService.
    **/
    
    private void registerConsole() {
        System.out.println("\n--- Register Console ---");
        String id = readValidated("ID: ", this::isNumeric, "Invalid ID. Numbers only: ");
        String title = readValidated("Title: ", this::isAlphanumeric, "Invalid title. Letters and numbers only: ");
        double price = readValidPrice();
        int quantity = readValidQuantity();
        String brand = readValidated("Brand: ", this::isAlphanumeric, "Invalid brand. Letters and numbers only: ");
        String model = readValidated("Model: ", this::isAlphanumeric, "Invalid model. Letters and numbers only: ");
        String generation = readValidated("Generation: ", this::isAlphanumeric, "Invalid generation. Letters and numbers only: ");

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

// GENTE/PERSONAS
    
    /**
     * Displays the person submenu and executes the selected operation.
     
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
     * Prompts the user for customer data, validating each field,
     * builds a Customer object, and registers it through PersonService.
    **/
    
    private void registerCustomer() {
        System.out.println("\n--- Register Customer ---");
        String id = readValidated("ID: ", this::isNumeric, "Invalid ID. Numbers only: ");
        String name = readValidated("Name: ", this::isValidName, "Invalid name. Letters only: ");
        String phone = readValidated("Phone: ", this::isValidPhone, "Invalid phone. Must be exactly 10 digits: ");
        String email = readValidated("Email: ", this::isValidEmail, "Invalid email. Must contain '@': ");

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

//VENTAS
    
    /**
     * Displays the sale submenu and executes the selected operation.
     
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
     * product ids (each validated as numeric), then attempts to register
     * the sale through SaleService, printing a friendly message if any
     * business rule is violated.
    **/
    
    private void registerSale() {
        System.out.println("\n--- Register Sale ---");
        String customerId = readValidated("Customer ID: ", this::isNumeric, "Invalid ID. Numbers only: ");
        String sellerId = readValidated("Seller ID: ", this::isNumeric, "Invalid ID. Numbers only: ");

        java.util.List<String> productIds = new java.util.ArrayList<>();
        boolean addingProducts = true;
        while (addingProducts) {
            System.out.print("Product ID (empty to finish): ");
            String productId = scanner.nextLine();
            if (productId.isBlank()) {
                addingProducts = false;
            } else if (!isNumeric(productId)) {
                System.out.println("Invalid ID. Numbers only.");
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
        String customerId = readValidated("Customer ID: ", this::isNumeric, "Invalid ID. Numbers only: ");
        saleService.getSalesByCustomer(customerId).forEach(this::printSale);
    }

    /**
     * Prompts for a seller id and prints the sales that seller attended.
    **/
    
    private void viewHistoryBySeller() {
        String sellerId = readValidated("Seller ID: ", this::isNumeric, "Invalid ID. Numbers only: ");
        saleService.getSalesBySeller(sellerId).forEach(this::printSale);
    }

    /**
     * Prints a single sale in a readable, one-line format, used by all
     * three history views to avoid repeating the same formatting logic.
     
     * @param sale the sale to print
     */
    private void printSale(com.gamezone.model.Sale sale) {
        System.out.println("Sale " + sale.getId() + " | Customer: " + sale.getCustomer().getName()
                + " | Seller: " + sale.getSeller().getName() + " | Total: " + sale.calculateTotal());
    }
}