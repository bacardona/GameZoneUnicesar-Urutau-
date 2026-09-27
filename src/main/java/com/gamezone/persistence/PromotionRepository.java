package com.gamezone.persistence;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;

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
 * Handles the persistence of promotions in the GameZone system.
 * Promotions are stored in the data/promotions.csv file.
 */
public class PromotionRepository {

    private final String filePath = "data/promotions.csv";

    /**
     * Saves all promotions to the data file.
     *
     * @param promotions list of promotions to persist
     */
    public void saveAll(List<Promotion> promotions) {

        File folder = new File("data");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (Promotion promotion : promotions) {

                String line = "";

                if (promotion instanceof PercentageDiscount) {

                    PercentageDiscount percentageDiscount =
                            (PercentageDiscount) promotion;

                    line = "PERCENTAGE;"
                            + percentageDiscount.getId() + ";"
                            + percentageDiscount.getName() + ";"
                            + percentageDiscount.getStartDate() + ";"
                            + percentageDiscount.getEndDate() + ";"
                            + percentageDiscount.getPercentage();

                } else if (promotion instanceof CategoryDiscount) {

                    CategoryDiscount categoryDiscount =
                            (CategoryDiscount) promotion;

                    line = "CATEGORY;"
                            + categoryDiscount.getId() + ";"
                            + categoryDiscount.getName() + ";"
                            + categoryDiscount.getStartDate() + ";"
                            + categoryDiscount.getEndDate() + ";"
                            + categoryDiscount.getPercentage() + ";"
                            + categoryDiscount.getTargetCategory();

                } else if (promotion instanceof BulkPurchaseDiscount) {

                    BulkPurchaseDiscount bulkPurchaseDiscount =
                            (BulkPurchaseDiscount) promotion;

                    line = "BULK;"
                            + bulkPurchaseDiscount.getId() + ";"
                            + bulkPurchaseDiscount.getName() + ";"
                            + bulkPurchaseDiscount.getStartDate() + ";"
                            + bulkPurchaseDiscount.getEndDate() + ";"
                            + bulkPurchaseDiscount.getMinQuantity() + ";"
                            + bulkPurchaseDiscount.getPercentage();
                }

                if (!line.isEmpty()) {
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error saving promotions.", e);
        }
    }

    /**
     * Loads all promotions stored in the data file.
     *
     * @return list of loaded promotions, or an empty list if the file does not exist
     */
    public List<Promotion> loadAll() {

        List<Promotion> promotions = new ArrayList<>();

        File file = new File(filePath);

        if (!file.exists()) {
            return promotions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";", -1);

                String type = parts[0];
                String id = parts[1];
                String name = parts[2];
                LocalDate startDate = LocalDate.parse(parts[3]);
                LocalDate endDate = LocalDate.parse(parts[4]);

                Promotion promotion = null;

                if (type.equals("PERCENTAGE")) {

                    double percentage = Double.parseDouble(parts[5]);

                    promotion = new PercentageDiscount(
                            id,
                            name,
                            startDate,
                            endDate,
                            percentage
                    );

                } else if (type.equals("CATEGORY")) {

                    double percentage = Double.parseDouble(parts[5]);
                    String targetCategory = parts[6];

                    promotion = new CategoryDiscount(
                            id,
                            name,
                            startDate,
                            endDate,
                            percentage,
                            targetCategory
                    );

                } else if (type.equals("BULK")) {

                    int minQuantity = Integer.parseInt(parts[5]);
                    double percentage = Double.parseDouble(parts[6]);

                    promotion = new BulkPurchaseDiscount(
                            id,
                            name,
                            startDate,
                            endDate,
                            minQuantity,
                            percentage
                    );
                }

                if (promotion != null) {
                    promotions.add(promotion);
                }
            }

        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Error loading promotions.", e);
        }

        return promotions;
    }
}
