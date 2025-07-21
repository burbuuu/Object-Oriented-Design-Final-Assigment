package edu.uoc.uoctron.model.Plants;

import edu.uoc.uoctron.exception.PowerPlantException;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * This class represents a Solar Plant and contains its static information.
 * Inherits from {@code RenewablePlant}.
 *
 * This class sets specific values for restart time, stability, and icon.
 * These are passed to the parent constructor.
 *
 * @author Iris Garcia Gomez
 * @version 1.0
 */
public class SolarPlant extends RenewablePlant{
    /**
     * Restart time of the plant type.
     */
    private static final Duration RESTART_TIME = Duration.of(6, ChronoUnit.MINUTES);
    /**
     * Stability constant of the plant type.
     */
    private static final double STABILITY = 0.1;
    /**
     * Icon filename.
     */
    private static final String ICON = "solar.png";
    /**
     * Start of availability window of this plant type.
     */
    private static final LocalTime AVAILABILITY_START = LocalTime.of(7,0);
    /**
     * Icon filename.
     */
    private static final LocalTime AVAILABILITY_END = LocalTime.of(18,59);

    /**
     * Constructor of a new {@code SolarPlant}. Passes the data to the parent constructor.
     * @param name Name of the plant.
     * @param latitude Latitude of the plant.
     * @param longitude Longitude of the plant.
     * @param city City of the plant.
     * @param maxCapacityMW Max capacity of the plant in MW.
     * @param efficiency Efficiency of the plant.
     */
    public SolarPlant(String name, double latitude, double longitude, String city, double maxCapacityMW,
                      double efficiency) throws PowerPlantException {
        super(name, latitude, longitude, city, maxCapacityMW, STABILITY, RESTART_TIME, ICON, AVAILABILITY_START,
                AVAILABILITY_END, efficiency);
    }

    /**
     * Returns the type of the power plant as a string.
     * @return "Solar
     */
    @Override
    public String getType(){
        return "Solar";
    }


}
