package edu.uoc.uoctron.exception;

/**
 * Exception class, for handling validation errors of the class {@code PowerPlant}. This class contains
 * predefined error messages.
 * @author Iris Garcia Gomez
 * @version 1.0
 */
public class PowerPlantException extends AppException {

    /**
     * Error message for null or blank name.
     */
    public static final String ERROR_NAME = "Name cannot be null or blank.";
    /**
     * Error message for invalid latitude range.
     */
    public static final String ERROR_LATITUDE = "Latitude must be in range [-90,90].";
    /**
     * Error message for invalid longitude range.
     */
    public static final String ERROR_LONGITUDE = "Longitude must be in range [-180,180].";
    /**
     * Error message for null or blank city.
     */
    public static final String ERROR_CITY = "City name cannot be null or blank.";
    /**
     * Error message for negative capacity.
     */
    public static final String ERROR_CAPACITY = "Max capacity cannot be negative";
    /**
     * Error message for invalid stability
     */
    public static final String ERROR_STABILITY = "Stability must be between 0 and 1.";
    /**
     * Error message for invalid efficiency.
     */
    public static final String ERROR_EFFICIENCY = "Efficiency must be in rank [0,1].";
    /**
     * Error message for the plant State. State cannot be null.
     */
    public static final String ERROR_STATE_NULL = "State of the plant can't be null.";
    /**
     * Error message for negative power assignment request.
     */
    public static final String ERROR_NEGATIVE_POWER_ASSIGNMENT = "Power assignment of the power plant cannot be negative.";

    /**
     * Constructor of the class. Creates a exception with a custom error message.
     * @param message Error message.
     */
    public PowerPlantException(String message) {
        super(message);
    }
}
