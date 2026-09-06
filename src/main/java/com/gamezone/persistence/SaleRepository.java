package com.gamezone.persistence;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.SaleItem;
import com.gamezone.model.Seller;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaleRepository {

    private static final String FILE_PATH = "data/sales.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private List<Customer> customers;
    private List<Seller> sellers;
    private List<Product> products;

    public SaleRepository(List<Customer> customers, List<Seller> sellers, List<Product> products) {
        this.customers = customers;
        this.sellers = sellers;
        this.products = products;
    }

    public void save(List<Sale> sales) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Sale sale : sales) {
                StringBuilder line = new StringBuilder();
                line.append(sale.getId()).append("|");
                line.append(DATE_FORMAT.format(sale.getDate())).append("|");
                line.append(sale.getCustomer().getId()).append("|");
                line.append(sale.getSeller().getId()).append("|");

                StringBuilder itemsPart = new StringBuilder();
                for (SaleItem item : sale.getItems()) {
                    if (itemsPart.length() > 0) {
                        itemsPart.append(",");
                    }
                    itemsPart.append(item.getProduct().getId()).append(":").append(item.getQuantity());
                }
                line.append(itemsPart);

                writer.write(line.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving sales: " + e.getMessage());
        }
    }

    public List<Sale> load() {
        List<Sale> sales = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return sales;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] parts = line.split("\\|");
                String id = parts[0];
                Date date = DATE_FORMAT.parse(parts[1]);
                Customer customer = findCustomerById(parts[2]);
                Seller seller = findSellerById(parts[3]);
                String itemsPart = parts.length > 4 ? parts[4] : "";

                List<SaleItem> items = new ArrayList<>();
                if (!itemsPart.isBlank()) {
                    for (String token : itemsPart.split(",")) {
                        String[] itemParts = token.split(":");
                        Product product = findProductById(itemParts[0]);
                        int quantity = Integer.parseInt(itemParts[1]);
                        items.add(new SaleItem(product, quantity));
                    }
                }

                sales.add(new Sale(id, date, customer, seller, items));
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error loading sales: " + e.getMessage());
        }

        return sales;
    }

    private Customer findCustomerById(String id) {
        for (Customer c : customers) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    private Seller findSellerById(String id) {
        for (Seller s : sellers) {
            if (s.getId().equals(id)) return s;
        }
        return null;
    }

    private Product findProductById(String id) {
        for (Product p : products) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }
}