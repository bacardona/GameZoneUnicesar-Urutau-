package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Product;

/**
 * Validation rules for products and accessories. Services call these static
 * methods before running any business operation. This class only depends on
 * the model and the exceptions: it never calls services or repositories and
 * never touches files.
 */
public final class ProductValidator {

    private ProductValidator() {
        // Utility class: not meant to be instantiated.
    }

    /**
     * Validates the basic data of a product.
     *
     * @param id    product identifier, must not be null or blank
     * @param title product title, must not be null or blank
     * @param price unit price, must be greater than zero
     * @param stock units in stock, must not be negative
     * @throws InvalidDataException if any value is invalid
     */
    public static void validateProductData(String id, String title, double price, int stock) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("identificador", "no puede estar vacío");
        }
        if (title == null || title.isBlank()) {
            throw new InvalidDataException("título", "no puede estar vacío");
        }
        if (price <= 0) {
            throw new InvalidDataException("precio", "debe ser mayor que cero");
        }
        if (stock < 0) {
            throw new InvalidDataException("stock", "no puede ser negativo");
        }
    }

    /**
     * Validates that a product has enough stock for the requested quantity.
     *
     * @param product           the product to check
     * @param requestedQuantity units that are going to be taken from stock
     * @throws BusinessRuleException if the available stock is lower than the
     *                               requested quantity
     */
    public static void validateStockAvailability(Product product, int requestedQuantity) {
        if (product.getQuantity() < requestedQuantity) {
            throw new BusinessRuleException("Stock insuficiente para: " + product.getTitle()
                    + " (disponible: " + product.getQuantity()
                    + ", solicitado: " + requestedQuantity + ").");
        }
    }

    /**
     * Validates that a product was found.
     *
     * @param product   the result of a product lookup
     * @param productId the identifier that was searched
     * @throws ResourceNotFoundException if the product is null
     */
    public static void validateProductExists(Product product, String productId) {
        if (product == null) {
            throw new ResourceNotFoundException("producto", productId);
        }
    }
}