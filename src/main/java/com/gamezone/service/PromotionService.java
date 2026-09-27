package com.gamezone.service;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.persistence.PromotionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the business rules for managing promotions.
 */
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final List<Promotion> promotions;

    /**
     * Creates a promotion service using the given repository.
     *
     * @param promotionRepository repository used to persist promotions
     */
    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
        this.promotions = promotionRepository.loadAll();
    }

    /**
     * Registers a new percentage discount promotion.
     *
     * @param id unique identifier
     * @param name promotion name
     * @param startDate promotion start date
     * @param endDate promotion end date
     * @param percentage discount percentage
     */
    public void registerPercentageDiscount(
            String id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            double percentage) {

        PercentageDiscount promotion = new PercentageDiscount(
                id,
                name,
                startDate,
                endDate,
                percentage
        );

        validateNewPromotion(promotion);

        promotions.add(promotion);
        promotionRepository.saveAll(promotions);
    }

    /**
     * Registers a new category discount promotion.
     *
     * @param id unique identifier
     * @param name promotion name
     * @param startDate promotion start date
     * @param endDate promotion end date
     * @param percentage discount percentage
     * @param targetCategory target product category
     */
    public void registerCategoryDiscount(
            String id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            double percentage,
            String targetCategory) {

        CategoryDiscount promotion = new CategoryDiscount(
                id,
                name,
                startDate,
                endDate,
                percentage,
                targetCategory
        );

        validateNewPromotion(promotion);

        if (!targetCategory.equalsIgnoreCase("VIDEOGAME")
                && !targetCategory.equalsIgnoreCase("CONSOLE")) {

            throw new IllegalArgumentException(
                    "Target category must be VIDEOGAME or CONSOLE.");
        }

        promotions.add(promotion);
        promotionRepository.saveAll(promotions);
    }

    /**
     * Registers a new bulk purchase discount promotion.
     *
     * @param id unique identifier
     * @param name promotion name
     * @param startDate promotion start date
     * @param endDate promotion end date
     * @param minQuantity minimum number of products required
     * @param percentage discount percentage
     */
    public void registerBulkPurchaseDiscount(
            String id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            int minQuantity,
            double percentage) {

        BulkPurchaseDiscount promotion = new BulkPurchaseDiscount(
                id,
                name,
                startDate,
                endDate,
                minQuantity,
                percentage
        );

        validateNewPromotion(promotion);

        if (minQuantity <= 0) {
            throw new IllegalArgumentException(
                    "Minimum quantity must be greater than zero.");
        }

        promotions.add(promotion);
        promotionRepository.saveAll(promotions);
    }

    /**
     * Returns all registered promotions.
     *
     * @return list containing all promotions
     */
    public List<Promotion> listAllPromotions() {
        return new ArrayList<>(promotions);
    }

    /**
     * Returns the promotions that are active on the current date.
     *
     * @return list of active promotions
     */
    public List<Promotion> listActivePromotions() {

        List<Promotion> activePromotions = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (Promotion promotion : promotions) {

            if (promotion.isActive(today)) {
                activePromotions.add(promotion);
            }
        }

        return activePromotions;
    }

    /**
     * Finds the active promotion that provides the highest discount for the
     * given sale.
     *
     * @param sale sale to evaluate
     * @return the promotion with the highest applicable discount, or null if no
     * promotion applies
     */
    public Promotion findBestPromotionFor(Sale sale) {

        if (sale == null) {
            throw new IllegalArgumentException(
                    "Sale cannot be null.");
        }

        Promotion bestPromotion = null;
        double highestDiscount = 0.0;

        LocalDate today = LocalDate.now();

        for (Promotion promotion : promotions) {

            if (!promotion.isActive(today)) {
                continue;
            }

            double discount = promotion.calculateDiscount(sale);

            if (discount > highestDiscount) {
                highestDiscount = discount;
                bestPromotion = promotion;
            }
        }

        return bestPromotion;
    }

    /**
     * Finds a promotion by its identifier.
     *
     * @param id promotion identifier
     * @return the promotion with the specified identifier, or null if it does
     * not exist
     */
    public Promotion findById(String id) {

        for (Promotion promotion : promotions) {

            if (promotion.getId().equals(id)) {
                return promotion;
            }
        }

        return null;
    }

    /**
     * Validates the common information of a new promotion.
     *
     * @param promotion promotion to validate
     */
    private void validateNewPromotion(Promotion promotion) {

        if (promotion == null) {
            throw new IllegalArgumentException(
                    "Promotion cannot be null.");
        }

        if (promotion.getId() == null
                || promotion.getId().isBlank()) {

            throw new IllegalArgumentException(
                    "Promotion ID cannot be empty.");
        }

        if (promotion.getName() == null
                || promotion.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Promotion name cannot be empty.");
        }

        if (promotion.getStartDate() == null
                || promotion.getEndDate() == null) {

            throw new IllegalArgumentException(
                    "Promotion dates cannot be null.");
        }

        if (promotion.getEndDate().isBefore(promotion.getStartDate())) {

            throw new IllegalArgumentException(
                    "End date cannot be before start date.");
        }

        if (findById(promotion.getId()) != null) {

            throw new IllegalArgumentException(
                    "Promotion ID already exists.");
        }

        double percentage = 0.0;

        if (promotion instanceof PercentageDiscount) {

            percentage = ((PercentageDiscount) promotion).getPercentage();

        } else if (promotion instanceof CategoryDiscount) {

            percentage = ((CategoryDiscount) promotion).getPercentage();

        } else if (promotion instanceof BulkPurchaseDiscount) {

            percentage = ((BulkPurchaseDiscount) promotion).getPercentage();
        }

        if (percentage < 0 || percentage > 100) {

            throw new IllegalArgumentException(
                    "Discount percentage must be between 0 and 100.");
        }
    }
}
