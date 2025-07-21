package edu.uoc.uoctron.model;

import edu.uoc.uoctron.exception.SimulationException;
import edu.uoc.uoctron.model.Plants.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * This class is responsible for managing the power production after a blackout of the power grid. Implements an algorithm for
 * the recuperation of the grid taking into account, the power demand forecast, and the average stability of the grid.
 * It assumes that the power demand forecast is the same for every day.
 *
 * <p>This class is also responsible for adding power plants to the system and updating assigning the power output to
 * each plant and updating their states.</p>
 *
 * @author Iris Garcia Gomez
 * @version 1.0
 */
public class Simulation {

    /**
     * Duration of the simulation: 36h
     */
    public final int SIMULATION_DURATION_MINUTES = 2160;

    /**
     * Minimum stability threshold.
     */
    public final double MINIMUM_STABILITY = 0.7;

    /**
     * Simulation start: blackoutTime
     */
    private LocalDateTime blackoutTime;
    /**
     * Current time of the simulation.
     */
    private LocalDateTime simulationTime;
    /**
     * Demand forecast in MW.
     */
    private HashMap<LocalTime,Double> powerDemandForecastMW;
    /**
     * Simulation result data.
     */
    private LinkedList<MinuteSimulationData> simulationResults;
    /**
     * Power plants.
     */
    private final LinkedList<PowerPlant> plants;


    /**
     * Constructor of the class. Initializes the power demand, and the plants list.
     * The other parameters get initialized by deployNewSimulation method, so this instance can get reused for new
     * simulations as long as the demand forecast remains the same.
     * @param powerDemandForecastMW Demand forecast.
     */
    public Simulation(HashMap<LocalTime,Double> powerDemandForecastMW){
        setPowerDemandForecastMW(powerDemandForecastMW);
        plants = new LinkedList<>();
        simulationResults = new LinkedList<>();
    }


    /**
     * This method creates a new simulation for the blackoutTime introduced. It creates a new simulation for each minute
     * of the time duration.
     * @param blackoutTime Blackout start time. It represents the start of the simulation.
     */
    public void deployNewSimulation(LocalDateTime blackoutTime){
       if(plants.isEmpty()){
           throw new SimulationException(SimulationException.ERROR_POWER_PLANT_LIST_IS_EMPTY);
       }

        setBlackoutTime(blackoutTime);
        /*Set the simulationTime at the blackout time. We will truncate it to minutes to avoid
        null values in the hash map of the demand forecast.
        */
        simulationTime = blackoutTime.truncatedTo(ChronoUnit.MINUTES);
        // Make a new LinkedList for the results, so we won't destroy other simulations
        simulationResults = new LinkedList<>();

        //Generate a Minute simulation for all the length of the simulation.
        for(int i = 0; i < SIMULATION_DURATION_MINUTES; i++){
            createNewMinuteSimulation();
            simulationTime = simulationTime.plusMinutes(1); // Update the simulation time.
        }
    }

