package com.gamezone.exceptions;

/**
 * Thrown when a resource (product, customer, seller, sale, etc.) is looked up
 * by its identifier and does not exist in the system.
 */
public class ResourceNotFoundException extends GameZoneException {

    private static final String ERROR_CODE = "RESOURCE_NOT_FOUND";

    private final String resourceType;
    private final String resourceId;

    /**
     * Creates the exception and builds a descriptive message in Spanish,
     * for example "El producto con identificador P01 no fue encontrado".
     *
     * @param resourceType kind of resource that was searched, in Spanish and
     *                     singular (for example "producto" or "venta")
     * @param resourceId   identifier that was searched
     */
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(buildMessage(resourceType, resourceId), ERROR_CODE);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    /**
     * Returns the kind of resource that was not found.
     *
     * @return the resource type
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * Returns the identifier that was searched.
     *
     * @return the resource identifier
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * Builds the user-facing message, choosing the Spanish article and the
     * ending of "encontrado" according to the grammatical gender of the
     * resource type.
     *
     * @param resourceType kind of resource
     * @param resourceId   searched identifier
     * @return the message in Spanish
     */
    private static String buildMessage(String resourceType, String resourceId) {
        String type = (resourceType == null || resourceType.isBlank())
                ? "recurso" : resourceType.trim();
        boolean feminine = isFeminine(type);
        return (feminine ? "La " : "El ") + type
                + " con identificador " + resourceId
                + (feminine ? " no fue encontrada" : " no fue encontrado");
    }

    /**
     * Heuristic for Spanish nouns used in the system: words ending in "a" or
     * "ción" (venta, persona, garantía, devolución, promoción) are feminine.
     *
     * @param type the noun to check
     * @return true if the noun is treated as feminine
     */
    private static boolean isFeminine(String type) {
        String lower = type.toLowerCase();
        return lower.endsWith("a") || lower.endsWith("ción");
    }
}