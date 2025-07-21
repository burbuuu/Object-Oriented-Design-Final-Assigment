package edu.uoc.uoctron.model.Plants;

/**
 * Interface that defines the behavior for assigning and simulating the power output of a power plant.
 * Implementing classes are expected to model those behaviors taking into consideration the specifications of the plant type.
 *
 * <p>The class {@code PowerPlant} brings a default implementation of this class.</p>
 *
 * @author Iris Garcia Gomez
 * @version 1.0
 */
public interface PowerOutputSimulation {

    /**
     * Assign power output to a power plant based on a given request demand.
     * If the plant already had some power assigned, this method calculates the difference between the previous
     * assignment and the new one, updates the assignment, and returns the actual change in power output.
     *
     * @param powerDemand Requested power output in MW.
     * @return The actual difference between the old and new power output assignment in MW.
     */
    double assignPowerOutput(double powerDemand);

    /**
     * This method returns the power output that a Power Plant produces.
     * First the method checks the state of the power plant and returns its output in MW.
     * @return Power output of the plant in MW.
     */
    double simulatePowerOutputMW();
}