    /**
     * This method contains the main algorithm of the simulation. Is responsible for assigning power output to each plant,
     * prioritizing renewables and nuclear over thermal plants, as well as updating the state of the plants for every iteration.
     * The algorithm ensures that the average stability of the grid is > 0.7, condition necessary to avoid another blackout
     * during the recuperation process.
     */
    private void createNewMinuteSimulation() {
        /*
          Check availability of every plant and set them as idle or unavailable.
          Also, the assigned power should be set to 0.
         */
        plants.forEach(plant -> {
            plant.setState(plant.isAvailable(blackoutTime, simulationTime) ? State.IDLE : State.UNAVAILABLE);
            plant.assignPowerOutput(0);
        });

        double demand = powerDemandForecastMW.get(simulationTime.toLocalTime()); //Load demand from the forecast.
        double remainingDemand = demand;
        double stability;

        //Filter available Power Plants and save it to a list.
        List<PowerPlant> availablePlants = plants.stream().filter(plant -> plant.getState() == State.IDLE)
                .sorted(Comparator.comparingInt(PowerPlant::getId)).toList();

        // Filter available list by renewable, thermal and nuclear. Then the plants are sorted by their id and stability.
        List<PowerPlant> renewables = plants.stream().filter(powerPlant -> powerPlant instanceof RenewablePlant).toList();
        List<PowerPlant> sortedRenewables = renewables.stream()
                .sorted(Comparator.comparingDouble(PowerPlant::getStability).reversed().thenComparing(PowerPlant::getId)).toList();
        List<PowerPlant> thermal = plants.stream().filter(plant -> plant instanceof ThermalPlant).toList();
        List<PowerPlant> sortedThermal = thermal.stream()
                .sorted(Comparator.comparingDouble(PowerPlant::getId)).toList();
        List<PowerPlant> nuclear = plants.stream().filter(plant -> plant instanceof NuclearPlant).toList();
        List<PowerPlant> sortedNuclear = nuclear.stream()
                .sorted(Comparator.comparingDouble(PowerPlant::getId)).toList();


        //Assign first to renewables, then nuclear and lastly to thermal.
        remainingDemand = assignGeneration(sortedRenewables, remainingDemand);
        remainingDemand = assignGeneration(sortedNuclear, remainingDemand);
        remainingDemand = assignGeneration(sortedThermal, remainingDemand);

        //Calculate Stability
        stability = calculateStability();

        //If stability < 0.7. Disconnect the least stable renewable plant (with the highest id) until the stability is back to >= 0.7.

        //Make a comparator that looks for the least stable plant.
        Comparator<PowerPlant> leastStablePlant = Comparator.comparingDouble(PowerPlant::getStability)
                .thenComparing(PowerPlant::getId); // If tied, select the plant with higher id.

        //Create a tree set with online renewable plants ordered by least stability.
        TreeSet<PowerPlant> renewablesOrderedByLeastStability = new TreeSet<>(leastStablePlant);
        availablePlants.stream()
                .filter(plant -> (plant instanceof RenewablePlant) && plant.getState() == State.ONLINE)
                .forEach(renewablesOrderedByLeastStability::add);

        //Disconnect renewables until the stability is good enough.
        for (PowerPlant plant : renewablesOrderedByLeastStability) {
            if (stability >= MINIMUM_STABILITY) break;
            //Disconnect the least stable plant
            double disconnectedOutput = plant.simulatePowerOutputMW();
            plant.assignPowerOutput(0);
            plant.setState(State.IDLE);
            remainingDemand += disconnectedOutput;
            stability = calculateStability();
        }

        // Assign power production again to nuclear plants.
        assignGeneration(nuclear, remainingDemand);

        //Add a new minute to the simulation data.
        simulationResults.add(new MinuteSimulationData(simulationTime, demand, calculateStability(), calculateGeneratedPowerMW(),
                calculateGeneratedPowerByType()));
    }

    /**
     * This method gets a list of power plants and a demand. It tries to assign them power generation updates the
     * state of the plants.
     * Then it returns the power demand that can't be allocated to the plants it received.
     * @param plants Power plants.
     * @param demand Power demand to be assigned.
     * @return Power demand that can't be allocated.
     */
    private double assignGeneration(List<PowerPlant> plants, double demand) {
        double remaining = demand;

        for (PowerPlant plant : plants) {
            if (remaining <= 0) break;

            double assigned = plant.assignPowerOutput(remaining);
            if (assigned > 0) plant.setState(State.ONLINE);
            remaining -= assigned;
        }
        return remaining;
    }

    /**
     * Sets the blackout time for a new simulation. It gets called inside deployNewSimulation.
     * @param blackoutTime Simulation start time date.
     * @throws SimulationException If the blackoutTime is null.
     */
    private void setBlackoutTime(LocalDateTime blackoutTime) throws SimulationException {
        if(blackoutTime == null){
            throw new SimulationException(SimulationException.ERROR_BLACKOUT_TIME_NULL);
        }
        this.blackoutTime = blackoutTime;
    }

