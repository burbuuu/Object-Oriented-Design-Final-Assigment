package edu.uoc.uoctron.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UOCtronControllerTest {

    private UOCtronController controller;

    @BeforeEach
    public void setUp() {
        controller = new UOCtronController("plants.txt", "demand_forecast.txt");
    }

    @Test
    @Order(1)
    @Tag("basic")
    public void testGetPowerPlantsNotEmpty() {
        Object[] plants = controller.getPowerPlants();
        assertNotNull(plants);
        assertTrue(plants.length > 0);
    }

    @Test
    @Order(2)
    @Tag("basic")
    public void testAllPlantsHaveValidToStringAsJSON() {
        for (Object plant : controller.getPowerPlants()) {
            assertDoesNotThrow(() -> new JSONObject(plant.toString()));
        }
    }

    @Test
    @Order(3)
    @Tag("basic")
    public void testEachPlantHasExpectedFieldsInJSON() {
        for (Object plant : controller.getPowerPlants()) {
            JSONObject json = new JSONObject(plant.toString());
            assertTrue(json.has("name"));
            assertTrue(json.has("type"));
            assertTrue(json.has("icon"));
            assertTrue(json.has("city"));
            assertTrue(json.has("latitude"));
            assertTrue(json.has("longitude"));
        }
    }

    private void assertPlant(Object plantObj, String expectedName, String expectedType, String expectedCity, double expectedCapacity) {
        JSONObject json = new JSONObject(plantObj.toString());

        assertEquals(expectedName, json.getString("name"));
        assertEquals(expectedType, json.getString("type"));
        assertEquals(expectedCity, json.getString("city"));
        assertEquals(expectedCapacity, json.getDouble("maxCapacityMW"), 0.0001);
    }

    @Test
    @Order(4)
    @Tag("basic")
    public void testAllPlantsLoadedCorrectlyInOrder() {
        Object[] plants = controller.getPowerPlants();
        assertEquals(39, plants.length);

        assertPlant(plants[0], "Almaraz I Nuclear Power Plant", "Nuclear", "Almaraz", 1300.0);
        assertPlant(plants[1], "Almaraz II Nuclear Power Plant", "Nuclear", "Almaraz", 1300.0);
        assertPlant(plants[2], "Ascó I Nuclear Power Plant", "Nuclear", "Ascó", 1275.0);
        assertPlant(plants[3], "Ascó II Nuclear Power Plant", "Nuclear", "Ascó", 1275.0);
        assertPlant(plants[4], "Cofrentes Nuclear Power Plant", "Nuclear", "Cofrentes", 1375.0);
        assertPlant(plants[5], "Trillo Nuclear Power Plant", "Nuclear", "Trillo", 1325.0);
        assertPlant(plants[6], "Vandellós II Nuclear Power Plant", "Nuclear", "Vandellòs", 1350.0);
        assertPlant(plants[7], "Aldeadávila Hydroelectric Plant", "Hydroelectric", "Aldeadávila de la Ribera", 1550.0);
        assertPlant(plants[8], "José María de Oriol Hydroelectric Plant", "Hydroelectric", "Alcántara", 1200.0);
        assertPlant(plants[9], "Villarino Hydroelectric Plant", "Hydroelectric", "Villarino de los Aires", 1075.0);
        assertPlant(plants[10], "Cortes-La Muela Pumped Hydro Plant", "Hydroelectric", "Cortes de Pallás", 2187.5);
        assertPlant(plants[11], "Saucelle Hydroelectric Plant", "Hydroelectric", "Saucelle", 650.0);
        assertPlant(plants[12], "Gigabateria do Tâmega", "Hydroelectric", "Vila Real", 1450.0);
        assertPlant(plants[13], "La Serena Hydroelectric Plant", "Hydroelectric", "Villanueva de la Serena", 1100.0);
        assertPlant(plants[14], "Castellón Combined Cycle Plant", "Combined cycle", "Castellón", 2062.5);
        assertPlant(plants[15], "Sagunto Combined Cycle Plant", "Combined cycle", "Sagunto", 1500.0);
        assertPlant(plants[16], "Bahía de Bizkaia Plant", "Combined cycle", "Ciérvana", 975.0);
        assertPlant(plants[17], "Castejón Combined Cycle Plant", "Combined cycle", "Castejón", 1050.0);
        assertPlant(plants[18], "Soto de Ribera Plant", "Combined cycle", "Ribera de Arriba", 1525.0);
        assertPlant(plants[19], "Aboño Thermal Power Plant", "Coal", "Carreño", 1150.0);
        assertPlant(plants[20], "Es Murterar Thermal Power Plant", "Coal", "Alcudia", 425.0);
        assertPlant(plants[21], "Meirama Thermal Power Plant", "Coal", "Cerceda", 687.5);
        assertPlant(plants[22], "Los Barrios Thermal Power Plant", "Coal", "Los Barrios", 700.0);
        assertPlant(plants[23], "El Andévalo Wind Farm", "Wind", "Huelva", 730.0);
        assertPlant(plants[24], "Maranchón Wind Farm", "Wind", "Guadalajara", 520.0);
        assertPlant(plants[25], "Peña del Cuervo Wind Farm", "Wind", "La Rioja", 337.5);
        assertPlant(plants[26], "Penamacor Wind Farm", "Wind", "Penamacor", 375.0);
        assertPlant(plants[27], "Núñez de Balboa Solar Plant", "Solar", "Usagre", 1250.0);
        assertPlant(plants[28], "Francisco Pizarro Solar Plant", "Solar", "Cáceres", 1475.0);
        assertPlant(plants[29], "Andasol Solar Plant", "Solar", "Granada", 375.0);
        assertPlant(plants[30], "Flotante de Alqueva Floating Solar Plant", "Solar", "Moura", 12.5);
        assertPlant(plants[31], "Solara 4 Solar Plant", "Solar", "Alcoutim", 550.0);
        assertPlant(plants[32], "La Loma Biomass Plant", "Biomass", "Villanueva del Arzobispo", 40.0);
        assertPlant(plants[33], "Cogeneración Puertollano", "Biomass", "Puertollano", 50.0);
        assertPlant(plants[34], "Geothermal Experimental Plant", "Geothermal", "San Sebastián", 12.5);
        assertPlant(plants[35], "Geotermia Pico Vermelho", "Geothermal", "Ponta Delgada (Azores)", 32.5);
        assertPlant(plants[36], "Central térmica de Castelnou Energía", "Fuel gas", "Castelnou", 987.5);
        assertPlant(plants[37], "Central térmica de Sabón", "Fuel gas", "Arteixo", 500.0);
        assertPlant(plants[38], "Central térmica de Escombreras", "Fuel gas", "Cartagena", 1037.5);
    }

    @Test
    @Order(5)
    @Tag("advanced")
    public void testGetSimulationsNotEmpty() {
        JSONArray simulationResults = controller.getSimulationResults();
        assertNotNull(simulationResults);
        assertTrue(simulationResults.isEmpty());

        // Run a simulation
        controller.runBlackoutSimulation(LocalDateTime.now());

        simulationResults = controller.getSimulationResults();
        assertNotNull(simulationResults);
        assertFalse(simulationResults.isEmpty());
    }

    @Test
    @Order(6)
    @Tag("advanced")
    public void testRunBlackoutSimulationAndGetResultsStructure() {
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.of(2, 0));
        controller.runBlackoutSimulation(now);
        JSONArray results = controller.getSimulationResults();

        assertNotNull(results);
        assertEquals(2160, results.length());

        JSONObject first = results.getJSONObject(0);
        assertTrue(first.has("time"));
        assertTrue(first.has("generatedMW"));
        assertTrue(first.has("expectedDemandMW"));
        assertTrue(first.has("averageStability"));
        assertTrue(first.has("generatedByTypeMW"));
    }

    @Test
    @Order(7)
    @Tag("advanced")
    public void testSimulationValuesWithinExpectedRanges() {
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.of(3, 15));
        controller.runBlackoutSimulation(now);
        JSONArray results = controller.getSimulationResults();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            double generated = result.getDouble("generatedMW");
            double demand = result.getDouble("expectedDemandMW");
            double stability = result.getDouble("averageStability");

            assertTrue(generated >= 0);
            assertTrue(demand >= 0);
            assertTrue(stability >= 0 && stability <= 1);
        }
    }

    @Test
    @Order(8)
    @Tag("advanced")
    public void testStabilityAlwaysAboveThreshold() {
        controller.runBlackoutSimulation(LocalDateTime.now().withHour(0).withMinute(0));
        JSONArray results = controller.getSimulationResults();

        boolean allAbove = true;
        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            if (result.getDouble("generatedMW") > 0.0 && result.getDouble("averageStability") < 0.7) {
                allAbove = false;
                break;
            }
        }

        assertTrue(allAbove);
    }

    @Test
    @Order(9)
    @Tag("advanced")
    public void testGeneratedEnergyDoesNotExceedDemand() {
        controller.runBlackoutSimulation(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 0)));
        JSONArray results = controller.getSimulationResults();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            double generated = result.getDouble("generatedMW");
            double demand = result.getDouble("expectedDemandMW");

            assertTrue(generated <= demand + 0.001);
        }
    }

    @Test
    @Order(10)
    @Tag("advanced")
    public void testSimulationResultsForKnownValues() {
        LocalDateTime blackoutStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
        controller.runBlackoutSimulation(blackoutStart);
        JSONArray results = controller.getSimulationResults();

        assertFalse(results.isEmpty());

        // Firsts minutes
        for (int i = 0; i < 4; i++) {
            JSONObject minute = results.getJSONObject(i);
            JSONObject genByType = minute.getJSONObject("generatedByTypeMW");

            assertFalse(genByType.has("Nuclear"));
            assertFalse(genByType.has("Hydroelectric"));
            assertFalse(genByType.has("Combined cycle"));
            assertFalse(genByType.has("Coal"));
            assertFalse(genByType.has("Wind"));
            assertFalse(genByType.has("Solar"));
            assertFalse(genByType.has("Biomass"));
            assertFalse(genByType.has("Geothermal"));
            assertFalse(genByType.has("Fuel gas"));
        }

        for (int i = 4; i < 7; i++) {
            JSONObject minute = results.getJSONObject(i);
            JSONObject genByType = minute.getJSONObject("generatedByTypeMW");

            assertFalse(genByType.has("Nuclear"));
            assertTrue(genByType.has("Hydroelectric"));
            assertFalse(genByType.has("Combined cycle"));
            assertFalse(genByType.has("Coal"));
            assertFalse(genByType.has("Wind"));
            assertFalse(genByType.has("Solar"));
            assertFalse(genByType.has("Biomass"));
            assertFalse(genByType.has("Geothermal"));
            assertFalse(genByType.has("Fuel gas"));
        }

        for (int i = 7; i < 61; i++) {
            JSONObject minute = results.getJSONObject(i);
            JSONObject genByType = minute.getJSONObject("generatedByTypeMW");

            assertFalse(genByType.has("Nuclear"));
            assertTrue(genByType.has("Hydroelectric"));
            assertFalse(genByType.has("Combined cycle"));
            assertFalse(genByType.has("Coal"));
            assertTrue(genByType.has("Wind"));
            assertFalse(genByType.has("Solar"));
            assertFalse(genByType.has("Biomass"));
            assertFalse(genByType.has("Geothermal"));
            assertFalse(genByType.has("Fuel gas"));
        }

        for (int i = 61; i < 121; i++) {
            JSONObject minute = results.getJSONObject(i);
            JSONObject genByType = minute.getJSONObject("generatedByTypeMW");

            assertFalse(genByType.has("Nuclear"));
            assertTrue(genByType.has("Hydroelectric"));
            assertFalse(genByType.has("Combined cycle"));
            assertFalse(genByType.has("Coal"));
            assertTrue(genByType.has("Wind"));
            assertFalse(genByType.has("Solar"));
            assertFalse(genByType.has("Biomass"));
            assertTrue(genByType.has("Geothermal"));
            assertFalse(genByType.has("Fuel gas"));
        }

        // Minute 500
        JSONObject minute500 = results.getJSONObject(500);
        JSONObject genByType500 = minute500.getJSONObject("generatedByTypeMW");

        assertFalse(genByType500.has("Nuclear"));
        assertTrue(genByType500.has("Hydroelectric"));
        assertTrue(genByType500.has("Combined cycle"));
        assertTrue(genByType500.has("Coal"));
        assertTrue(genByType500.has("Wind"));
        assertTrue(genByType500.has("Solar"));
        assertFalse(genByType500.has("Biomass"));
        assertTrue(genByType500.has("Geothermal"));
        assertFalse(genByType500.has("Fuel gas"));
        assertEquals(9212.5, genByType500.getDouble("Hydroelectric"), 0.1);
        assertEquals(7112.5, genByType500.getDouble("Combined cycle"), 0.1);
        assertEquals(2015.0, genByType500.getDouble("Coal"), 0.1);
        assertEquals(1962.5, genByType500.getDouble("Wind"), 0.1);
        assertEquals(562.5, genByType500.getDouble("Solar"), 0.1);
        assertEquals(45.0, genByType500.getDouble("Geothermal"), 0.1);

        // Minute 1000
        JSONObject minute1000 = results.getJSONObject(1000);
        JSONObject genByType1000 = minute1000.getJSONObject("generatedByTypeMW");

        assertFalse(genByType1000.has("Nuclear"));
        assertTrue(genByType1000.has("Hydroelectric"));
        assertTrue(genByType1000.has("Combined cycle"));
        assertFalse(genByType1000.has("Coal"));
        assertTrue(genByType1000.has("Wind"));
        assertFalse(genByType1000.has("Solar"));
        assertFalse(genByType1000.has("Biomass"));
        assertTrue(genByType1000.has("Geothermal"));
        assertFalse(genByType1000.has("Fuel gas"));
        assertEquals(9212.5, genByType1000.getDouble("Hydroelectric"), 0.1);
        assertEquals(6119.5, genByType1000.getDouble("Combined cycle"), 0.1);
        assertEquals(1232.5, genByType1000.getDouble("Wind"), 0.1);
        assertEquals(45.0, genByType1000.getDouble("Geothermal"), 0.1);

        // Minute 1500
        JSONObject minute1500 = results.getJSONObject(1500);
        JSONObject genByType1500 = minute1500.getJSONObject("generatedByTypeMW");

        assertTrue(genByType1500.has("Nuclear"));
        assertTrue(genByType1500.has("Hydroelectric"));
        assertTrue(genByType1500.has("Combined cycle"));
        assertFalse(genByType1500.has("Coal"));
        assertTrue(genByType1500.has("Wind"));
        assertFalse(genByType1500.has("Solar"));
        assertFalse(genByType1500.has("Biomass"));
        assertTrue(genByType1500.has("Geothermal"));
        assertFalse(genByType1500.has("Fuel gas"));
        assertEquals(9200.0, genByType1500.getDouble("Nuclear"), 0.1);
        assertEquals(9212.5, genByType1500.getDouble("Hydroelectric"), 0.1);
        assertEquals(1398.0, genByType1500.getDouble("Combined cycle"), 0.1);
        assertEquals(1962.5, genByType1500.getDouble("Wind"), 0.1);
        assertEquals(45.0, genByType1500.getDouble("Geothermal"), 0.1);

        // Minute 2000
        JSONObject minute2000 = results.getJSONObject(2000);
        JSONObject genByType2000 = minute2000.getJSONObject("generatedByTypeMW");

        assertTrue(genByType2000.has("Nuclear"));
        assertTrue(genByType2000.has("Hydroelectric"));
        assertTrue(genByType2000.has("Combined cycle"));
        assertFalse(genByType2000.has("Coal"));
        assertTrue(genByType2000.has("Wind"));
        assertTrue(genByType2000.has("Solar"));
        assertFalse(genByType2000.has("Biomass"));
        assertTrue(genByType2000.has("Geothermal"));
        assertFalse(genByType2000.has("Fuel gas"));
        assertEquals(9200.0, genByType2000.getDouble("Nuclear"), 0.1);
        assertEquals(9212.5, genByType2000.getDouble("Hydroelectric"), 0.1);
        assertEquals(283.5, genByType2000.getDouble("Combined cycle"), 0.1);
        assertEquals(1962.5, genByType2000.getDouble("Wind"), 0.1);
        assertEquals(3662.5, genByType2000.getDouble("Solar"), 0.1);
        assertEquals(45.0, genByType2000.getDouble("Geothermal"), 0.1);
    }

}
