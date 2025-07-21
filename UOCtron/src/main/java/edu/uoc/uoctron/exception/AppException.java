package edu.uoc.uoctron.exception;

/**
 * Exception class that generates a RuntimeException with a custom message.
 * @version 1.0
 * @author Iris Garcia Gomez
 */
public abstract class AppException extends RuntimeException {

    /**
     * Base message for all errors.
     */
    public static final String ERROR_BASE = "[ERROR]: ";


    /**
     * Constructor. Adds the base string to the error message
     * @param message Error message of the child exception class.
     */
    public AppException(String message) {
        super(ERROR_BASE + message);
    }
}
