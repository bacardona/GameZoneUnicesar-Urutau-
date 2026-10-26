package com.gamezone.exceptions;

/**
 * Root of the GameZone exception hierarchy. Every custom exception of the
 * system extends this class, directly or indirectly, so callers can catch
 * all domain errors with a single type when they do not need to distinguish
 * between them.
 *
 * <p>It extends {@link RuntimeException} (unchecked), so methods that throw it
 * do not need a {@code throws} clause and existing method signatures stay
 * unchanged.</p>
 *
 * <p>The class is abstract on purpose: it cannot be instantiated directly,
 * forcing every error to be reported with one of the specialized
 * subclasses.</p>
 */
public abstract class GameZoneException extends RuntimeException {

    private static final String DEFAULT_ERROR_CODE = "GAMEZONE_ERROR";

    private final String errorCode;

    /**
     * Creates an exception with a message and the default error code.
     *
     * @param message description of the error
     */
    protected GameZoneException(String message) {
        this(message, DEFAULT_ERROR_CODE);
    }

    /**
     * Creates an exception with a message and a specific error code.
     *
     * @param message   description of the error
     * @param errorCode short code that identifies the kind of error
     */
    protected GameZoneException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code that identifies the kind of error.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}