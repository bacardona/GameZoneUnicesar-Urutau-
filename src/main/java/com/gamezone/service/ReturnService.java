package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Console;
import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.persistence.ReturnRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Contains the business rules for managing product returns.
 */
public class ReturnService {

    private final ReturnRepository returnRepository;
    private final SaleService saleService;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final WarrantyService warrantyService;
    private final List<Return> returns;

    /**
     * Creates a return service using the required dependencies.
     *
     * @param returnRepository repository used to persist returns
     * @param saleService service used to find existing sales
     * @param productService service used to restore the stock of products
     * @param accessoryService service used to restore the stock of accessories
     * @param warrantyService service used to cancel the warranties of returned
     * consoles
     */
    public ReturnService(
            ReturnRepository returnRepository,
            SaleService saleService,
            ProductService productService,
            AccessoryService accessoryService,
            WarrantyService warrantyService) {

        this.returnRepository = returnRepository;
        this.saleService = saleService;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.warrantyService = warrantyService;
        this.returns = new ArrayList<>(returnRepository.loadAll());
    }

    /**
     * Registers a new return for products belonging to an existing sale.
     *
     * @param saleId identifier of the original sale
     * @param productIds identifiers of the products being returned
     * @param reason reason given for the return
     * @return the newly registered return
     */
    public Return registerReturn(
            String saleId,
            List<String> productIds,
            String reason) {

        if (saleId == null || saleId.isBlank()) {
            throw new IllegalArgumentException(
                    "El identificador de la venta no puede estar vacío.");
        }

        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe indicar al menos un producto para devolver.");
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException(
                    "El motivo de la devolución no puede estar vacío.");
        }

        Sale sale = findSaleById(saleId);

        if (sale == null) {
            throw new IllegalArgumentException(
                    "La venta indicada no existe.");
        }

        if (!sale.canBeReturned()) {
            throw new IllegalArgumentException(
                    "La venta está fuera del plazo de 30 días para devoluciones.");
        }

        List<Product> returnedProducts = new ArrayList<>();

        for (String productId : productIds) {

            Product product = findProductInSale(sale, productId);

            if (product == null) {
                throw new IllegalArgumentException(
                        "El producto " + productId
                        + " no pertenece a la venta indicada.");
            }

            returnedProducts.add(product);
        }

        Return returnItem = new Return(
                UUID.randomUUID().toString(),
                LocalDate.now(),
                sale,
                returnedProducts,
                reason
        );

        returnItem.setWarrantyRefundAmount(
                cancelReturnedWarranties(returnedProducts, saleId));
        returnItem.calculateRefundAmount();

        restoreReturnedStock(returnedProducts);

        returns.add(returnItem);
        returnRepository.saveAll(returns);

