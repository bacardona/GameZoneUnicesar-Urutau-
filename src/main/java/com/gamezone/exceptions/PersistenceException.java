package com.gamezone.exceptions;

/**
 * Thrown when an error occurs while reading or writing the system files, for
 * example a corrupt file, denied permission or a full disk. It keeps the
 * original exception as its cause so the technical detail is never lost.
 */
public class PersistenceException extends GameZoneException {

    private static final String ERROR_CODE = "PERSISTENCE_ERROR";

    /**
     * Creates the exception with a contextual message and the original cause.
     *
     * @param message description of the failure (which file and which
     *                operation)
     * @param cause   the original exception, usually an IOException
     */
    public PersistenceException(String message, Throwable cause) {
        super(message, ERROR_CODE);
        initCause(cause);
    }
}