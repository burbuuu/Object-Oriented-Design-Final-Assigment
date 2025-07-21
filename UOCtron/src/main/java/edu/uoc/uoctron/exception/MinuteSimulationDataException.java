package edu.uoc.uoctron.exception;

/**
 * Exception class, for handling validation errors of the class {@code MinuteSimulationData}. This class contains
 * the error messages.
 * @author Iris Garcia Gomez
 * @version 1.0
 */
public class MinuteSimulationDataException extends AppException {
    /**
     * Error message of null time.
     */
    public static final String ERROR_TIME_NULL = "Time cannot be null.";
    /**
     * Error message for the stability.
     */
    public static final String ERROR_INVALID_STABILITY = "Stability must be between 0 and 1.";
    /**
     *  Error message for the Power generation.
     */
    public static final String ERROR_POWER_DEMAND = "Power demand cannot be negative.";
    /**
     *  Error message for the Power generation.
     */
    public static final String ERROR_GENERATED_POWER = "Power generation cannot be negative.";


    /**
     * Constructor of the class. Creates a exception with a custom error message.
     * @param message Error message.
     */
    public MinuteSimulationDataException(String message){
        super(message);
    }
}
