package com.gamezone.exceptions;

/**
 * Thrown when an input value does not meet the expected format, range or type,
 * for example a negative price, an empty text or a malformed email.
 */
public class InvalidDataException extends GameZoneException {

    private static final String ERROR_CODE = "INVALID_DATA";

    private final String fieldName;

    /**
     * Creates the exception and builds a descriptive message in Spanish, for
     * example "El campo 'precio' debe ser mayor que cero.".
     *
     * @param fieldName name of the invalid field, in Spanish (for example
     *                  "precio")
     * @param problem   description of what is wrong, in Spanish and without
     *                  final period (for example "debe ser mayor que cero")
     */
    public InvalidDataException(String fieldName, String problem) {
        super("El campo '" + fieldName + "' " + problem + ".", ERROR_CODE);
        this.fieldName = fieldName;
    }

    /**
     * Returns the name of the invalid field.
     *
     * @return the field name
     */
    public String getFieldName() {
        return fieldName;
    }
}