package com.gamezone.service;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.Console;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.WarrantyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Provides business logic for managing product warranties. Stored warranties
 * only hold sale and product identifiers, so this service resolves them into
 * real Sale and Product objects when loading.
 */
public class WarrantyService {

    private final WarrantyRepository warrantyRepository;
    private final SaleRepository saleRepository;
    private final ProductService productService;
    private final List<Warranty> warranties;

    /**
     * Creates a WarrantyService and loads stored warranties. The sale
     * repository must have loaded its sales before this service is created.
     *
     * @param warrantyRepository repository used to persist warranties
     * @param saleRepository repository used to resolve sales by identifier
     * @param productService service used to resolve products by identifier
     */
    public WarrantyService(
            WarrantyRepository warrantyRepository,
            SaleRepository saleRepository,
            ProductService productService) {

        this.warrantyRepository = warrantyRepository;
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.warranties = new ArrayList<>();

        loadStoredWarranties();
    }

    /**
     * Assigns a basic warranty to a console. Basic warranties last six months
     * and have no additional cost.
     *
     * @param product covered product
     * @param sale associated sale
     * @param startDate warranty start date
     * @return the created basic warranty
     */
    public BasicWarranty assignBasicWarranty(
            Product product, Sale sale, LocalDate startDate) {

        validateWarrantyData(product, sale, startDate);

        if (!(product instanceof Console)) {
            throw new IllegalArgumentException(
                    "Basic warranties are only available for consoles.");
        }

        BasicWarranty warranty = new BasicWarranty(
                UUID.randomUUID().toString(), product, sale, startDate);

        warranties.add(warranty);
        warrantyRepository.saveAll(warranties);

        return warranty;
    }

    /**
     * Assigns an extended warranty to a console. Extended warranties last
     * twelve months and cost ten percent of the covered product price.
     *
     * @param product covered product
     * @param sale associated sale
     * @param startDate warranty start date
     * @return the created extended warranty
     */
    public ExtendedWarranty assignExtendedWarranty(
            Product product, Sale sale, LocalDate startDate) {

        validateWarrantyData(product, sale, startDate);

        if (!(product instanceof Console)) {
            throw new IllegalArgumentException(
                    "Extended warranties are only available for consoles.");
        }

        ExtendedWarranty warranty = new ExtendedWarranty(
                UUID.randomUUID().toString(), product, sale, startDate);

        warranties.add(warranty);
        warrantyRepository.saveAll(warranties);

        return warranty;
    }

    /**
     * Finds a warranty by product and sale identifiers.
     *
     * @param productId product identifier
     * @param saleId sale identifier
     * @return the matching warranty, or null if not found
     */
    public Warranty findWarrantyByProduct(String productId, String saleId) {
        for (Warranty warranty : warranties) {
            if (warranty.getProduct().getId().equals(productId)
                    && warranty.getSale().getId().equals(saleId)) {
                return warranty;
            }
        }

        return null;
    }

    /**
     * Returns all registered warranties.
     *
     * @return a copy of the registered warranties
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

            if (!endDate.isBefore(today)
                    && !endDate.isAfter(limitDate)) {
                result.add(warranty);
            }
        }

        return result;
    }

    /**
     * Cancels the warranties of one returned unit of a product in a sale: at
     * most one basic and one extended warranty are removed, so returning one of
     * two identical consoles keeps the warranties of the other one. Returns the
     * refundable cost: zero for a basic warranty and the additional cost for an
     * extended one.
     *
     * @param productId identifier of the returned product
     * @param saleId identifier of the sale the product belongs to
     * @return the amount to refund for the cancelled warranties
     */
    public double cancelWarranties(String productId, String saleId) {
        Warranty basic = null;
        Warranty extended = null;

        for (Warranty warranty : warranties) {
            boolean matches = warranty.getProduct().getId().equals(productId)
                    && warranty.getSale().getId().equals(saleId);

            if (matches && basic == null && warranty instanceof BasicWarranty) {
                basic = warranty;
            } else if (matches && extended == null
                    && warranty instanceof ExtendedWarranty) {
                extended = warranty;
            }
        }

        double refundableCost = 0.0;

        if (basic != null) {
            refundableCost += basic.getAdditionalCost();
            warranties.remove(basic);
        }

        if (extended != null) {
            refundableCost += extended.getAdditionalCost();
            warranties.remove(extended);
        }

        if (basic != null || extended != null) {
            warrantyRepository.saveAll(warranties);
        }

        return refundableCost;
    }

    /**
     * Loads the stored warranty records and resolves their sale and product
     * identifiers into real objects. Records with an invalid date or with
     * references that can no longer be resolved are skipped.
     */
    private void loadStoredWarranties() {
        for (String[] record : warrantyRepository.loadAll()) {
            String type = record[0];
            String id = record[1];
            String productId = record[2];
            String saleId = record[3];

            LocalDate startDate;
            try {
                startDate = LocalDate.parse(record[4]);
            } catch (RuntimeException e) {
                continue;
            }

            Sale sale = saleRepository.findById(saleId);
            Product product = findProductById(productId);

            if (sale == null || product == null) {
                continue;
            }

            Warranty warranty = warrantyRepository.createWarranty(
                    type, id, product, sale, startDate);

            if (warranty != null) {
                warranties.add(warranty);
            }
        }
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

    /**
     * Validates the common data required to create a warranty.
     *
     * @param product covered product
     * @param sale associated sale
     * @param startDate warranty start date
     */
    private void validateWarrantyData(
            Product product, Sale sale, LocalDate startDate) {

        if (product == null || sale == null || startDate == null) {
            throw new IllegalArgumentException(
                    "Product, sale, and start date are required.");
        }
    }
}
