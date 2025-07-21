package edu.uoc.uoctron.model.Plants;

import edu.uoc.uoctron.exception.PowerPlantException;
import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Abstract base class for the Power Plants. This class is responsible for the validation of the PowerPlant data,
 * storing plant information and simulating their power output.
 *
 * <p>The class provides two constructors: one that accepts start and end times for the plant's availability time,
 *  and other constructor with default values for these attributes.</p>
 *
 * <p>The class implements the Interface {@code PowerOutputSimulation}, and provides a default method for assignPowerOutput
 * but child classes are expected to override it if necessary.</p>
 *
 * <p>During the data validation, this class may throw a {@code PowerPlantException}, if any data is not valid.</p>
 *
 * @author Iris Garcia Gomez
 * @version 0.6
 */

public abstract class PowerPlant implements PowerOutputSimulation{

    /**
     * Static field, plant count.
     */
    private static int plantCount = 0;
    /**
     * Plant id, it's set automatically, based on the plant count.
     */
    private final int id;
    /**
     * Name of the plant.
     */
    private String name;
    /**
     * It is the Latitude of the plant, valid range [-90,90].
     */
    private double latitude;
    /**
     * It is the Longitude of the plant, valid range [-180,180].
     */
    private double longitude;
    /**
     * City
     */
    private String city;
    /**
     * Max generation capacity in MW.
     */
    private double maxCapacityMW;
    /**
     * Stability of the plant. Valid range: [0,1].
     */
    private double stability;
    /**
     * Restart time after the blackout
     */
    private final Duration restartTime;
    /**
     * Icon filename.
     */
    private final String icon;
    /**
     * Available from this time in normal conditions.
     */
    private final LocalTime availabilityStart;
    /**
     * Available until this time in normal conditions.
     */
    private final LocalTime availabilityEnd;
    /**
     * State of the plant: unavailable, offline or online.
     */
    private State state;

    /**
     * Assigned power output in MW.
     *
     */
    double assignedPowerOutput;

    /**
     * Constructs a new Power plant. This is the most general constructor as it sets the availability start and end.
     * Initializes all the information of the plant as well as the static data of the child classes.
     * The constructor is responsible for updating the static {@code plantCount} and assigns a unique id to the plant,
     * based on the count.
     *
     * @throws PowerPlantException If any argument is invalid.
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
     * @param availabilityEnd Available until this time in normal conditions. Static data from child classes.
     */
    public PowerPlant(String name, double latitude, double longitude, String city, double maxCapacityMW,
                      double stability, Duration restartTime, String icon, LocalTime availabilityStart,
                      LocalTime availabilityEnd) throws PowerPlantException{
        setName(name);
        setLatitude(latitude);
        setLongitude(longitude);
        setCity(city);
        setMaxCapacity(maxCapacityMW);
        setStability(stability);
        this.restartTime = restartTime;
        this.icon = icon;
        this.availabilityStart = availabilityStart;
        this.availabilityEnd = availabilityEnd;
        setState(State.UNAVAILABLE); // At the start of the simulation the plant state is unavailable

        // Set the id and increment the plant count
        id = plantCount;
        plantCount++;
    }

    /**
     * This constructor provides default {@code availabilityStart} at 00:00:00 and {@code availabilityEnd} at 23:59:59.
     * Then it delegates to the most general constructor.
     *
     * @throws PowerPlantException If an argument is invalid.
     *
     * @param name Its name.
     * @param latitude Latitude of the plant.
     * @param longitude Longitude of the plant.
     * @param city Town of the plant.
     * @param maxCapacityMW Max generation capacity in MW.
     * @param stability Stability of the plant.
     * @param restartTime Restart time after the blackout.
     * @param icon Icon filename.
     */
    public PowerPlant(String name, double latitude, double longitude, String city, double maxCapacityMW, double stability,
                      Duration restartTime, String icon) throws PowerPlantException{
        this(name, latitude, longitude, city, maxCapacityMW, stability, restartTime, icon, LocalTime.MIN, LocalTime.MAX);
    }

    /**
     * Returns the id of the plant.
     * @return id.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the name of the plant and checks that is not empty or null.
     * @param name Name of the power plant.
     * @throws PowerPlantException If name is null or blank.
     */
    public void setName(String name) throws PowerPlantException{
        if(name.isEmpty()){
            throw new PowerPlantException(PowerPlantException.ERROR_NAME);
        }
        this.name = name.trim();
    }

    /**
     * Sets the latitude of the plant and checks that is not out of range [-90,90].
     * @param latitude Latitude of the power plant.
     * @throws PowerPlantException If latitude is out of range.
     */
    public void setLatitude(double latitude) throws PowerPlantException {
        if(latitude < -90 || latitude > 90){
            throw new PowerPlantException(PowerPlantException.ERROR_LATITUDE);
        }
        this.latitude = latitude;
    }

    /**
     * Sets the longitude of the plant and checks that is not out of range [-180,180].
     * @param longitude Longitude of the power plant.
     * @throws PowerPlantException If longitude is out of range.
     */
    public void setLongitude(double longitude) throws PowerPlantException{
        if(longitude < -180 || longitude > 180){
            throw new PowerPlantException(PowerPlantException.ERROR_LONGITUDE);
        }
        this.longitude = longitude;
    }

