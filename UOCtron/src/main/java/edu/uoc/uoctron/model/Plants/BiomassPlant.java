package edu.uoc.uoctron.model.Plants;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * This class represents a Biomass Plant and contains its static information.
 * Inherits from {@code ThermalPlant}.
 *
 * This class sets specific values for restart time, stability, fuel type, and icon.
 * These are passed to the parent constructor.
 *
 * @author Iris Garcia Gomez
 * @version 1.0
 */
public class BiomassPlant extends ThermalPlant{
    /**
     * Restart time of the plant type.
     */
    private static final Duration RESTART_TIME = Duration.of(3, ChronoUnit.HOURS);
    /**
     * Stability constant of the plant type.
     */
    private static final double STABILITY = 0.5;
    /**
     * Icon filename.
     */
    private static final String ICON = "biomass.png";
    /**
     * Fuel type of this plant type.
     */
    private static final FuelType FUEL = FuelType.BIOMASS;

    /**
     * Constructor of a new {@code BiomassPlant}. Passes the data to the parent constructor.
     * @param name Name of the plant.
     * @param latitude Latitude of the plant.
     * @param longitude Longitude of the plant.
     * @param city City of the plant.
     * @param maxCapacityMW Max capacity of the plant in MW.
     */
    public BiomassPlant(String name, double latitude, double longitude, String city, double maxCapacityMW){
        super(name, latitude, longitude, city, maxCapacityMW, STABILITY, RESTART_TIME, ICON, FUEL);
    }

    /**
     * Returns the type of the power plant as a string.
     * @return "Biomass
     */
    @Override
    public String getType(){
        return "Biomass";
    }


}