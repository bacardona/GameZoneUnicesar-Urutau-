package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Console;
import com.gamezone.model.Customer;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.persistence.SaleRepository;

import java.time.LocalDate;
import java.time.ZoneId;
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
    private final PromotionService promotionService;
    private WarrantyService warrantyService;
    private final PersonService personService;
    private final List<Sale> sales;

    /**
     * Creates a new SaleService.
     *
     * @param saleRepository repository used to persist sales
     * @param productService service used to validate and update product stock
     * @param accessoryService service used to validate and update accessory stock
     * @param promotionService service used to find the best promotion for a sale
     * @param personService service used to resolve customers and sellers
     * @param initialSales sales previously loaded at application startup
     **/

    public SaleService(SaleRepository saleRepository, ProductService productService,
                        AccessoryService accessoryService, PromotionService promotionService,
                        PersonService personService, List<Sale> initialSales) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.promotionService = promotionService;
        this.personService = personService;
        this.sales = new ArrayList<>(initialSales);
    }

    /**
     * Sets the service used to assign warranties to the consoles sold.
     * It is injected after construction because WarrantyRepository needs a
     * SaleService and SaleService needs a WarrantyService (circular
     * dependency). This setter is a temporary workaround until adjustment A2
     * removes the cycle.
     *
     * @param warrantyService service used to assign warranties
     **/

    public void setWarrantyService(WarrantyService warrantyService) {
        this.warrantyService = warrantyService;
    }

    /**
     * Registers a new sale containing products and/or accessories. Every item
     * is resolved and its stock validated BEFORE any inventory is modified,
     * so a failed validation never leaves the inventory partially updated.
     * The best active promotion (highest discount) is applied automatically
     * over the subtotal. Every console gets a free basic warranty and, when
     * requested, an extended warranty whose cost is added to the total.
     * On success, the inventory is decreased automatically
     * and the sale is persisted.
     *
     * @param customerId the id of the customer making the purchase
     * @param sellerId the id of the seller attending the sale
     * @param itemIds the ids of the purchased products or accessories; a
     *                repeated id represents more than one unit of that item
     * @param productIdsWithExtendedWarranty ids of the consoles that get an extended
     *                warranty; a console id repeated N times requests N extended warranties
     * @return the newly registered Sale
     * @throws IllegalArgumentException if the sale has no items, the
     *                                   customer/seller does not exist, an
     *                                   item does not exist, or stock is
     *                                   insufficient for any item, or an extended
     *                                   warranty is requested for a non-console item
     **/

    //Registra una nueva venta (productos y/o accesorios) despues de validar las reglas
    public Sale registerSale(String customerId, String sellerId, List<String> itemIds,
                             List<String> productIdsWithExtendedWarranty) {
        if (warrantyService == null) {
            throw new IllegalStateException("WarrantyService has not been set.");
        }

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

        // Extended warranties can only be requested for consoles that are part of the sale
        Map<String, Integer> extendedRequests = countQuantities(
                productIdsWithExtendedWarranty == null ? new ArrayList<>() : productIdsWithExtendedWarranty);
        for (Map.Entry<String, Integer> entry : extendedRequests.entrySet()) {
            Product requested = findProductById(entry.getKey());
            if (!(requested instanceof Console)
                    || quantitiesById.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
                throw new IllegalArgumentException(
                        "Extended warranty is only available for consoles included in the sale: "
                                + entry.getKey());
            }
        }

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

        Sale sale = new Sale(UUID.randomUUID().toString(), new Date(), customer, seller, items);

        // Apply the best active promotion: final total = subtotal - discount.
        // Done before touching the inventory so a failure here leaves stock intact.
        Promotion bestPromotion = promotionService.findBestPromotionFor(sale);
        if (bestPromotion != null) {
            double discount = Math.min(bestPromotion.calculateDiscount(sale), sale.calculateTotal());
            sale.setAppliedPromotionName(bestPromotion.getName());
            sale.setDiscountAmount(discount);
        }

        // Warranties: a basic one for every console, plus the extended ones requested
        LocalDate warrantyStart = sale.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Map<String, Integer> pendingExtended = new HashMap<>(extendedRequests);
        double extendedWarrantyCost = 0.0;
        for (Product item : items) {
            if (item instanceof Console) {
                warrantyService.assignBasicWarranty(item, sale, warrantyStart);
                int pending = pendingExtended.getOrDefault(item.getId(), 0);
                if (pending > 0) {
                    ExtendedWarranty extended = warrantyService.assignExtendedWarranty(item, sale, warrantyStart);
                    extendedWarrantyCost += extended.getAdditionalCost();
                    pendingExtended.put(item.getId(), pending - 1);
                }
            }
        }
        sale.setExtendedWarrantyCost(extendedWarrantyCost);

        // Update inventory delegating according to the real type of each item
        for (Map.Entry<String, Integer> entry : quantitiesById.entrySet()) {
            updateInventory(resolvedItems.get(entry.getKey()), entry.getValue());
        }

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