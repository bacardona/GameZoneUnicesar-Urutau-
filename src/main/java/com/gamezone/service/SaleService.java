package com.gamezone.service;

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
 * Coordinates with ProductService (stock validation and update) and
 * PersonService (customer/seller lookup) to enforce the sale rules.
 **/

public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductService productService;
    private final PersonService personService;
    private final List<Sale> sales;

    /**
     * Creates a new SaleService.
     *
     * @param saleRepository repository used to persist sales
     * @param productService service used to validate and update product stock
     * @param personService service used to resolve customers and sellers
     * @param initialSales sales previously loaded at application startup
     **/
    
    public SaleService(SaleRepository saleRepository, ProductService productService,
                        PersonService personService, List<Sale> initialSales) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.personService = personService;
        this.sales = new ArrayList<>(initialSales);
    }

    /**
     * Registers a new sale after validating business rules: the sale must
     * contain at least one product, the customer and seller must exist,
     * and there must be enough stock for each requested product.
     * On success, the inventory is decreased automatically and the sale
     * is persisted.
     *
     * @param customerId the id of the customer making the purchase
     * @param sellerId the id of the seller attending the sale
     * @param productIds the ids of the purchased products; a repeated id
     *                    represents more than one unit of that product
     * @return the newly registered Sale
     * @throws IllegalArgumentException if the sale has no products, the
     *                                   customer/seller does not exist, or
     *                                   stock is insufficient for any product
     **/
    
    public Sale registerSale(String customerId, String sellerId, List<String> productIds) {
        if (productIds == null || productIds.isEmpty()) {
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

        Map<String, Integer> quantitiesByProductId = countQuantities(productIds);

        for (Map.Entry<String, Integer> entry : quantitiesByProductId.entrySet()) {
            if (!hasSufficientStock(entry.getKey(), entry.getValue())) {
                throw new IllegalArgumentException(
                        "Insufficient stock for product: " + entry.getKey());
            }
        }

        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            products.add(findProductById(productId));
        }

        for (Map.Entry<String, Integer> entry : quantitiesByProductId.entrySet()) {
            productService.updateStock(entry.getKey(), entry.getValue());
        }

        Sale sale = new Sale(UUID.randomUUID().toString(), new Date(), customer, seller, products);
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
     * Counts how many times each product id appears in the requested list,
     * so that buying the same product multiple times is treated as one
     * stock check for the total quantity instead of several separate checks.
     *
     * @param productIds the raw list of requested product ids
     * @return a map from product id to the quantity requested
    **/
    
    private Map<String, Integer> countQuantities(List<String> productIds) {
        Map<String, Integer> counts = new HashMap<>();
        for (String id : productIds) {
            counts.merge(id, 1, Integer::sum);
        }
        return counts;
    }

    /**
     * Searches for a customer by ID among all registered customers.
     * Added locally because PersonService does not expose a direct lookup method.
     *
     * @param customerId the id to search for
     * @return the matching customer, or null if not found
    **/
    
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
    
    private Product findProductById(String productId) {
        for (Product p : productService.listProducts()) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Checks whether the current stock of a product is enough to cover
     * the requested quantity.
     *
     * @param productId the id of the product to check
     * @param quantityNeeded the quantity being requested
     * @return true if enough stock is available, false otherwise
    **/
    
    private boolean hasSufficientStock(String productId, int quantityNeeded) {
        Product product = findProductById(productId);
        return product != null && product.getQuantity() >= quantityNeeded;
    }
}