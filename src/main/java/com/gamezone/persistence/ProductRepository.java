
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
                Product product = products.get(i);
                String line = "";

                if (product instanceof VideoGame) {
                    VideoGame vg = (VideoGame) product;
                    line = "VG;" + vg.getId() + ";" + vg.getTitle() + ";"
                            + vg.getPrice() + ";" + vg.getQuantity() + ";"
                            + vg.getPlatform() + ";" + vg.getGenre() + ";" + vg.getAgeRating();
                } else if (product instanceof Console) {
                    Console c = (Console) product;
                    line = "CO;" + c.getId() + ";" + c.getTitle() + ";"
                            + c.getPrice() + ";" + c.getQuantity() + ";"
                            + c.getBrand() + ";" + c.getModel() + ";" + c.getGeneration();
                }

                writer.write(line);
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }
    
    /**
     * Loads all products previously saved in the data file.
     *
     * @return list of products, or an empty list if the file does not exist
     */
    public List<Product> load() {
        List<Product> productList = new ArrayList<>();
        File file = new File(filePath);

        // Si el archivo aún no existe, devolvemos la lista vacía
        if (!file.exists()) {
            return productList;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String linea = reader.readLine();

            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    String[] parts = linea.split(";");
                    String type = parts[0];
                    String id = parts[1];
                    String title = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    int quantity = Integer.parseInt(parts[4]);

                    if (type.equals("VG")) {
                        productList.add(new VideoGame(id, title, price, quantity,
                                parts[5], parts[6], parts[7]));
                    } else if (type.equals("CO")) {
                        productList.add(new Console(id, title, price, quantity,
                                parts[5], parts[6], parts[7]));
                    }
                }
                linea = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading products: " + e.getMessage());
        }

        return productList;
    }
}