    /**
     * Sets the city of the plant and checks that is not empty or null.
     * @param city City of the power plant.
     * @throws PowerPlantException If city is null or blank.
     */
    public void setCity(String city) throws PowerPlantException {
        if(city.isBlank()){
            throw new PowerPlantException(PowerPlantException.ERROR_CITY);
        }
        this.city = city;
    }

    /**
     * Returns the max capacity of a power plant (MW).
     * @return Max Capacity in MW
     */
    public double getMaxCapacityMW() {
        return maxCapacityMW;
    }

    /**
     * Sets max capacity of the plant.
     * @param maxCapacityMW Max capacity in MW.
     * @throws PowerPlantException If the capacity is negative.
     */
    public void setMaxCapacity(double maxCapacityMW) throws PowerPlantException{
        if(maxCapacityMW < 0){
            throw new PowerPlantException(PowerPlantException.ERROR_CAPACITY);
        }
        this.maxCapacityMW = maxCapacityMW;
    }

    /**
     * Returns the maximum operational power output of the plant in MW.
     * This is expected to be overridden by the child classes if the plant cannot work at full capacity.
     * @return Maximum power output of the plant in MW.
     */
    public double getMaxPowerOutput(){
        return maxCapacityMW;
    }

    /**
     * Returns a double with the stability of the power plant.
     * @return stability
     */
    public double getStability(){
        return stability;
    }

    /**
     * Sets the stability of the power plant. It should be between 1 and 0.
     * @param stability Stability of the power generation. Should be between one and 0.
     * @throws PowerPlantException If the stability is outside the valid rank.
     */
    public void setStability(double stability) throws PowerPlantException{
        if(!(stability >= 0 && stability <= 1)){
            throw new PowerPlantException(PowerPlantException.ERROR_STABILITY);
        }
        this.stability = stability;
    }


    /**
     * Returns the state of the power plant: ONLINE, IDLE or UNAVAILABLE.
     * @return State of the power plant.
     */
    public State getState() {
        return state;
    }

    /**
     * Sets state of the power plant.
     * Do not use this method outside the model package! Not for external use outside the simulation logic.
     * @param state State of the plant.
     * @throws PowerPlantException Throws an exception if the state is null.
     */
    public void setState(State state) throws PowerPlantException{
        if(state == null){
            throw new PowerPlantException(PowerPlantException.ERROR_STATE_NULL);
        }
        this.state = state;
    }

    /**
     * This method tries to assign a Power Output to the plant, and returns how much is assigned.
     * The method tries to adjust the assigned output to the actual capability of the plant, so if the plant is unavailable
     * or working at a lower efficiency, the actual output gets adjusted.
     * @param powerDemand Power output demand.
     * @return Assigned power output for the plant.
     */
    @Override
    public double assignPowerOutput(double powerDemand){
        if(powerDemand < 0){
            throw new PowerPlantException(PowerPlantException.ERROR_NEGATIVE_POWER_ASSIGNMENT);
        }
        if(getState()==State.UNAVAILABLE) return 0;

        double oldOutput = assignedPowerOutput;
        assignedPowerOutput = Math.min(powerDemand,getMaxPowerOutput());
        double added = assignedPowerOutput - oldOutput;
        return added > 0 ? added : 0;
    }

    /**
     * Checks plant availability based on its availability window (after availability start and before its end),
     * the blackoutTime and the restarting time of the plant.
     * @param blackoutTime Blackout time.
     * @param time Actual time in the simulation.
     * @return {@code false} the plant is not available and should be set to UNAVAILABLE
     *         {@code true} the plant is available it should be set to IDLE
     */
    public boolean isAvailable(LocalDateTime blackoutTime, LocalDateTime time){
        boolean isInAvailabilityWindow;
        boolean hasRestarted;

        //Check availability window, blackoutTime and time should be converted to LocalTime
        isInAvailabilityWindow = (!time.toLocalTime().isBefore(availabilityStart) &&
                !time.toLocalTime().isAfter(availabilityEnd));
        // Check if the plant has restarted.
        hasRestarted = time.isAfter(blackoutTime.plus(restartTime));

        return isInAvailabilityWindow && hasRestarted;
    }


    /**
     * Returns the plant type as a string.
     * @return Type of power plant.
     */
    public abstract String getType();

    /**
     * Returns the basic data of the plant.
     * @return Json basic data of the plant.
     */
    @Override
    public String toString(){
        JSONObject plant = new JSONObject();
        plant.put("id",id);
        plant.put("name",name);
        plant.put("type",getType());
        plant.put("latitude",latitude);
        plant.put("longitude",longitude);
        plant.put("city",city);
        plant.put("maxCapacityMW",maxCapacityMW);
        plant.put("assignedOutputMW", assignedPowerOutput);
        plant.put("icon",icon);
        plant.put("state",state.toString());
        return plant.toString();
    }
}
