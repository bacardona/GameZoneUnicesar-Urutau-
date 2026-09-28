package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.persistence.SaleRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Provides business logic for registering and querying sales.
 * Coordinates with ProductService and AccessoryService (stock validation and
 * update) and PersonService (customer/seller lookup) to enforce the sale rules.
 **/

//Encargado de la logica principal
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final PersonService personService;
    private final List<Sale> sales;

    /**
     * Creates a new SaleService.
     *
     * @param saleRepository repository used to persist sales
     * @param productService service used to validate and update product stock
     * @param accessoryService service used to validate and update accessory stock
     * @param personService service used to resolve customers and sellers
     * @param initialSales sales previously loaded at application startup
     **/

    public SaleService(SaleRepository saleRepository, ProductService productService,
                        AccessoryService accessoryService, PersonService personService,
                        List<Sale> initialSales) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.personService = personService;
        this.sales = new ArrayList<>(initialSales);
    }

    /**
     * Registers a new sale containing products and/or accessories. Every item
     * is resolved and its stock validated BEFORE any inventory is modified,
     * so a failed validation never leaves the inventory partially updated.
     * On success, the inventory is decreased automatically and the sale
     * is persisted.
     *
     * @param customerId the id of the customer making the purchase
     * @param sellerId the id of the seller attending the sale
     * @param itemIds the ids of the purchased products or accessories; a
     *                repeated id represents more than one unit of that item
     * @return the newly registered Sale
     * @throws IllegalArgumentException if the sale has no items, the
     *                                   customer/seller does not exist, an
     *                                   item does not exist, or stock is
     *                                   insufficient for any item
     **/

    //Registra una nueva venta (productos y/o accesorios) despues de validar las reglas
    public Sale registerSale(String customerId, String sellerId, List<String> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            throw new IllegalArgumentException("A sale must contain at least one product.");
        }

        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }

        Seller seller = findSellerById(sellerId);
        if (seller == null) {
            throw new IllegalArgumentException("Seller not found: " + sellerId);
        }

        Map<String, Integer> quantitiesById = countQuantities(itemIds);

        // Resolve every id (product or accessory) and validate its stock
        Map<String, Product> resolvedItems = new HashMap<>();
        for (Map.Entry<String, Integer> entry : quantitiesById.entrySet()) {
            Product item = findSellableById(entry.getKey());
            if (item == null) {
                throw new IllegalArgumentException(
                        "Product or accessory not found: " + entry.getKey());
            }
            if (item.getQuantity() < entry.getValue()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for: " + item.getTitle());
            }
            resolvedItems.put(entry.getKey(), item);
        }

        List<Product> items = new ArrayList<>();
        for (String id : itemIds) {
            items.add(resolvedItems.get(id));
        }

        // Update inventory delegating according to the real type of each item
        for (Map.Entry<String, Integer> entry : quantitiesById.entrySet()) {
            updateInventory(resolvedItems.get(entry.getKey()), entry.getValue());
        }

        Sale sale = new Sale(UUID.randomUUID().toString(), new Date(), customer, seller, items);
        sales.add(sale);
        saleRepository.save(sales);

        return sale;
    }

     /**
     * Returns the purchase history of a specific customer.
     *
     * @param customerId the id of the customer
     * @return the list of sales made by that customer
     **/

    public List<Sale> getSalesByCustomer(String customerId) {
        List<Sale> result = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getCustomer().getID().equals(customerId)) {
                result.add(sale);
            }
        }
        return result;
    }

    /**
     * Returns the sales attended by a specific seller.
     *
     * @param sellerId the id of the seller
     * @return the list of sales attended by that seller
    **/

    public List<Sale> getSalesBySeller(String sellerId) {
        List<Sale> result = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getSeller().getID().equals(sellerId)) {
                result.add(sale);
            }
        }
        return result;
    }

    /**
     * Returns the complete sales history.
     *
     * @return the full list of registered sales
     **/

    public List<Sale> getAllSales() {
        return new ArrayList<>(sales);
    }

    /**
     * Counts how many times each id appears in the requested list,
     * so that buying the same item multiple times is treated as one
     * stock check for the total quantity instead of several separate checks.
     *
     * @param itemIds the raw list of requested ids
     * @return a map from id to the quantity requested
    **/

    //Cuenta cuantas veces aparece cada ID para validar el stock una sola vez por item
    private Map<String, Integer> countQuantities(List<String> itemIds) {
        Map<String, Integer> counts = new HashMap<>();
        for (String id : itemIds) {
            counts.merge(id, 1, Integer::sum);
        }
        return counts;
    }

    /**
     * Decreases the stock of an item, delegating to the service that owns
     * its real type. The Accessory check goes first because every Accessory
     * is also a Product.
     *
     * @param item the item being sold
     * @param quantity the quantity sold
    **/

    private void updateInventory(Product item, int quantity) {
        if (item instanceof Accessory) {
            accessoryService.updateStock(item.getId(), quantity);
        } else {
            productService.updateStock(item.getId(), quantity);
        }
    }

    /**
     * Searches for a sellable item by id, looking first among products
     * and then among accessories.
     *
     * @param id the id to search for
     * @return the matching product or accessory, or null if not found
    **/

    private Product findSellableById(String id) {
        Product product = findProductById(id);
        if (product != null) {
            return product;
        }
        return accessoryService.findById(id);
    }

    /**
     * Searches for a customer by ID among all registered customers.
     * Added locally because PersonService does not expose a direct lookup method.
     *
     * @param customerId the id to search for
     * @return the matching customer, or null if not found
    **/

    //Buscar cliente por ID
    private Customer findCustomerById(String customerId) {
        for (Customer c : personService.listCustomers()) {
            if (c.getID().equals(customerId)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Searches for a seller by ID among all registered sellers.
     * Added locally because PersonService does not expose a direct lookup method.
     *
     * @param sellerId the id to search for
     * @return the matching seller, or null if not found
    **/

    //Buscar vendedor por ID
    private Seller findSellerById(String sellerId) {
        for (Seller s : personService.listSellers()) {
            if (s.getID().equals(sellerId)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Searches for a product by ID among all products in inventory.
     * Added locally because ProductService does not expose a direct lookup method.
     *
     * @param productId the id to search for
     * @return the matching product, or null if not found
    **/

    //Buscar un producto por ID
    private Product findProductById(String productId) {
        for (Product p : productService.listProducts()) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }
}