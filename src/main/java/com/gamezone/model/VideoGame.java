
package com.gamezone.model;


/**
 * Represents a video game product.
 */
public class VideoGame extends Product {

    // Atributos propios de VideoGame
    private String platform;
    private String genre;
    private String ageRating;

    /**
     * Creates a new VideoGame.
     *
     * @param id        unique identifier
     * @param title     game title
     * @param price     unit price
     * @param quantity  quantity available in stock
     * @param platform  platform the game runs on
     * @param genre     genre of the game
     * @param ageRating recommended age rating
     */
    public VideoGame(String id, String title, double price, int quantity,
                      String platform, String genre, String ageRating) {
        // Llamamos al constructor del padre para los atributos comunes
        super(id, title, price, quantity);
        this.platform = platform;
        this.genre = genre;
        this.ageRating = ageRating;
    }
}
