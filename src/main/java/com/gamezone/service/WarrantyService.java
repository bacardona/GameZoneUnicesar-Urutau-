package com.gamezone.service;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.Console;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.persistence.WarrantyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Provides business logic for managing product warranties.
 */
public class WarrantyService {

    private final WarrantyRepository warrantyRepository;
    private final List<Warranty> warranties;

    /**
     * Creates a WarrantyService.
     *
     * @param warrantyRepository repository used to persist warranties
     */
    public WarrantyService(WarrantyRepository warrantyRepository) {
        this.warrantyRepository = warrantyRepository;
        this.warranties = new ArrayList<>(warrantyRepository.loadAll());
    }

    /**
     * Assigns a basic warranty to a console. Basic warranties last six months
     * and have no additional cost.
     *
     * @param product product covered by the warranty
     * @param sale sale associated with the warranty
     * @param startDate warranty start date
     * @return the created basic warranty
     */
    public BasicWarranty assignBasicWarranty(Product product,
            Sale sale,
            LocalDate startDate) {

        validateWarrantyData(product, sale, startDate);

        if (!(product instanceof Console)) {
            throw new IllegalArgumentException(
                    "Basic warranties are only available for consoles.");
        }

        BasicWarranty warranty = new BasicWarranty(
                UUID.randomUUID().toString(),
                product,
                sale,
                startDate
        );

        warranties.add(warranty);
        warrantyRepository.saveAll(warranties);

        return warranty;
    }

    /**
     * Assigns an extended warranty to a console. Extended warranties last
     * twelve months and cost ten percent of the covered product price.
     *
     * @param product product covered by the warranty
     * @param sale sale associated with the warranty
     * @param startDate warranty start date
     * @return the created extended warranty
     */
    public ExtendedWarranty assignExtendedWarranty(Product product,
            Sale sale,
            LocalDate startDate) {

        validateWarrantyData(product, sale, startDate);

        if (!(product instanceof Console)) {
            throw new IllegalArgumentException(
                    "Extended warranties are only available for consoles.");
        }

        ExtendedWarranty warranty = new ExtendedWarranty(
                UUID.randomUUID().toString(),
                product,
                sale,
                startDate
        );

        warranties.add(warranty);
        warrantyRepository.saveAll(warranties);

        return warranty;
    }

    /**
     * Finds the warranty associated with a product in a specific sale.
     *
     * @param productId product identifier
     * @param saleId sale identifier
     * @return the matching warranty, or null if no warranty exists
     */
    public Warranty findWarrantyByProduct(String productId, String saleId) {

        for (Warranty warranty : warranties) {

            boolean sameProduct
                    = warranty.getProduct().getId().equals(productId);

            boolean sameSale
                    = warranty.getSale().getId().equals(saleId);

            if (sameProduct && sameSale) {
                return warranty;
            }
        }

        return null;
    }

    /**
     * Returns all registered warranties.
     *
     * @return a copy of all registered warranties
     */
    public List<Warranty> listAllWarranties() {
        return new ArrayList<>(warranties);
    }

    /**
     * Returns warranties that are active on the current date.
     *
     * @return the currently active warranties
     */
    public List<Warranty> listActiveWarranties() {

        List<Warranty> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Warranty warranty : warranties) {

            if (warranty.isActive(today)) {
                result.add(warranty);
            }
        }

        return result;
    }

    /**
     * Returns warranties whose expiration date is within the next specified
     * number of days.
     *
     * @param daysAhead number of days to look ahead
     * @return warranties expiring within the requested period
     */
    public List<Warranty> listWarrantiesExpiringSoon(int daysAhead) {

        if (daysAhead < 0) {
            throw new IllegalArgumentException(
                    "The number of days cannot be negative.");
        }

        List<Warranty> result = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate limitDate = today.plusDays(daysAhead);

        for (Warranty warranty : warranties) {

            LocalDate endDate = warranty.getEndDate();

            boolean expiresSoon
                    = !endDate.isBefore(today)
                    && !endDate.isAfter(limitDate);

            if (expiresSoon) {
                result.add(warranty);
            }
        }

        return result;
    }

    /**
     * Validates the common data required to create a warranty.
     *
     * @param product product covered by the warranty
     * @param sale sale associated with the warranty
     * @param startDate warranty start date
     */
    private void validateWarrantyData(Product product,
            Sale sale,
            LocalDate startDate) {

        if (product == null) {
            throw new IllegalArgumentException(
                    "The product cannot be null.");
        }

        if (sale == null) {
            throw new IllegalArgumentException(
                    "The sale cannot be null.");
        }

        if (startDate == null) {
            throw new IllegalArgumentException(
                    "The start date cannot be null.");
        }
    }
}
