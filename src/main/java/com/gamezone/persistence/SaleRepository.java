package com.gamezone.persistence;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Handles file-based persistence for Sale objects using a plain-text CSV format.
 * Sales are stored by referencing the IDs of Customer, Seller, and Product,
 * which are resolved back into real objects when loading.
 **/

public class SaleRepository {

    private static final String FILE_PATH = "data/sales.csv";
    private static final String FIELD_SEPARATOR = ";";
    private static final String PRODUCT_SEPARATOR = ",";

    /**
     * Saves the complete list of sales to a CSV file, overwriting previous content.
     * @param sales the list of sales to persist
     */
    public void save(List<Sale> sales) {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Sale sale : sales) {
                writer.write(toLine(sale));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving sales: " + e.getMessage());
        }
    }

    /**
     * Loads sales from the CSV file, resolving stored IDs into the given
     * customer, seller, and product objects.

     * @param customers previously loaded customers
     * @param sellers previously loaded sellers
     * @param products previously loaded products
     * @return the list of sales recovered from disk
     **/
    
    public List<Sale> load(List<Customer> customers, List<Seller> sellers, List<Product> products) {
        List<Sale> sales = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return sales;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                Sale sale = fromLine(line, customers, sellers, products);
                if (sale != null) {
                    sales.add(sale);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading sales: " + e.getMessage());
        }
        return sales;
    }
    
    /**
     * Converts a Sale into a single CSV line, storing only the IDs of its
     * related customer, seller, and products instead of the full objects.
     *
     * @param sale the sale to convert
     * @return the CSV representation of the sale
    **/
    
    private String toLine(Sale sale) {
        List<Product> saleProducts = sale.getProducts();
        StringBuilder productIds = new StringBuilder();
        for (int i = 0; i < saleProducts.size(); i++) {
            productIds.append(saleProducts.get(i).getId());
            if (i < saleProducts.size() - 1) {
                productIds.append(PRODUCT_SEPARATOR);
            }
        }

        return String.join(FIELD_SEPARATOR,
                sale.getId(),
                String.valueOf(sale.getDate().getTime()),
                sale.getCustomer().getID(),
                sale.getSeller().getID(),
                productIds.toString());
    }
    
    /**
     * Rebuilds a Sale object from a single CSV line, resolving the stored
     * customer, seller, and product IDs into the actual in-memory objects.
     *
     * @param line the CSV line to parse
     * @param customers the customers available to resolve the buyer reference
     * @param sellers the sellers available to resolve the seller reference
     * @param products the products available to resolve the purchased items
     * @return the reconstructed Sale, or null if the customer or seller could not be found
    **/
    
    private Sale fromLine(String line, List<Customer> customers, List<Seller> sellers, List<Product> products) {
        String[] fields = line.split(FIELD_SEPARATOR, -1);
        if (fields.length < 5) return null;

        String id = fields[0];
        Date date = new Date(Long.parseLong(fields[1]));
        Customer customer = findCustomerById(customers, fields[2]);
        Seller seller = findSellerById(sellers, fields[3]);

        List<Product> saleProducts = new ArrayList<>();
        if (!fields[4].isEmpty()) {
            for (String productId : fields[4].split(PRODUCT_SEPARATOR)) {
                Product product = findProductById(products, productId);
                if (product != null) {
                    saleProducts.add(product);
                }
            }
        }

        if (customer == null || seller == null) return null;
        return new Sale(id, date, customer, seller, saleProducts);
    }

    /**
     * Searches for a customer by ID within the given list.
     *
     * @param customers the customers to search through
     * @param id the ID to look for
     * @return the matching customer, or null if not found
    **/
    
    private Customer findCustomerById(List<Customer> customers, String id) {
        for (Customer c : customers) {
            if (c.getID().equals(id)) return c;
        }
        return null;
    }

    /**
     * Searches for a seller by ID within the given list.
     *
     * @param sellers the sellers to search through
     * @param id the ID to look for
     * @return the matching seller, or null if not found
    **/
    
    private Seller findSellerById(List<Seller> sellers, String id) {
        for (Seller s : sellers) {
            if (s.getID().equals(id)) return s;
        }
        return null;
    }

    /**
     * Searches for a product by ID within the given list.
     *
     * @param products the products to search through
     * @param id the ID to look for
     * @return the matching product, or null if not found
    **/
    
    private Product findProductById(List<Product> products, String id) {
        for (Product p : products) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }
}