
package com.gamezone.service;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.persistence.ProductRepository;

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
     */
    public void registerVideoGame(String id, String title, double price, int quantity,
                                   String platform, String genre, String ageRating) {
        // Creamos el videojuego y lo agregamos a la lista en memoria
        VideoGame nuevo = new VideoGame(id, title, price, quantity, platform, genre, ageRating);
        products.add(nuevo);
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
     */
    public void registerConsole(String id, String title, double price, int quantity,
                                 String brand, String model, String generation) {
        Console nueva = new Console(id, title, price, quantity, brand, model, generation);
        products.add(nueva);
        productRepository.save(products);
    }
    
}

