package com.gamezone.ui;

import com.gamezone.model.Accessory;
import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.Cable;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.Console;
import com.gamezone.model.Controller;
import com.gamezone.model.Customer;
import com.gamezone.model.Memory;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
import com.gamezone.model.Warranty;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.SaleService;
import com.gamezone.service.WarrantyService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Console-based user interface for the GameZone system.
 * Displays the main menu and delegates each operation to the
 * corresponding service (PersonService, ProductService, AccessoryService,
 * SaleService). Also validates raw user input before it is passed to any service.
**/

public class ConsoleUI {

    private final Scanner scanner;
    private final PersonService personService;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final PromotionService promotionService;
    private final WarrantyService warrantyService;
    private final SaleService saleService;

    /**
     * Creates a new ConsoleUI.
     *
     * @param personService service used to manage customers and sellers
     * @param productService service used to manage products
     * @param accessoryService service used to manage accessories
     * @param promotionService service used to manage promotions
     * @param warrantyService service used to query warranties
     * @param saleService service used to manage sales
    **/

    public ConsoleUI(PersonService personService, ProductService productService,
                     AccessoryService accessoryService, PromotionService promotionService,
                     WarrantyService warrantyService, SaleService saleService) {
        this.scanner = new Scanner(System.in);
        this.personService = personService;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.promotionService = promotionService;
        this.warrantyService = warrantyService;
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
            System.out.println("--- Accessories ---");
            System.out.println("11. Accessory management");
            System.out.println("--- Promotions ---");
            System.out.println("12. Promotion management");
            System.out.println("--- Warranties ---");
            System.out.println("13. Warranty management");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1", "2", "3" -> showProductMenu(option);
                case "4", "5", "6" -> showPersonMenu(option);
                case "7", "8", "9", "10" -> showSaleMenu(option);
                case "11" -> showAccessoryMenu();
                case "12" -> showPromotionMenu();
                case "13" -> showWarrantyMenu();
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
     *
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

//ACCESORIOS

    /**
     * Displays the accessory management submenu in a loop until the user
     * chooses to go back to the main menu.
    **/

    private void showAccessoryMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Accessory Management ---");
            System.out.println("1. Register a controller");
            System.out.println("2. Register a cable");
            System.out.println("3. Register a memory");
            System.out.println("4. List all accessories");
            System.out.println("5. List accessories by type");
            System.out.println("6. List accessories compatible with a console");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            switch (scanner.nextLine()) {
                case "1" -> registerController();
                case "2" -> registerCable();
                case "3" -> registerMemory();
                case "4" -> listAllAccessories();
                case "5" -> listAccessoriesByType();
                case "6" -> listCompatibleAccessories();
                case "0" -> back = true;
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    /**
     * Prompts for controller data and registers it through AccessoryService.
    **/

    private void registerController() {
        System.out.println("\n--- Register Controller ---");
        String id = readNewSellableId();
        String title = readValidated("Title: ", this::isAlphanumeric, "Invalid title. Letters and numbers only: ");
        double price = readValidPrice();
        int quantity = readValidQuantity();
        String connectionType = readValidated("Connection type (1. Wireless, 2. Wired): ",
                o -> o.equals("1") || o.equals("2"), "Invalid option. Enter 1 or 2: ")
                .equals("1") ? "Inalámbrico" : "Alámbrico";

        Controller controller = new Controller(id, title, price, quantity, connectionType);
        applyCompatibility(controller);
        try {
            accessoryService.registerController(controller);
            System.out.println("Controller registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not register controller: " + e.getMessage());
        }
    }

    /**
     * Prompts for cable data and registers it through AccessoryService.
    **/

    private void registerCable() {
        System.out.println("\n--- Register Cable ---");
        String id = readNewSellableId();
        String title = readValidated("Title: ", this::isAlphanumeric, "Invalid title. Letters and numbers only: ");
        double price = readValidPrice();
        int quantity = readValidQuantity();
        float meters = readValidPositiveFloat("Length in meters: ");
        String type = readValidated("Connector type (HDMI, USB, optical...): ", this::isAlphanumeric,
                "Invalid connector type. Letters and numbers only: ");

        Cable cable = new Cable(id, title, price, quantity, meters, type);
        applyCompatibility(cable);
        try {
            accessoryService.registerCable(cable);
            System.out.println("Cable registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not register cable: " + e.getMessage());
        }
    }

    /**
     * Prompts for memory data and registers it through AccessoryService.
    **/

    private void registerMemory() {
        System.out.println("\n--- Register Memory ---");
        String id = readNewSellableId();
        String title = readValidated("Title: ", this::isAlphanumeric, "Invalid title. Letters and numbers only: ");
        double price = readValidPrice();
        int quantity = readValidQuantity();
        int capacityGb = readValidPositiveInt("Capacity in GB: ");
        String memoryType = readValidated("Memory type (SD, microSD, internal card): ", this::isAlphanumeric,
                "Invalid memory type. Letters and numbers only: ");

        Memory memory = new Memory(id, title, price, quantity, capacityGb, memoryType);
        applyCompatibility(memory);
        try {
            accessoryService.registerMemory(memory);
            System.out.println("Memory registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not register memory: " + e.getMessage());
        }
    }

    /**
     * Reads a numeric id that is not used by any product or accessory, since
     * sales resolve products first and a repeated id would hide the accessory.
     *
     * @return an id not used by any existing product or accessory
    **/

    private String readNewSellableId() {
        while (true) {
            String id = readValidated("ID: ", this::isNumeric, "Invalid ID. Numbers only: ");
            boolean usedByProduct = false;
            for (Product p : productService.listProducts()) {
                if (p.getId().equals(id)) {
                    usedByProduct = true;
                    break;
                }
            }
            if (usedByProduct || accessoryService.findById(id) != null) {
                System.out.println("That ID is already used by another product or accessory.");
            } else {
                return id;
            }
        }
    }

    /**
     * Asks for the ids of the consoles the accessory is compatible with
     * (comma separated, optional) and stores them in the accessory. Every id
     * must belong to a registered console.
     *
     * @param accessory the accessory receiving the compatibility information
    **/

    private void applyCompatibility(Accessory accessory) {
        while (true) {
            System.out.print("Compatible console IDs (comma separated, empty for none): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return;
            }
            String[] ids = input.split("\\s*,\\s*");
            List<String> invalid = new ArrayList<>();
            for (String id : ids) {
                if (!isExistingConsole(id)) {
                    invalid.add(id);
                }
            }
            if (invalid.isEmpty()) {
                for (String id : ids) {
                    accessory.addCompatibleConsoleId(id);
                }
                return;
            }
            System.out.println("These IDs are not registered consoles: " + String.join(", ", invalid));
        }
    }

    /**
     * Checks whether the given id belongs to a registered console.
    **/

    private boolean isExistingConsole(String id) {
        for (Product p : productService.listProducts()) {
            if (p instanceof Console && p.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reads a decimal number strictly greater than zero.
    **/

    private float readValidPositiveFloat(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                float value = Float.parseFloat(scanner.nextLine());
                if (value > 0) {
                    return value;
                }
                System.out.println("Value must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    /**
     * Reads a whole number strictly greater than zero.
    **/

    private int readValidPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value > 0) {
                    return value;
                }
                System.out.println("Value must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a whole number.");
            }
        }
    }

    /**
     * Prints the description of every registered accessory.
    **/

    private void listAllAccessories() {
        System.out.println("\n--- Accessories ---");
        accessoryService.listAllAccessories().forEach(a -> System.out.println(a.getDescription()));
    }

    /**
     * Prompts for an accessory type and prints the accessories of that type.
    **/

    private void listAccessoriesByType() {
        String type = readValidated("Type (Controller/Cable/Memory): ",
                t -> t.equalsIgnoreCase("Controller") || t.equalsIgnoreCase("Cable")
                        || t.equalsIgnoreCase("Memory"),
                "Invalid type. Use Controller, Cable or Memory: ");
        accessoryService.listAccessoriesByType(type)
                .forEach(a -> System.out.println(a.getDescription()));
    }

    /**
     * Prompts for a console id and prints the accessories compatible with it.
    **/

    private void listCompatibleAccessories() {
        String consoleId = readValidated("Console ID: ", this::isNumeric, "Invalid ID. Numbers only: ");
        accessoryService.findAccessoriesCompatibleWith(consoleId)
                .forEach(a -> System.out.println(a.getDescription()));
    }

//PROMOCIONES

    /**
     * Displays the promotion management submenu in a loop until the user
     * chooses to go back to the main menu.
    **/

    private void showPromotionMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Promotion Management ---");
            System.out.println("1. Register a percentage discount");
            System.out.println("2. Register a category discount");
            System.out.println("3. Register a bulk purchase discount");
            System.out.println("4. List all promotions");
            System.out.println("5. List active promotions");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            switch (scanner.nextLine()) {
                case "1" -> registerPercentageDiscount();
                case "2" -> registerCategoryDiscount();
                case "3" -> registerBulkPurchaseDiscount();
                case "4" -> listPromotions(promotionService.listAllPromotions(), "All Promotions");
                case "5" -> listPromotions(promotionService.listActivePromotions(), "Active Promotions");
                case "0" -> back = true;
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    /**
     * Prompts for the data of a percentage discount and registers it.
    **/

    private void registerPercentageDiscount() {
        System.out.println("\n--- Register Percentage Discount ---");
        String id = readNewPromotionId();
        String name = readValidated("Name: ", this::isAlphanumeric, "Invalid name. Letters and numbers only: ");
        LocalDate startDate = readValidDate("Start date (yyyy-MM-dd): ");
        LocalDate endDate = readValidDate("End date (yyyy-MM-dd): ");
        double percentage = readValidPercentage();

        try {
            promotionService.registerPercentageDiscount(id, name, startDate, endDate, percentage);
            System.out.println("Percentage discount registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not register promotion: " + e.getMessage());
        }
    }

    /**
     * Prompts for the data of a category discount and registers it.
    **/

    private void registerCategoryDiscount() {
        System.out.println("\n--- Register Category Discount ---");
        String id = readNewPromotionId();
        String name = readValidated("Name: ", this::isAlphanumeric, "Invalid name. Letters and numbers only: ");
        LocalDate startDate = readValidDate("Start date (yyyy-MM-dd): ");
        LocalDate endDate = readValidDate("End date (yyyy-MM-dd): ");
        double percentage = readValidPercentage();
        String category = readValidated("Category (1. VIDEOGAME, 2. CONSOLE): ",
                o -> o.equals("1") || o.equals("2"), "Invalid option. Enter 1 or 2: ")
                .equals("1") ? "VIDEOGAME" : "CONSOLE";

        try {
            promotionService.registerCategoryDiscount(id, name, startDate, endDate, percentage, category);
            System.out.println("Category discount registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not register promotion: " + e.getMessage());
        }
    }

    /**
     * Prompts for the data of a bulk purchase discount and registers it.
    **/

    private void registerBulkPurchaseDiscount() {
        System.out.println("\n--- Register Bulk Purchase Discount ---");
        String id = readNewPromotionId();
        String name = readValidated("Name: ", this::isAlphanumeric, "Invalid name. Letters and numbers only: ");
        LocalDate startDate = readValidDate("Start date (yyyy-MM-dd): ");
        LocalDate endDate = readValidDate("End date (yyyy-MM-dd): ");
        int minQuantity = readValidPositiveInt("Minimum quantity of products: ");
        double percentage = readValidPercentage();

        try {
            promotionService.registerBulkPurchaseDiscount(id, name, startDate, endDate, minQuantity, percentage);
            System.out.println("Bulk purchase discount registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not register promotion: " + e.getMessage());
        }
    }

    /**
     * Prints a list of promotions under the given title.
     *
     * @param promotions the promotions to print
     * @param title the heading shown before the list
    **/

    private void listPromotions(java.util.List<Promotion> promotions, String title) {
        System.out.println("\n--- " + title + " ---");
        if (promotions.isEmpty()) {
            System.out.println("No promotions found.");
        }
        promotions.forEach(p -> System.out.println(describePromotion(p)));
    }

    /**
     * Builds a one-line description of a promotion, including the details
     * specific to its concrete type.
     *
     * @param promotion the promotion to describe
     * @return the description text
    **/

    private String describePromotion(Promotion promotion) {
        String details = "";
        if (promotion instanceof PercentageDiscount) {
            PercentageDiscount pd = (PercentageDiscount) promotion;
            details = "Percentage: " + pd.getPercentage() + "%";
        } else if (promotion instanceof CategoryDiscount) {
            CategoryDiscount cd = (CategoryDiscount) promotion;
            details = "Category: " + cd.getTargetCategory() + ", " + cd.getPercentage() + "%";
        } else if (promotion instanceof BulkPurchaseDiscount) {
            BulkPurchaseDiscount bd = (BulkPurchaseDiscount) promotion;
            details = "Min quantity: " + bd.getMinQuantity() + ", " + bd.getPercentage() + "%";
        }
        return promotion.getId() + " | " + promotion.getName() + " | " + details
                + " | " + promotion.getStartDate() + " to " + promotion.getEndDate();
    }

    /**
     * Reads a numeric promotion id that is not used by another promotion.
    **/

    private String readNewPromotionId() {
        while (true) {
            String id = readValidated("ID: ", this::isNumeric, "Invalid ID. Numbers only: ");
            if (promotionService.findById(id) == null) {
                return id;
            }
            System.out.println("That ID is already used by another promotion.");
        }
    }

    /**
     * Reads a date in yyyy-MM-dd format, repeating until it is valid.
    **/

    private LocalDate readValidDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Use the format yyyy-MM-dd (example: 2026-12-31).");
            }
        }
    }

    /**
     * Reads a discount percentage greater than 0 and at most 100.
    **/

    private double readValidPercentage() {
        while (true) {
            System.out.print("Discount percentage (0-100): ");
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (value > 0 && value <= 100) {
                    return value;
                }
                System.out.println("Percentage must be greater than 0 and at most 100.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

//GARANTIAS

    /**
     * Asks, for every console unit in the sale, whether the customer wants
     * the extended warranty. Every console already gets the basic warranty
     * automatically.
     *
     * @param itemIds the ids of the items in the sale (a repeated id is one unit each)
     * @return the ids of the consoles that requested an extended warranty
    **/

    private java.util.List<String> askExtendedWarranties(java.util.List<String> itemIds) {
        java.util.List<String> extendedIds = new java.util.ArrayList<>();
        java.util.Map<String, Integer> unitByConsole = new java.util.HashMap<>();

        for (String id : itemIds) {
            if (!isExistingConsole(id)) {
                continue;
            }
            int unit = unitByConsole.merge(id, 1, Integer::sum);
            String answer = readValidated(
                    "Extended warranty for console " + findProductTitle(id) + " (unit " + unit
                            + ", 12 months, +10% of the price)? (y/n): ",
                    a -> a.equalsIgnoreCase("y") || a.equalsIgnoreCase("n"),
                    "Invalid answer. Enter y or n: ");
            if (answer.equalsIgnoreCase("y")) {
                extendedIds.add(id);
            }
        }
        return extendedIds;
    }

    /**
     * Returns the title of the product with the given id, or the id itself if not found.
    **/

    private String findProductTitle(String id) {
        for (Product p : productService.listProducts()) {
            if (p.getId().equals(id)) {
                return p.getTitle();
            }
        }
        return id;
    }

    /**
     * Displays the warranty management submenu in a loop until the user
     * chooses to go back to the main menu.
    **/

    private void showWarrantyMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Warranty Management ---");
            System.out.println("1. List all warranties");
            System.out.println("2. List active warranties");
            System.out.println("3. List warranties expiring soon");
            System.out.println("4. Find the warranty of a product in a sale");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            switch (scanner.nextLine()) {
                case "1" -> listWarranties(warrantyService.listAllWarranties(), "All Warranties");
                case "2" -> listWarranties(warrantyService.listActiveWarranties(), "Active Warranties");
                case "3" -> listExpiringWarranties();
                case "4" -> findWarrantyByProduct();
                case "0" -> back = true;
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    /**
     * Prints a list of warranties under the given title.
    **/

    private void listWarranties(java.util.List<Warranty> warranties, String title) {
        System.out.println("\n--- " + title + " ---");
        if (warranties.isEmpty()) {
            System.out.println("No warranties found.");
        }
        warranties.forEach(w -> System.out.println(w.getWarrantyType() + " | "
                + w.getProduct().getTitle() + " | Sale: " + w.getSale().getId() + " | "
                + w.getStartDate() + " to " + w.getEndDate()
                + " | Extra cost: $" + w.getAdditionalCost()));
    }

    /**
     * Prompts for a number of days and lists the warranties that expire within them.
    **/

    private void listExpiringWarranties() {
        int days = readValidPositiveInt("Days ahead: ");
        listWarranties(warrantyService.listWarrantiesExpiringSoon(days),
                "Warranties expiring in the next " + days + " days");
    }

    /**
     * Prompts for a product id and a sale id and prints the matching warranty certificate.
    **/

    private void findWarrantyByProduct() {
        String productId = readValidated("Product ID: ", this::isNumeric, "Invalid ID. Numbers only: ");
        String saleId = readValidated("Sale ID: ", v -> !v.isBlank(), "Sale ID cannot be empty: ");
        Warranty warranty = warrantyService.findWarrantyByProduct(productId, saleId.trim());
        if (warranty == null) {
            System.out.println("No warranty found for that product in that sale.");
        } else {
            System.out.println(warranty.generateWarrantyCertificate());
        }
    }

//VENTAS

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
     * product or accessory ids (each validated as numeric), then attempts to
     * register the sale through SaleService, printing a friendly message if
     * any business rule is violated.
    **/

    private void registerSale() {
        System.out.println("\n--- Register Sale ---");
        String customerId = readValidated("Customer ID: ", this::isNumeric, "Invalid ID. Numbers only: ");
        String sellerId = readValidated("Seller ID: ", this::isNumeric, "Invalid ID. Numbers only: ");

        java.util.List<String> itemIds = new java.util.ArrayList<>();
        boolean addingItems = true;
        while (addingItems) {
            System.out.print("Product or accessory ID (empty to finish): ");
            String itemId = scanner.nextLine();
            if (itemId.isBlank()) {
                addingItems = false;
            } else if (!isNumeric(itemId)) {
                System.out.println("Invalid ID. Numbers only.");
            } else {
                itemIds.add(itemId);
            }
        }

        java.util.List<String> extendedWarrantyIds = askExtendedWarranties(itemIds);

        try {
            var sale = saleService.registerSale(customerId, sellerId, itemIds, extendedWarrantyIds);
            System.out.println("\nSale registered.\n" + sale.generateReceipt());
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
     *
     * @param sale the sale to print
     */
    private void printSale(com.gamezone.model.Sale sale) {
        System.out.println("Sale " + sale.getId() + " | Customer: " + sale.getCustomer().getName()
                + " | Seller: " + sale.getSeller().getName() + " | Total: " + sale.calculateFinalTotal());
    }
}