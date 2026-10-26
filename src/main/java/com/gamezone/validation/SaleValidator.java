package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;

import java.util.List;

/**
 * Validation rules for sales. Services call these static methods before
 * running any business operation. This class only depends on the model and
 * the exceptions: it never calls services or repositories and never touches
 * files.
 */
public final class SaleValidator {

    private SaleValidator() {
        // Utility class: not meant to be instantiated.
    }

    /**
     * Validates the data needed to register a sale.
     *
     * @param customer the customer making the purchase, must not be null
     * @param seller   the seller attending the sale, must not be null
     * @param products the items of the sale, must not be null or empty
     * @throws InvalidDataException  if the customer or the seller is null
     * @throws BusinessRuleException if the sale has no products
     */
    public static void validateSaleData(Customer customer, Seller seller, List<Product> products) {
        if (customer == null) {
            throw new InvalidDataException("cliente", "es obligatorio");
        }
        if (seller == null) {
            throw new InvalidDataException("vendedor", "es obligatorio");
        }
        if (products == null || products.isEmpty()) {
            throw new BusinessRuleException("La venta debe contener al menos un producto.");
        }
    }

    /**
     * Validates that a sale was found.
     *
     * @param sale   the result of a sale lookup
     * @param saleId the identifier that was searched
     * @throws ResourceNotFoundException if the sale is null
     */
    public static void validateSaleExists(Sale sale, String saleId) {
        if (sale == null) {
            throw new ResourceNotFoundException("venta", saleId);
        }
    }
}