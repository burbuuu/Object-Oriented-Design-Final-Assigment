package edu.uoc.uoctron.model.Plants;

import edu.uoc.uoctron.exception.PowerPlantException;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * This class represents a Hydroelectrical Plant and contains its static information.
 * Inherits from {@code RenewablePlant}.
 *
 * This class sets specific values for restart time, stability, and icon.
 * These are passed to the parent constructor.
 *
 * @author Iris Garcia Gomez
 * @version 1.0
 */
public class HydroPlant extends RenewablePlant{
    /**
     * Restart time of the plant type.
     */
    private static final Duration RESTART_TIME = Duration.of(3, ChronoUnit.MINUTES);
    /**
     * Stability constant of the plant type.
     */
    private static final double STABILITY = 0.8;
    /**
     * Icon filename.
     */
    private static final String ICON = "hydro.png";

    /**
     * Constructor of a new {@code HydroPlant}. Passes the data to the parent constructor.
     * @param name Name of the plant.
     * @param latitude Latitude of the plant.
     * @param longitude Longitude of the plant.
     * @param city City of the plant.
     * @param maxCapacityMW Max capacity of the plant in MW.
     * @param efficiency Efficiency of the plant.
     */
    public HydroPlant(String name, double latitude, double longitude, String city, double maxCapacityMW,
                      double efficiency) throws PowerPlantException {
        super(name, latitude, longitude, city, maxCapacityMW, STABILITY, RESTART_TIME, ICON, efficiency);
    }

    /**
     * Returns the type of the power plant as a string.
     * @return "Hydroelectric"
     */
    @Override
    public String getType(){
        return "Hydroelectric";
    }


}
