package edu.uoc.uoctron.model.Plants;

import edu.uoc.uoctron.exception.PowerPlantException;
import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalTime;

/**
 * This is the abstract, base class for renewable power plants. Inherits from {@code PowerPlant} and adds the efficiency.
 * This new attribute limits the amount of power output that the plant can produce.
 *
 * <p>The class provides two constructors, one with start and end availabilities, and other that uses the default values
 * for these attributes of the parent class.</p>
 *
 * <p>The class is responsible for validating the efficiency. </p>
 *
 * @author Iris Garcia Gomez
 * @version 1.0
 */
public abstract class RenewablePlant extends PowerPlant{

    /** Efficiency of the power plant, limits the amount of output that the plant can produce. Range [0,1]. */
    private double efficiency;

    /**
     * Most general constructor of the class. Is responsible for validating the efficiency and delegates to the
     * constructor of the parent class.
     *
     *
     * @param name Its name.
     * @param latitude Latitude of the plant.
     * @param longitude Longitude of the plant.
     * @param city Town of the plant.
     * @param maxCapacityMW Maximum generation capacity in MW.
     * @param stability Stability of the plant
     * @param restartTime Restart time after the blackout. Static data from child classes.
     * @param icon Icon filename. Static data from child classes.
     * @param availabilityStart Available until this time in normal conditions. Static data from child classes.
     * @param availabilityEnd Available until this time in normal conditions. Static data from child classes
     * @param efficiency Operating efficiency of the power plant.
     * @throws PowerPlantException If any argument is invalid.
     */
    public RenewablePlant(String name, double latitude, double longitude, String city, double maxCapacityMW,
                          double stability, Duration restartTime, String icon, LocalTime availabilityStart,
                          LocalTime availabilityEnd, double efficiency) throws PowerPlantException{

        super(name, latitude, longitude, city, maxCapacityMW, stability, restartTime, icon, availabilityStart, availabilityEnd);
        setEfficiency(efficiency);
    }

    /**
     * Constructor of the class that uses default starting and ending availability times. This constructor is
     * responsible for setting and validating the efficiency.
     *
     * @param name Its name.
     * @param latitude Latitude of the plant.
     * @param longitude Longitude of the plant.
     * @param city Town of the plant.
     * @param maxCapacityMW Maximum generation capacity in MW.
     * @param stability Stability of the plant
     * @param restartTime Restart time after the blackout. Static data from child classes.
     * @param icon Icon filename. Static data from child classes.
     * @param efficiency Operating efficiency of the power plant.
     * @throws PowerPlantException If any argument is invalid.
     */
    public RenewablePlant(String name, double latitude, double longitude, String city, double maxCapacityMW,
                          double stability, Duration restartTime, String icon, double efficiency) throws PowerPlantException {
        super(name, latitude, longitude, city, maxCapacityMW, stability, restartTime, icon);
        setEfficiency(efficiency);
    }

    /**
     * Set the efficiency of the plant. Validates that the efficiency is between 0 and 1.
     * @param efficiency Efficiency of the power plant.
     * @throws PowerPlantException If the efficiency is out of range.
     */
    public void setEfficiency(double efficiency) throws PowerPlantException{
        if(efficiency > 1 || efficiency < 0){
            throw new PowerPlantException(PowerPlantException.ERROR_EFFICIENCY);
        }
        this.efficiency = efficiency;
    }

    /**
     * Returns the output of the plant in MW.
     * If the plant is ONLINE the output is calculated as: capacity * efficiency.
     * Otherwise, the output is 0.
     * @return Output of the plant in MW.
     */
    @Override
    public double simulatePowerOutputMW() {
        double output = 0;
        if (getState() == State.ONLINE){
            output = assignedPowerOutput;
        }
        return output;
    }

    /**
     * Calculates the maximum power output at a given time.
     * @return Maximum power output in MW.
     */
    @Override
    public double getMaxPowerOutput(){
        return getMaxCapacityMW()*efficiency;
    }

    /**
     * Returns the information of the power plant as a string in json format. Gets the data from the parent class and
     * adds the efficiency information.
     * @return Plant data with the fuel type.
     */
    @Override
    public String toString() {
        JSONObject plant = new JSONObject(super.toString());
        plant.put("efficiency", efficiency);
        return plant.toString();
    }
}
