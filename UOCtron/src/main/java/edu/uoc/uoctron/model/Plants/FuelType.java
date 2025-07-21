package edu.uoc.uoctron.model.Plants;

/**
 * This Enum represents the different combustibles for the thermal plants.
 * @author Iris Garcia Gomez
 * @version 1.0
 */
public enum FuelType {
    /**
     * Coal
     */
    COAL("Coal"),
    /**
     * Natural gas
     */
    NATURAL_GAS("Natural gas"),
    /**
     * Biomass
     */
    BIOMASS("Biomass"),
    /**
     * Fuel gas
     */
    FUEL_GAS("Fuel gas");


    private final String name;

    /**
     * Constructor for {@code FuelType}.
     *
     * @param name Name of the fuel type.
     */
    FuelType(String name){
        this.name = name;
    }

    /**
     * Returns a String with the fuel name.
     * @return Type of fuel.
     */
    @Override
    public String toString(){
        return name;
    }

}
