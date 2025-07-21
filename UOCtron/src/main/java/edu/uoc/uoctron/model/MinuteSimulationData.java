package edu.uoc.uoctron.model;

import edu.uoc.uoctron.exception.MinuteSimulationDataException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Stores the information of a minute in the simulation. Holds the time, the power demand, the grid stability,
 * the generated power and the generated power per plant type.
 *
 * <p>This class validates their attributes and throws an {@code MinuteSimulationException} if any of the data is not valid.</p>
 *
 * @author Iris Garcia Gomez
 * @version 1.0
 */
public class MinuteSimulationData {
    /**
     * Time inside the simulation.
     */
    private LocalDateTime time;
    /**
     * Power demand for this minute in MW.
     */
    private double powerDemandMW;
    /**
     * Stability of the grid. Valid values are between 0 and 1.
     */
    private double stability;
    /**
     * Generated power in MW.
     */
    private double generatedPowerMW;
    /**
     * Generated power by plant type in MW. Key: Type of plant. Value: Power generation in MW.
     */
    private HashMap<String,Double> generatedPowerPerPlantType;

    /**
     * Constructor of the class.
     * @param time Time.
     * @param powerDemandMW Power demand in MW.
     * @param stability Average stability of the grid.
     * @param generatedPowerMW Generated power in MW.
     * @param generatedPowerPerPlantType Generated power per plant type.
     * @throws MinuteSimulationDataException If any of the data is not valid.
     */
    public MinuteSimulationData(LocalDateTime time, double powerDemandMW, double stability, double generatedPowerMW,
                                HashMap<String, Double> generatedPowerPerPlantType) throws MinuteSimulationDataException{
        setTime(time);
        setPowerDemandMW(powerDemandMW);
        setStability(stability);
        setGeneratedPowerMW(generatedPowerMW);
        setGeneratedPowerPerPlantType(generatedPowerPerPlantType);
    }

    /**
     * Sets the time and checks that is not null.
     * @param time Time of the simulation.
     * @throws MinuteSimulationDataException If time is null.
     */
    private void setTime(LocalDateTime time) throws MinuteSimulationDataException{
        if(time == null){
            throw new MinuteSimulationDataException(MinuteSimulationDataException.ERROR_TIME_NULL);
        }
        this.time = time;
    }

    /**
     * Power demand in MW.
     * @param powerDemandMW Power demand.
     * @throws MinuteSimulationDataException If the power demand is negative.
     */
    private void setPowerDemandMW(double powerDemandMW) throws MinuteSimulationDataException{
        if(powerDemandMW < 0){
            throw new MinuteSimulationDataException(MinuteSimulationDataException.ERROR_POWER_DEMAND);
        }
        this.powerDemandMW = powerDemandMW;
    }

    /**
     * Average stability of the power grid (must be between 0 and 1). But the objective of the algorithm is that should
     * be always above 0.7.
     * @param stability Average stability of the grid.
     * @throws MinuteSimulationDataException If it's an invalid value.
     */
    private void setStability(double stability) throws MinuteSimulationDataException{
        if(stability < 0 || stability > 1){
            throw new MinuteSimulationDataException(MinuteSimulationDataException.ERROR_INVALID_STABILITY);
        }
        this.stability = stability;
    }

    /**
     * Sets the generated power by the grid. It should be always lower or equal to the demand.
     * @param generatedPowerMW Generated power in MW.
     * @throws MinuteSimulationDataException If the generation power is negative.
     */
    private void setGeneratedPowerMW(double generatedPowerMW) throws MinuteSimulationDataException{
        if(generatedPowerMW < 0){
            throw new MinuteSimulationDataException(MinuteSimulationDataException.ERROR_GENERATED_POWER);
        }
        this.generatedPowerMW = generatedPowerMW;
    }

    /**
     * Sets the generation by plant type. The data is en MW.
     * @param generatedPowerPerPlantType Generation by plant type.
     */
    private void setGeneratedPowerPerPlantType(HashMap<String, Double> generatedPowerPerPlantType) {
        this.generatedPowerPerPlantType = generatedPowerPerPlantType;
    }

    /**
     * Returns a string with the data of the simulation in JSON format.
     * @return JSON with the simulation data.
     */
    @Override
    public String toString(){

        JSONObject data = new JSONObject();
        data.put("time", time);
        data.put("expectedDemandMW", powerDemandMW);
        data.put("generatedMW",generatedPowerMW);
        data.put("averageStability", stability);
        data.put("generatedByTypeMW",generatedPowerPerPlantType);
        return data.toString();
    }
}
