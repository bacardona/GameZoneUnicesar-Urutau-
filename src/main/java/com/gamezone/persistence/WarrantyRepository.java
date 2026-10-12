package com.gamezone.persistence;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;

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
 * Handles the persistence of warranties in the GameZone system. Only the
 * identifiers of the sale and the product are stored; resolving them into
 * objects is the responsibility of the service layer.
 */
public class WarrantyRepository {

    private static final String FILE_PATH = "data/warranties.csv";

    /**
     * Stores all warranties in the CSV file, overwriting previous content.
     *
     * @param warranties warranties to save
     */
    public void saveAll(List<Warranty> warranties) {
        File file = new File(FILE_PATH);
        File folder = file.getParentFile();

        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedWriter writer
                = new BufferedWriter(new FileWriter(file))) {

            for (Warranty warranty : warranties) {
                String type;

                if (warranty instanceof BasicWarranty) {
                    type = "BASIC";
                } else if (warranty instanceof ExtendedWarranty) {
                    type = "EXTENDED";
                } else {
                    continue;
                }

                String line = type + ";"
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
     * Reads the raw warranty records from the CSV file. Each record contains,
     * in order: type, warranty ID, product ID, sale ID and start date.
     *
     * @return the stored records, or an empty list if the file does not exist
     */
    public List<String[]> loadAll() {
        List<String[]> records = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return records;
        }

        try (BufferedReader reader
                = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split(";", -1);

                if (parts.length == 5) {
                    records.add(parts);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error loading warranties.", e);
        }

        return records;
    }

    /**
     * Rebuilds a warranty using already resolved references.
     *
     * @param type warranty type ("BASIC" or "EXTENDED")
     * @param id warranty identifier
     * @param product covered product
     * @param sale associated sale
     * @param startDate warranty start date
     * @return the reconstructed warranty, or null if the type is unknown
     */
    public Warranty createWarranty(
            String type,
            String id,
            Product product,
            Sale sale,
            LocalDate startDate) {

        if ("BASIC".equalsIgnoreCase(type)) {
            return new BasicWarranty(id, product, sale, startDate);
        }

        if ("EXTENDED".equalsIgnoreCase(type)) {
            return new ExtendedWarranty(id, product, sale, startDate);
        }

        return null;
    }
}

