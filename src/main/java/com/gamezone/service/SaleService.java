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

public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductService productService;
    private final PersonService personService;
    private final List<Sale> sales;

    public SaleService(SaleRepository saleRepository, ProductService productService,
                        PersonService personService, List<Sale> initialSales) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.personService = personService;
        this.sales = new ArrayList<>(initialSales);
    }

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

    public List<Sale> getSalesByCustomer(String customerId) {
        List<Sale> result = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getCustomer().getID().equals(customerId)) {
                result.add(sale);
            }
        }
        return result;
    }

    public List<Sale> getSalesBySeller(String sellerId) {
        List<Sale> result = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getSeller().getID().equals(sellerId)) {
                result.add(sale);
            }
        }
        return result;
    }

    public List<Sale> getAllSales() {
        return new ArrayList<>(sales);
    }

    private Map<String, Integer> countQuantities(List<String> productIds) {
        Map<String, Integer> counts = new HashMap<>();
        for (String id : productIds) {
            counts.merge(id, 1, Integer::sum);
        }
        return counts;
    }

    private Customer findCustomerById(String customerId) {
        for (Customer c : personService.listCustomers()) {
            if (c.getID().equals(customerId)) {
                return c;
            }
        }
        return null;
    }

    private Seller findSellerById(String sellerId) {
        for (Seller s : personService.listSellers()) {
            if (s.getID().equals(sellerId)) {
                return s;
            }
        }
        return null;
    }

    private Product findProductById(String productId) {
        for (Product p : productService.listProducts()) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    private boolean hasSufficientStock(String productId, int quantityNeeded) {
        Product product = findProductById(productId);
        return product != null && product.getQuantity() >= quantityNeeded;
    }
}