        return returnItem;
    }

    /**
     * Returns all registered returns.
     *
     * @return list containing all returns
     */
    public List<Return> viewAllReturns() {
        return new ArrayList<>(returns);
    }

    /**
     * Returns all returns associated with a specific customer.
     *
     * @param customerId customer identifier
     * @return returns belonging to sales made by the customer
     */
    public List<Return> viewReturnsByCustomer(String customerId) {

        List<Return> result = new ArrayList<>();

        for (Return returnItem : returns) {

            Customer customer
                    = returnItem.getOriginalSale().getCustomer();

            if (customer != null
                    && customer.getID().equals(customerId)) {

                result.add(returnItem);
            }
        }

        return result;
    }

    /**
     * Returns all returns associated with a specific sale.
     *
     * @param saleId sale identifier
     * @return returns associated with the sale
     */
    public List<Return> viewReturnsBySale(String saleId) {

        List<Return> result = new ArrayList<>();

        for (Return returnItem : returns) {

            if (returnItem.getOriginalSale()
                    .getId()
                    .equals(saleId)) {

                result.add(returnItem);
            }
        }

        return result;
    }

    /**
     * Calculates the total sold in the given month and year. Each sale counts
     * with its final total: subtotal minus the applied discount plus the cost
     * of the extended warranties.
     *
     * @param month month number from 1 to 12
     * @param year calendar year
     * @return total sales of the month
     */
    public double calculateMonthlySales(int month, int year) {

        validateMonthAndYear(month, year);

        double totalSales = 0.0;

        for (Sale sale : saleService.getAllSales()) {

            if (isDateInMonth(sale.getDate(), month, year)) {
                totalSales += sale.calculateFinalTotal();
            }
        }

        return totalSales;
    }

    /**
     * Calculates the total refunded in the given month and year.
     *
     * @param month month number from 1 to 12
     * @param year calendar year
     * @return total refunded by the returns of the month
     */
    public double calculateMonthlyReturns(int month, int year) {

        validateMonthAndYear(month, year);

        double totalReturns = 0.0;

        for (Return returnItem : returns) {

            LocalDate returnDate = returnItem.getDate();

            if (returnDate.getMonthValue() == month
                    && returnDate.getYear() == year) {

                totalReturns += returnItem.getRefundAmount();
            }
        }

        return totalReturns;
    }

    /**
     * Calculates the monthly net balance: total sales of the month minus the
     * total refunded in the same month.
     *
     * @param month month number from 1 to 12
     * @param year calendar year
     * @return net balance for the specified month
     */
    public double generateMonthlyBalance(int month, int year) {

        return calculateMonthlySales(month, year)
                - calculateMonthlyReturns(month, year);
    }

    /**
     * Validates the month and year used by the monthly report.
     *
     * @param month month number from 1 to 12
     * @param year calendar year
     */
    private void validateMonthAndYear(int month, int year) {

        if (month < 1 || month > 12) {
            throw new IllegalArgumentException(
                    "El mes debe estar entre 1 y 12.");
        }

        if (year <= 0) {
            throw new IllegalArgumentException(
                    "El año debe ser válido.");
        }
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
     * Finds a product with the given identifier inside a specific sale.
     *
     * @param sale original sale
     * @param productId product identifier
     * @return matching product, or null if it is not part of the sale
     */
    private Product findProductInSale(
            Sale sale,
            String productId) {

        for (Product product : sale.getProducts()) {

            if (product.getId().equals(productId)) {
                return product;
            }
        }

        return null;
    }

    /**
     * Cancels the warranties of every returned console. A returned console
     * cannot keep an active warranty, and the cost of a cancelled extended
     * warranty is refunded to the customer.
     *
     * @param returnedItems products and accessories being returned
     * @param saleId identifier of the original sale
     * @return the total amount to refund for the cancelled warranties
     */
    private double cancelReturnedWarranties(
            List<Product> returnedItems,
            String saleId) {

        double warrantyRefund = 0.0;

        for (Product item : returnedItems) {

            if (item instanceof Console) {
                warrantyRefund += warrantyService.cancelWarranties(
                        item.getId(), saleId);
            }
        }

        return warrantyRefund;
    }

    /**
     * Restores the stock of every returned item, delegating to the service
     * that owns its real type. The Accessory check goes first because every
     * Accessory is also a Product.
     *
     * @param returnedItems products and accessories being returned
     */
    private void restoreReturnedStock(List<Product> returnedItems) {

        for (Product item : returnedItems) {

            if (item instanceof Accessory) {
                accessoryService.restoreStock(item.getId(), 1);
            } else {
                productService.restoreStock(item.getId(), 1);
            }
        }
    }

    /**
     * Checks whether a Date belongs to a specific month and year.
     *
     * @param date date to evaluate
     * @param month target month
     * @param year target year
     * @return true if the date belongs to the requested month and year
     */
    private boolean isDateInMonth(
            Date date,
            int month,
            int year) {

        LocalDate localDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return localDate.getMonthValue() == month
                && localDate.getYear() == year;
    }
}
