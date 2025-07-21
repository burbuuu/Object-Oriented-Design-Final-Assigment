package edu.uoc.uoctron.controller;

import edu.uoc.uoctron.model.Simulation;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;


public class UOCtronController {

    /**
     * Power Demand forecast in MW for a day.
     */
    private HashMap<LocalTime,Double> demandForecast;
    /**
     * Simulation model.
     */
    private Simulation simulation;

    /**
     * Constructor for the controller. It initializes the system, loads the demand forecast, creates the Plants and adds it
     * to the system.
     * @param plansFile Plants filename.
     * @param demandFile Power demand forecast filename.
     */
    public UOCtronController(String plansFile, String demandFile) {
        demandForecast = new HashMap<>();
        loadMinuteDemand(demandFile);
        simulation = new Simulation(demandForecast);
        loadPlants(plansFile);
    }

    /**
     * Load the plants from a file.
     * @param filename The name of the file to load the plants from.
     */
    private void loadPlants(String filename) {
        try (InputStream is = getClass().getResourceAsStream("/data/" + filename)) {
            assert is != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    // Skip comments or empty lines
                    if (line.startsWith("#") || line.trim().isEmpty()) {
                        continue;
                    }

                    // Split the values
                    int columns = 6;
                    String[] parts = line.split(",", columns);
                    if (parts.length < columns) {
                        continue; // Skip malformed lines
                    }

                    String type = parts[0].trim();
                    String name = parts[1].trim();
                    double latitude = Double.parseDouble(parts[2].trim());
                    double longitude = Double.parseDouble(parts[3].trim());
                    String city = parts[4].trim();
                    double maxCapacityMW = Double.parseDouble(parts[5].trim());
                    double efficiency = 1.0;

                    addPlant(type, name, latitude, longitude, city, maxCapacityMW, efficiency);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Load the minute demand from a file.
     * @param filename The name of the file to load the minute demand from.
     */
    private void loadMinuteDemand(String filename) {
        try (InputStream is = getClass().getResourceAsStream("/data/" + filename)) {
            assert is != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("#") || line.trim().isEmpty()) continue;

                    String[] parts = line.split(",", 2);
                    if (parts.length != 2) continue;

                    LocalTime time = LocalTime.parse(parts[0].trim());
                    double demand = Double.parseDouble(parts[1].trim());

                    addMinuteDemand(time, demand);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading demand forecast file: " + e.getMessage());
        }
    }

    /**
     * Add a new power plant to the system.
     * @param type Type of the plant (e.g., "NUCLEAR", "HYDRO", etc.)
     * @param name Name of the plant
     * @param latitude Latitude of the plant
     * @param longitude Longitude of the plant
     * @param city City where the plant is located
     * @param maxCapacityMW Maximum generation capacity of the plant in MW
     * @param efficiency Efficiency of the plant (0.0 to 1.0)
     */
    private void addPlant(String type, String name, double latitude, double longitude, String city,
                          double maxCapacityMW, double efficiency) {
        simulation.addPlant(type, name, latitude, longitude, city, maxCapacityMW, efficiency);
    }

    /**
     * Add a new minute demand to the system.
     * @param time The time of the demand
     * @param demand The demand value
     */
    private void addMinuteDemand(LocalTime time, double demand) {
        demandForecast.put(time,demand);
    }

    /**
     * Get the power plants in the system.
     * @return An array of power plants
     */
    public Object[] getPowerPlants() {
        return simulation.getPlants();
    }

    /**
     * Simulate a blackout according to the given start time.
     * @param blackoutStart The start time of the blackout
     */
    public void runBlackoutSimulation(LocalDateTime blackoutStart) {
        simulation.deployNewSimulation(blackoutStart);
    }

    /**
     * Return the simulation results in JSON format.
     * @return JSON array with the result of the simulation.
     */
    public JSONArray getSimulationResults() {
        return simulation.getSimulationResults();
    }

}
