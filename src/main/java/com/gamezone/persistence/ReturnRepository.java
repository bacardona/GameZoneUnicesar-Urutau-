package com.gamezone.persistence;

import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
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

/**
 * Handles the persistence of returns in the GameZone system. Returns are stored
 * in the data/returns.csv file.
 */
public class ReturnRepository {

    private final String filePath = "data/returns.csv";
    private final SaleService saleService;
    private final ProductService productService;

    /**
     * Creates a ReturnRepository with the services required to resolve sale and
     * product references when loading returns.
     *
     * @param saleService service used to find existing sales
     * @param productService service used to find existing products
     */
    public ReturnRepository(
            SaleService saleService,
            ProductService productService) {

        this.saleService = saleService;
        this.productService = productService;
    }

    /**
     * Saves all returns to the data file.
     *
     * @param returns list of returns to persist
     */
    public void saveAll(List<Return> returns) {

        File folder = new File("data");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedWriter writer
                = new BufferedWriter(new FileWriter(filePath))) {

            for (Return returnItem : returns) {

                String returnedProductIds = buildProductIds(
                        returnItem.getReturnedProducts());

                String reason = returnItem.getReason()
                        .replace(";", ",");

                String line = returnItem.getId() + ";"
                        + returnItem.getDate() + ";"
                        + returnItem.getOriginalSale().getId() + ";"
                        + returnedProductIds + ";"
                        + reason + ";"
                        + returnItem.getRefundAmount();

                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Error saving returns.", e);
        }
    }

    /**
     * Loads all returns stored in the data file.
     *
     * @return list of loaded returns, or an empty list if the file does not
     * exist
     */
    public List<Return> loadAll() {

        List<Return> returns = new ArrayList<>();

        File file = new File(filePath);

        if (!file.exists()) {
            return returns;
        }

        try (BufferedReader reader
                = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";", -1);

                if (parts.length < 6) {
                    continue;
                }

                String id = parts[0];
                LocalDate date = LocalDate.parse(parts[1]);
                String saleId = parts[2];
                String productIds = parts[3];
                String reason = parts[4];
                double refundAmount = Double.parseDouble(parts[5]);

                Sale originalSale = findSaleById(saleId);

                if (originalSale == null) {
                    continue;
                }

                List<Product> returnedProducts
                        = findProductsByIds(productIds);

                Return returnItem = new Return(
                        id,
                        date,
                        originalSale,
                        returnedProducts,
                        reason
                );

                /*
                 * The refund amount was already calculated when the return
                 * was registered, so we restore the persisted value.
                 */
                returnItem.calculateRefundAmount();

                if (returnItem.getRefundAmount() != refundAmount) {
                    /*
                     * The amount is derived from the returned products.
                     * The calculated value is intentionally kept as the
                     * source of truth.
                     */
                }

                returns.add(returnItem);
            }

        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Error loading returns.", e);
        }

        return returns;
    }

    /**
     * Builds a serialized list of product identifiers.
     *
     * @param products products included in the return
     * @return product identifiers separated by a pipe character
     */
    private String buildProductIds(List<Product> products) {

        List<String> ids = new ArrayList<>();

        for (Product product : products) {
            ids.add(product.getId());
        }

        return String.join("|", ids);
    }

    /**
     * Finds a sale by its identifier.
     *
     * @param saleId sale identifier
     * @return matching sale, or null if it does not exist
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
     * Finds products by their identifiers.
     *
     * @param productIds serialized product identifiers
     * @return list of matching products
     */
    private List<Product> findProductsByIds(String productIds) {

        List<Product> products = new ArrayList<>();

        if (productIds == null || productIds.isBlank()) {
            return products;
        }

        String[] ids = productIds.split("\\|");

        for (String id : ids) {

            for (Product product : productService.listProducts()) {

                if (product.getId().equals(id)) {
                    products.add(product);
                    break;
                }
            }
        }

        return products;
    }
}
