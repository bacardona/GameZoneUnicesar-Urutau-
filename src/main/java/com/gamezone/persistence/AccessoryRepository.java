package com.gamezone.persistence;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the persistence of accessories in the GameZone system. Accessories
 * are stored in the data/accessories.csv file.
 */
public class AccessoryRepository {

    private final String filePath = "data/accessories.csv";

    /**
     * Saves all accessories to the data file.
     *
     * @param accessories list of accessories to persist
     */
    public void saveAll(List<Accessory> accessories) {

        File folder = new File("data");

        // Crea la carpeta data si todavía no existe.
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (Accessory accessory : accessories) {

                String line = "";

                // Guarda los IDs de las consolas compatibles separados por |.
                String compatibleIds = String.join(
                        "|",
                        accessory.getCompatibleConsoleIds()
                );

                if (accessory instanceof Controller) {

                    Controller controller = (Controller) accessory;

                    line = "CONTROLLER;"
                            + controller.getId() + ";"
                            + controller.getTitle() + ";"
                            + controller.getPrice() + ";"
                            + controller.getQuantity() + ";"
                            + controller.getConnectionType() + ";"
                            + compatibleIds;

                } else if (accessory instanceof Cable) {

                    Cable cable = (Cable) accessory;

                    line = "CABLE;"
                            + cable.getId() + ";"
                            + cable.getTitle() + ";"
                            + cable.getPrice() + ";"
                            + cable.getQuantity() + ";"
                            + cable.getMeters() + ";"
                            + cable.getType() + ";"
                            + compatibleIds;

                } else if (accessory instanceof Memory) {

                    Memory memory = (Memory) accessory;

                    line = "MEMORY;"
                            + memory.getId() + ";"
                            + memory.getTitle() + ";"
                            + memory.getPrice() + ";"
                            + memory.getQuantity() + ";"
                            + memory.getCapacityGb() + ";"
                            + memory.getMemoryType() + ";"
                            + compatibleIds;
                }

                if (!line.isEmpty()) {
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error saving accessories.", e);
        }
    }

    /**
     * Loads all accessories stored in the data file.
     *
     * @return list of loaded accessories, or an empty list if the file does not
     * exist
     */
    public List<Accessory> loadAll() {

        List<Accessory> accessories = new ArrayList<>();

        File file = new File(filePath);

        // Si el archivo no existe, se devuelve una lista vacía.
        if (!file.exists()) {
            return accessories;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";", -1);

                String type = parts[0];
                String id = parts[1];
                String title = parts[2];
                double price = Double.parseDouble(parts[3]);
                int quantity = Integer.parseInt(parts[4]);

                Accessory accessory = null;

                if (type.equals("CONTROLLER")) {

                    String connectionType = parts[5];

                    accessory = new Controller(
                            id,
                            title,
                            price,
                            quantity,
                            connectionType
                    );

                    loadCompatibleConsoleIds(accessory, parts[6]);

                } else if (type.equals("CABLE")) {

                    float meters = Float.parseFloat(parts[5]);
                    String connectorType = parts[6];

                    accessory = new Cable(
                            id,
                            title,
                            price,
                            quantity,
                            meters,
                            connectorType
                    );

                    loadCompatibleConsoleIds(accessory, parts[7]);

                } else if (type.equals("MEMORY")) {

                    int capacityGb = Integer.parseInt(parts[5]);
                    String memoryType = parts[6];

                    accessory = new Memory(
                            id,
                            title,
                            price,
                            quantity,
                            capacityGb,
                            memoryType
                    );

                    loadCompatibleConsoleIds(accessory, parts[7]);
                }

                if (accessory != null) {
                    accessories.add(accessory);
                }
            }

        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Error loading accessories.", e);
        }

        return accessories;
    }

    /**
     * Loads the compatible console IDs into an accessory.
     *
     * @param accessory accessory receiving the compatibility information
     * @param compatibleIds serialized compatible console IDs
     */
    private void loadCompatibleConsoleIds(
            Accessory accessory,
            String compatibleIds) {

        if (compatibleIds == null || compatibleIds.isBlank()) {
            return;
        }

        String[] ids = compatibleIds.split("\\|");

        for (String consoleId : ids) {

            if (!consoleId.isBlank()) {
                accessory.addCompatibleConsoleId(consoleId);
            }
        }
    }
}
