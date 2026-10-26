
package com.gamezone.service;

import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.validation.ProductValidator;

import java.util.List;

/**
 * Contains the business rules for managing products.
 */
public class ProductService {

    private ProductRepository productRepository;
    private List<Product> products;

    /**
     * Creates a ProductService and loads the currently stored products.
     */
    public ProductService() {
        this.productRepository = new ProductRepository();
        this.products = productRepository.load();
    }

    /**
     * Registers a new video game and persists the updated inventory.
     *
     * @param id        unique identifier
     * @param title     game title
     * @param price     unit price
     * @param quantity  initial stock quantity
     * @param platform  platform the game runs on
     * @param genre     genre of the game
     * @param ageRating recommended age rating
     * @throws com.gamezone.exceptions.InvalidDataException if the id, title,
     *         price or quantity are not valid
     */
    public void registerVideoGame(String id, String title, double price, int quantity,
                                   String platform, String genre, String ageRating) {
        ProductValidator.validateProductData(id, title, price, quantity);
        // Creamos el videojuego y lo agregamos a la lista en memoria
        VideoGame newVideoGame = new VideoGame(id, title, price, quantity, platform, genre, ageRating);
        products.add(newVideoGame);
        // Guardamos inmediatamente el cambio en el archivo
        productRepository.save(products);
    }

    /**
     * Registers a new console and persists the updated inventory.
     *
     * @param id         unique identifier
     * @param title      console title
     * @param price      unit price
     * @param quantity   initial stock quantity
     * @param brand      manufacturer brand
     * @param model      specific model
     * @param generation hardware generation
     * @throws com.gamezone.exceptions.InvalidDataException if the id, title,
     *         price or quantity are not valid
     */
    public void registerConsole(String id, String title, double price, int quantity,
                                 String brand, String model, String generation) {
        ProductValidator.validateProductData(id, title, price, quantity);
        Console newConsole = new Console(id, title, price, quantity, brand, model, generation);
        products.add(newConsole);
        productRepository.save(products);
    }
    
    /**
     * Returns the full list of products currently in inventory.
     *
     * @return list of all products
     */
    public List<Product> listProducts() {
        return products;
    }
    
    /**
     * Finds a product by its identifier.
     *
     * @param productId id of the product to search for
     * @return the matching product, or null if not found
     */
    public Product findById(String productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    /**
     * Reduces the stock of a product after a sale.
     *
     * @param productId    id of the product being sold
     * @param quantitySold quantity being sold
     * @throws com.gamezone.exceptions.ResourceNotFoundException if the product
     *         does not exist
     * @throws com.gamezone.exceptions.BusinessRuleException if there is not
     *         enough stock
     */
    public void updateStock(String productId, int quantitySold) {
        Product product = findById(productId);
        ProductValidator.validateProductExists(product, productId);
        ProductValidator.validateStockAvailability(product, quantitySold);

        product.setQuantity(product.getQuantity() - quantitySold);
        productRepository.save(products);
    }
    /**
     * Increases the stock of a product, used when a return is registered.
     *
     * @param productId id of the product being restored
     * @param quantity  quantity to add back to the stock
     * @throws com.gamezone.exceptions.InvalidDataException if the quantity is
     *         not positive
     * @throws com.gamezone.exceptions.ResourceNotFoundException if the product
     *         does not exist
     */
    public void restoreStock(String productId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidDataException("cantidad", "debe ser mayor que cero");
        }
        Product product = findById(productId);
        ProductValidator.validateProductExists(product, productId);

        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(products);
    }
}

