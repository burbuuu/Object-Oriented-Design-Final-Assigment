package edu.uoc.uoctron.model.Plants;

import edu.uoc.uoctron.exception.PowerPlantException;
import org.json.JSONObject;

import java.time.Duration;

/**
 * This is the abstract, base class for thermal power plants. Inherits from {@code PowerPlant} and adds the fuel.
 * The fuel type is stored inside a separate enum {@code FuelType}, and each child of this class is expected to have one fuelType
 * assigned.
 *<p>This class only provides one constructor, as it uses the default values for the start and end of the availability inside the
 * parent class. </p>
 *
 * @author Iris Garcia Gomez
 * @version 1.0
 */
public abstract class ThermalPlant extends PowerPlant {

    /**
     * Fuel type used in the power plant.
     */
    private final FuelType fuel;

    /**
     * Constructor of the class. Uses the default values for availability start and end of the parent class.
     *
     * @param name Its name.
     * @param latitude Latitude of the plant.
     * @param longitude Longitude of the plant.
     * @param city Town of the plant.
     * @param maxCapacityMW Maximum generation capacity in MW.
     * @param stability Stability of the plant
     * @param restartTime Restart time after the blackout. Static data from child classes.
     * @param icon Icon filename. Static data from child classes.
     * @param fuel Fuel type.
     * @throws PowerPlantException If any argument is invalid.
     */
    public ThermalPlant(String name, double latitude, double longitude, String city, double maxCapacityMW,
                        double stability, Duration restartTime, String icon, FuelType fuel) throws PowerPlantException {
        super(name, latitude, longitude, city, maxCapacityMW, stability, restartTime, icon);
        this.fuel = fuel;
    }


    /**
     * Returns the output of the plant in MW.
     * If the plant is ONLINE the output is calculated as: capacity.
     * Otherwise, the output is 0.
     * @return Output of the plant in MW.
     */
    @Override
    public double simulatePowerOutputMW() {
        if(getState()==State.ONLINE){
            return this.assignedPowerOutput;
        }
        else {
            return 0;
        }
    }

    /**
     * Returns the information of the power plant as String in JSON format. Gets the data from the parent class and
     * adds the fuel information.
     * @return Plant data with the fuel type.
     */
    @Override
    public String toString(){
        JSONObject plant = new JSONObject(super.toString());
        plant.put("fuelType", fuel.toString());
        return plant.toString();
    }
}
