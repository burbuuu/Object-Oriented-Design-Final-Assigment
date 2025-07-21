package edu.uoc.uoctron.exception;
/**
 * Exception class, for handling validation errors of the class {@code Simulation}. This class contains
 * predefined error messages.
 * @author Iris Garcia Gomez
 * @version 1.0
 */
public class SimulationException extends RuntimeException {
    /**
     * Error message for null blackout time.
     */
    public static final String ERROR_BLACKOUT_TIME_NULL = "Blackout time cannot be null.";
    /**
     * Error message for null or empty demand forecast.
     */
    public static final String ERROR_DEMAND_FORECAST_NULL = "Demand forecast is null or empty.";
    /**
     * Error message for a negative power demand value.
     */
    public static final String ERROR_NEGATIVE_POWER_DEMAND_VALUE = "Demand forecast contains a negative value.";
    /**
     * Error message for a missing demand entry.
     */
    public static final String ERROR_DEMAND_FORECAST_MISSING_ENTRY = "There is a missing demand forecast entry";
    /**
     * Error message for attempting a simulation with no PowerPlants on the system.
     */
    public static final String ERROR_POWER_PLANT_LIST_IS_EMPTY = "Power plant list is empty, cannot perform a simulation.";
    /**
     * Error message for wrong power plant type.
     */
    public static final String ERROR_INVALID_POWER_PLANT_TYPE = "Type of power plant is invalid or null.";


    /**
     * Constructor of the class. Creates a exception with a custom error message.
     * @param message Error message.
     */
    public SimulationException(String message) {
        super(message);
    }
}