    /**
     * Add a new power plant to the system.
     * @param type Type of the plant (e.g., "NUCLEAR", "HYDRO", etc.)
     * @param name Name of the plant
     * @param latitude Latitude of the plant
     * @param longitude Longitude of the plant
     * @param city City where the plant is located
     * @param maxCapacityMW Maximum generation capacity of the plant in MW
     * @param efficiency Efficiency of the plant (0.0 to 1.0).
     * @throws SimulationException If the type didn't coincide with any valid type of power plant.
     */
    public void addPlant(String type, String name, double latitude, double longitude, String city,
                         double maxCapacityMW, double efficiency) {
        PowerPlant plant;
        switch (type){
            case "BIOMASS" -> plant = new BiomassPlant(name, latitude, longitude, city, maxCapacityMW);
            case "COAL" -> plant = new CoalPlant(name, latitude, longitude, city, maxCapacityMW);
            case "COMBINED_CYCLE" -> plant = new CombinedCyclePlant(name, latitude, longitude, city, maxCapacityMW);
            case "FUEL_GAS" -> plant = new FuelGasPlant(name, latitude, longitude, city, maxCapacityMW);
            case "GEOTHERMAL" -> plant = new GeothermalPlant(name, latitude, longitude, city, maxCapacityMW, efficiency);
            case "HYDRO" -> plant = new HydroPlant(name, latitude, longitude, city, maxCapacityMW, efficiency);
            case "NUCLEAR" -> plant = new NuclearPlant(name, latitude, longitude, city, maxCapacityMW);
            case "SOLAR" -> plant = new SolarPlant(name, latitude, longitude, city, maxCapacityMW, efficiency);
            case "WIND" -> plant = new WindPlant(name, latitude, longitude, city, maxCapacityMW, efficiency);
            default -> throw new SimulationException(SimulationException.ERROR_INVALID_POWER_PLANT_TYPE);
        }
        plants.add(plant);
    }

    /**
     * Returns an array with all the power plants in the system.
     * @return An array of {@code PowerPlants}.
     */
    public PowerPlant[] getPlants(){
        return plants.toArray(new PowerPlant[0]);
    }

    /**
     * Sets the power demand forecast. Validates that all minutes of the day have a demand prediction and also that
     * all values are positive.
     * @param powerDemandForecastMW Hash map with the demand forecast.
     * @throws SimulationException Throws an exception if there is a missing data or a non-valid value.
     */
    public void setPowerDemandForecastMW(HashMap<LocalTime, Double> powerDemandForecastMW) throws SimulationException {
        if(powerDemandForecastMW == null || powerDemandForecastMW.isEmpty()){
            throw new SimulationException(SimulationException.ERROR_DEMAND_FORECAST_NULL);
        }

        LocalTime dayTime = LocalTime.MIN; //Sets it to 00:00
        //For every minute of the day create a new forecast enty.
        for(int i = 0; i < 1440; i++){
            //If an entry is missing throw an exception
            if(!powerDemandForecastMW.containsKey(dayTime)){
                throw new SimulationException(SimulationException.ERROR_DEMAND_FORECAST_MISSING_ENTRY);
            }
            //If there is a negative value, throw an exception.
            if(powerDemandForecastMW.get(dayTime) < 0){
                throw new SimulationException(SimulationException.ERROR_NEGATIVE_POWER_DEMAND_VALUE);
            }
            dayTime = dayTime.plusMinutes(1);
        }
        this.powerDemandForecastMW = powerDemandForecastMW;
    }


    /**
     * Simulates the power generated in MW.
     * @return Generated power in MW.
     */
    private double calculateGeneratedPowerMW(){
        double generation = 0;
        for(PowerPlant plant : plants) {
            generation += plant.simulatePowerOutputMW();
        }
        return generation;
    }

    /**
     * Returns the average stability of the power system.
     * @return Stability range [0.0,1.0].
     */
    private double calculateStability(){
        double ponderatedGeneration = 0;
        double totalGeneration = 0;
        double plantGeneration;
        for(PowerPlant plant: plants){
            plantGeneration = plant.simulatePowerOutputMW();
            ponderatedGeneration += plantGeneration * plant.getStability();
            totalGeneration += plantGeneration;
        }

        if(totalGeneration == 0) return 1;
        return ponderatedGeneration / totalGeneration;
    }

    /**
     * Returns a Hash map with the type of plant as key, and the generated output as values.
     * @return Power generated by type of plant in MW.
     */
    private HashMap<String, Double> calculateGeneratedPowerByType(){
        HashMap<String, Double> generatedByType = new HashMap<>();

        plants.forEach(plant -> {
            String type = plant.getType();
            double output = plant.simulatePowerOutputMW();
            //Filter out no output entries
            if(output > 0) {
                generatedByType.merge(type, output, Double::sum); // Sums the output value to the corresponding key.
            }
        });
        return generatedByType;
    }

    /**
     * Returns a JSONArray with the simulation results.
     * @return Simulation results in JSON format.
     */
    public JSONArray getSimulationResults(){
        JSONArray results = new JSONArray();

        for(MinuteSimulationData minuteResultData: simulationResults){
            String result = minuteResultData.toString();
            JSONObject jsonResult = new JSONObject(result);
            results.put(jsonResult);
        }
        return results;
    }



}
