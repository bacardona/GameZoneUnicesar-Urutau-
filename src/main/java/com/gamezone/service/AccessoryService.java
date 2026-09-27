package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;
import com.gamezone.persistence.AccessoryRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the business rules for managing accessories.
 */
public class AccessoryService {

    private final AccessoryRepository accessoryRepository;
    private final List<Accessory> accessories;

    /**
     * Creates an accessory service using the given repository.
     *
     * @param accessoryRepository repository used to persist accessories
     */
    public AccessoryService(AccessoryRepository accessoryRepository) {

        this.accessoryRepository = accessoryRepository;

        // Carga los accesorios que ya estaban guardados.
        this.accessories = accessoryRepository.loadAll();
    }

    /**
     * Registers a new controller.
     *
     * @param controller controller to register
     */
    public void registerController(Controller controller) {

        validateNewAccessory(controller);

        accessories.add(controller);

        accessoryRepository.saveAll(accessories);
    }

    /**
     * Registers a new cable.
     *
     * @param cable cable to register
     */
    public void registerCable(Cable cable) {

        validateNewAccessory(cable);

        accessories.add(cable);

        accessoryRepository.saveAll(accessories);
    }

    /**
     * Registers a new memory accessory.
     *
     * @param memory memory accessory to register
     */
    public void registerMemory(Memory memory) {

        validateNewAccessory(memory);

        accessories.add(memory);

        accessoryRepository.saveAll(accessories);
    }

    /**
     * Returns all registered accessories.
     *
     * @return list containing all accessories
     */
    public List<Accessory> listAllAccessories() {
        return accessories;
    }

    /**
     * Returns accessories filtered by their concrete type.
     *
     * @param type accessory type, such as Controller, Cable, or Memory
     * @return list of accessories matching the requested type
     */
    public List<Accessory> listAccessoriesByType(String type) {

        List<Accessory> result = new ArrayList<>();

        for (Accessory accessory : accessories) {

            if (type.equalsIgnoreCase("Controller")
                    && accessory instanceof Controller) {

                result.add(accessory);

            } else if (type.equalsIgnoreCase("Cable")
                    && accessory instanceof Cable) {

                result.add(accessory);

            } else if (type.equalsIgnoreCase("Memory")
                    && accessory instanceof Memory) {

                result.add(accessory);
            }
        }

        return result;
    }

    /**
     * Finds all accessories compatible with a given console.
     *
     * @param consoleId identifier of the console
     * @return list of compatible accessories
     */
    public List<Accessory> findAccessoriesCompatibleWith(String consoleId) {

        List<Accessory> result = new ArrayList<>();

        for (Accessory accessory : accessories) {

            if (accessory.isCompatibleWith(consoleId)) {
                result.add(accessory);
            }
        }

        return result;
    }

    /**
     * Finds an accessory by its identifier.
     *
     * @param id accessory identifier
     * @return the accessory with the specified ID, or null if it does not exist
     */
    public Accessory findById(String id) {

        for (Accessory accessory : accessories) {

            if (accessory.getId().equals(id)) {
                return accessory;
            }
        }

        return null;
    }

    /**
     * Updates the stock of an accessory by decreasing its available quantity.
     *
     * @param accessoryId identifier of the accessory
     * @param quantity quantity to subtract from the stock
     */
    public void updateStock(String accessoryId, int quantity) {

        Accessory accessory = findById(accessoryId);

        if (accessory == null) {
            throw new IllegalArgumentException(
                    "Accessory not found: " + accessoryId);
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero.");
        }

        if (accessory.getQuantity() < quantity) {
            throw new IllegalArgumentException(
                    "Not enough stock for: " + accessory.getTitle());
        }

        accessory.setQuantity(
                accessory.getQuantity() - quantity
        );

        accessoryRepository.saveAll(accessories);
    }

    /**
     * Validates the basic information of a new accessory.
     *
     * @param accessory accessory to validate
     */
    private void validateNewAccessory(Accessory accessory) {

        if (accessory == null) {
            throw new IllegalArgumentException(
                    "Accessory cannot be null.");
        }

        if (accessory.getId() == null
                || accessory.getId().isBlank()) {

            throw new IllegalArgumentException(
                    "Accessory ID cannot be empty.");
        }

        if (accessory.getTitle() == null
                || accessory.getTitle().isBlank()) {

            throw new IllegalArgumentException(
                    "Accessory title cannot be empty.");
        }

        if (accessory.getPrice() < 0) {
            throw new IllegalArgumentException(
                    "Accessory price cannot be negative.");
        }

        if (accessory.getQuantity() < 0) {
            throw new IllegalArgumentException(
                    "Accessory quantity cannot be negative.");
        }

        if (findById(accessory.getId()) != null) {
            throw new IllegalArgumentException(
                    "Accessory ID already exists.");
        }
    }
}