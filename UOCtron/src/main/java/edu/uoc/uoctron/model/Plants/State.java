package edu.uoc.uoctron.model.Plants;

/**
 * Represents the operational state of the power plant.
 * @author Iris Garcia Gomez
 * @version 1.0
 */
public enum State {
    /** The power plant is fully operational and generating power.*/
    ONLINE,
    /** The power plant is fully operational but currently not generating power. */
    IDLE,
    /** The power plant can't currently generate power. */
    UNAVAILABLE
}
