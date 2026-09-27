package com.gamezone.persistence;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Handles the persistence of warranties in the GameZone system. Warranties are
 * stored in the data/warranties.csv file.
 */
public class WarrantyRepository {

    private final String filePath = "data/warranties.csv";
    private final SaleService saleService;
    private final ProductService productService;

    /**
     * Creates a WarrantyRepository with the services required to resolve sale
     * and product references.
     *
     * @param saleService service used to find sales
     * @param productService service used to find products
     */
    public WarrantyRepository(SaleService saleService,
            ProductService productService) {
        this.saleService = saleService;
        this.productService = productService;
    }

    /**
     * Saves all warranties to the CSV file.
     *
     * @param warranties warranties to persist
     */
    public void saveAll(List<Warranty> warranties) {
        File folder = new File("data");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedWriter writer
                = new BufferedWriter(new FileWriter(filePath))) {

            for (Warranty warranty : warranties) {

                String warrantyType;

                if (warranty instanceof BasicWarranty) {
                    warrantyType = "BASIC";
                } else if (warranty instanceof ExtendedWarranty) {
                    warrantyType = "EXTENDED";
                } else {
                    continue;
                }

                String line = warrantyType + ";"
                        + warranty.getId() + ";"
                        + warranty.getProduct().getId() + ";"
                        + warranty.getSale().getId() + ";"
                        + warranty.getStartDate();

                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Error saving warranties.", e);
        }
    }

    /**
     * Loads all warranties from the CSV file.
     *
     * @return the loaded warranties, or an empty list if the file does not
     * exist
     */
    public List<Warranty> loadAll() {
        List<Warranty> warranties = new ArrayList<>();

        File file = new File(filePath);

        if (!file.exists()) {
            return warranties;
        }

        try (BufferedReader reader
                = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";", -1);

                if (parts.length < 5) {
                    continue;
                }

                String type = parts[0];
                String warrantyId = parts[1];
                String productId = parts[2];
                String saleId = parts[3];
                LocalDate startDate = LocalDate.parse(parts[4]);

                Sale sale = findSaleById(saleId);
                Product product = findProductById(productId);

                if (sale == null || product == null) {
                    continue;
                }

                Warranty warranty;

                if ("BASIC".equalsIgnoreCase(type)) {

                    warranty = new BasicWarranty(
                            warrantyId,
                            product,
                            sale,
                            startDate
                    );

                } else if ("EXTENDED".equalsIgnoreCase(type)) {

                    warranty = new ExtendedWarranty(
                            warrantyId,
                            product,
                            sale,
                            startDate
                    );

                } else {
                    continue;
                }

                warranties.add(warranty);
            }

        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Error loading warranties.", e);
        }

        return warranties;
    }

    /**
     * Finds a sale by its identifier.
     *
     * @param saleId sale identifier
     * @return the matching sale, or null if not found
     */
    private Sale findSaleById(String saleId) {
        for (Sale sale : saleService.getAllSales()) {
            if (sale.getId().equals(saleId)) {
                return sale;
            }
        }

        return null;
    }

    /**
     * Finds a product by its identifier.
     *
     * @param productId product identifier
     * @return the matching product, or null if not found
     */
    private Product findProductById(String productId) {
        for (Product product : productService.listProducts()) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }

        return null;
    }
}
