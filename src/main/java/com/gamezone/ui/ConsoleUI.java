package com.gamezone.ui;

import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

import java.util.Scanner;

/**
 * Console-based user interface for the GameZone system.
 * Displays the main menu and delegates each operation to the
 * corresponding service (PersonService, ProductService, SaleService).
 */
public class ConsoleUI {

    private final Scanner scanner;
    private final PersonService personService;
    private final ProductService productService;
    private final SaleService saleService;

    /**
     * Creates a new ConsoleUI.
     *
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
     */
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

    // Los submenús privados van en la Parte 2 (próximo commit)
    private void showProductMenu(String option) {
        System.out.println("Product menu not implemented yet.");
    }

    private void showPersonMenu(String option) {
        System.out.println("Person menu not implemented yet.");
    }

    private void showSaleMenu(String option) {
        System.out.println("Sale menu not implemented yet.");
    }
}