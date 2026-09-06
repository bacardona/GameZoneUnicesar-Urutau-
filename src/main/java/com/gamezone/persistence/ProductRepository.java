
package com.gamezone.persistence;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading Product objects to and from a text file.
 */
public class ProductRepository {

    // Ruta del archivo donde se guardan los productos
    private String filePath = "data/products.txt";

    /**
     * Saves the given list of products to the data file.
     *
     * @param products list of products to persist
     */
    public void save(List<Product> products) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            // Recorremos la lista y escribimos una línea por cada producto
            for (int i = 0; i < products.size(); i++) {
                Product producto = products.get(i);
                String linea = "";

                if (producto instanceof VideoGame) {
                    VideoGame vg = (VideoGame) producto;
                    linea = "VG;" + vg.getId() + ";" + vg.getTitle() + ";"
                            + vg.getPrice() + ";" + vg.getQuantity() + ";"
                            + vg.getPlatform() + ";" + vg.getGenre() + ";" + vg.getAgeRating();
                } else if (producto instanceof Console) {
                    Console c = (Console) producto;
                    linea = "CO;" + c.getId() + ";" + c.getTitle() + ";"
                            + c.getPrice() + ";" + c.getQuantity() + ";"
                            + c.getBrand() + ";" + c.getModel() + ";" + c.getGeneration();
                }

                writer.write(linea);
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Error al guardar los productos: " + e.getMessage());
        }
    }
    
}