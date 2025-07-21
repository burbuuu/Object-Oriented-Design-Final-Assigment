package edu.uoc.uoctron.model.Plants;

import edu.uoc.uoctron.exception.PowerPlantException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * This class represents a Nuclear Plant and contains its static information.
 * Inherits from {@code PowerPlant}.
 *
 * This class sets specific values for restart time, stability, and icon.
 * These are passed to the parent constructor.
 *
 * @author Iris Garcia Gomez
 * @version 1.0
 */
public class NuclearPlant extends PowerPlant{
    /**
     * Restart time of the plant type.
     */
    private static final Duration RESTART_TIME = Duration.of(1, ChronoUnit.DAYS);
    /**
     * Stability constant of the plant type.
     */
    private static final double STABILITY = 1.0;
    /**
     * Icon filename.
     */
    private static final String ICON = "nuclear.png";

    /**
     * Constructor of a new {@code PowerPlant}. Passes the data to the parent constructor.
     * @param name Name of the plant.
     * @param latitude Latitude of the plant.
     * @param longitude Longitude of the plant.
     * @param city City of the plant.
     * @param maxCapacityMW Max capacity of the plant in MW.
     */
    public NuclearPlant(String name, double latitude, double longitude, String city, double maxCapacityMW)
            throws PowerPlantException {
        super(name,latitude,longitude,city,maxCapacityMW, STABILITY, RESTART_TIME, ICON);
    }

    /**
     * Returns the type of the power plant as a string.
     * @return "Nuclear
     */
    @Override
    public String getType(){
        return "Nuclear";
    }

    /**
     * Returns the output of the plant in MW.
     * If the plant is ONLINE the output is calculated as: capacity * efficiency.
     * Otherwise, the output is 0.
     * @return Output of the plant in MW.
     */
    @Override
    public double simulatePowerOutputMW() {
        if(getState() == State.ONLINE){
            return this.assignedPowerOutput;
        }
        else {
            return 0;
        }
    }
}
