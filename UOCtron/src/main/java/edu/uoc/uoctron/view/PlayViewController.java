package edu.uoc.uoctron.view;

import edu.uoc.uoctron.UOCtron;
import edu.uoc.uoctron.controller.UOCtronController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Controller for the play view.
 */
public class PlayViewController {

    private UOCtronController controller;

    @FXML
    private Pane mapPane;

    @FXML
    private Button backButton;

    @FXML private DatePicker datePicker;

    @FXML private Spinner<Integer> hourSpinner;

    @FXML private Spinner<Integer> minuteSpinner;

    @FXML
    public void initialize() {
        controller = new UOCtronController("plants.txt", "demand_forecast.txt");
        loadPlants();
        setupBackButton();

        datePicker.setValue(LocalDate.now());
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    }

    private void loadPlants() {
        for (Object plant : controller.getPowerPlants()) {
            String plantJsonString = plant.toString();
            try {
                JSONObject plantJson = new JSONObject(plantJsonString);

                double lat = plantJson.getDouble("latitude");
                double lon = plantJson.getDouble("longitude");
                String iconName = plantJson.getString("icon");

                ImageView icon = createIcon(iconName);
                if (icon == null) continue;

                double x = mapLongitudeToX(lon);
                double y = mapLatitudeToY(lat);

                icon.setLayoutX(x);
                icon.setLayoutY(y);

                String tooltipText = formatTooltip(plantJson);
                Tooltip tooltip = new Tooltip(tooltipText);

                icon.setOnMouseEntered(e -> {
                    tooltip.show(icon, e.getScreenX() + 10, e.getScreenY() + 10);
                });

                icon.setOnMouseExited(e -> tooltip.hide());

                icon.setMouseTransparent(false);
                icon.setPickOnBounds(true);

                mapPane.getChildren().add(icon);

            } catch (Exception e) {
                System.err.println("Error processing plant JSON: " + e.getMessage());
            }
        }
    }

    private ImageView createIcon(String iconName) {
        try (var is = getClass().getResourceAsStream("/images/icons/" + iconName)) {
            if (is == null) {
                System.err.println("Icon not found: " + iconName);
                return null;
            }
            Image image = new Image(is);
            ImageView icon = new ImageView(image);
            icon.setFitWidth(24);
            icon.setFitHeight(24);
            return icon;
        } catch (Exception e) {
            System.err.println("Error loading icon: " + e.getMessage());
            return null;
        }
    }

    private String formatTooltip(JSONObject json) {
        return "Name: " + json.optString("name", "Unknown") + "\n" +
                "Type: " + json.optString("type", "Unknown") + "\n" +
                "City: " + json.optString("city", "Unknown");
    }

    private double mapLongitudeToX(double longitude) {
        double minLon = -10.5;
        double maxLon = 4.3;
        double mapWidth = 630.0;

        return ((longitude - minLon) / (maxLon - minLon)) * mapWidth;
    }

    private double mapLatitudeToY(double latitude) {
        double minLat = 35.5;
        double maxLat = 43.1;
        double mapHeight = 400.0;

        return ((maxLat - latitude) / (maxLat - minLat)) * mapHeight;
    }

    @FXML
    private void setupBackButton() {
        backButton.setOnAction(e -> {
            try {
                UOCtron.main.goScene("main");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @FXML
    private void onSimulateBlackoutClicked() {
        LocalDate date = datePicker.getValue();
        if (date == null) {
            showAlert();
            return;
        }

        int hour = hourSpinner.getValue();
        int minute = minuteSpinner.getValue();
        LocalDateTime blackoutStart = LocalDateTime.of(date, LocalTime.of(hour, minute));

        controller.runBlackoutSimulation(blackoutStart);
        JSONArray results = controller.getSimulationResults();
        showSimulationChart(results);
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Select a date to simulate the blackout.", ButtonType.OK);
        alert.showAndWait();
    }

    private void showSimulationChart(JSONArray results) {
        if (results == null || results.isEmpty()) {
            System.err.println("No simulation results to show.");
            return;
        }

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Minutes since blackout");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Megawatts (MW)");

        StackedAreaChart<Number, Number> areaChart = new StackedAreaChart<>(xAxis, yAxis);
        areaChart.setTitle("Energy Generation vs Expected Demand (36h)");
        areaChart.setAnimated(false);
        areaChart.setCreateSymbols(false);
        areaChart.setLegendVisible(true);
        areaChart.setStyle("-fx-background-color: transparent;");
        areaChart.setPrefSize(1000, 600);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setAlternativeRowFillVisible(false);
        lineChart.setAlternativeColumnFillVisible(false);
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);
        lineChart.setOpacity(1.0);
        lineChart.setStyle("-fx-background-color: transparent;");
        lineChart.setPrefSize(1000, 600);

        Set<String> allTypesSet = new LinkedHashSet<>();

        // Primero obtener todos los tipos
        for (int i = 0; i < results.length(); i++) {
            JSONObject obj = results.getJSONObject(i);
            JSONObject genByType = obj.getJSONObject("generatedByTypeMW");
            allTypesSet.addAll(genByType.keySet());
        }
        List<String> sortedTypes = new ArrayList<>(allTypesSet);
        Collections.sort(sortedTypes);

        Map<String, XYChart.Series<Number, Number>> generationSeriesMap = new LinkedHashMap<>();
        for (String type : sortedTypes) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(type);
            generationSeriesMap.put(type, series);
        }

        XYChart.Series<Number, Number> demandSeries = new XYChart.Series<>();
        demandSeries.setName("Expected Demand");

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            double expected = result.getDouble("expectedDemandMW");
            JSONObject genByType = result.getJSONObject("generatedByTypeMW");

            demandSeries.getData().add(new XYChart.Data<>(i, expected));

            for (String type : sortedTypes) {
                double value = genByType.optDouble(type, 0.0);
                generationSeriesMap.get(type).getData().add(new XYChart.Data<>(i, value));
            }
        }

        for (String type : sortedTypes) {
            XYChart.Series<Number, Number> series = generationSeriesMap.get(type);
            if (series.getData().isEmpty() || series.getData().get(0).getXValue().intValue() != 0) {
                series.getData().add(0, new XYChart.Data<>(0, 0.0));
            }
        }

        areaChart.getData().addAll(generationSeriesMap.values());
        lineChart.getData().add(demandSeries);

        areaChart.setScaleY(1.12);
        areaChart.setTranslateY(5);

        StackPane chartPane = new StackPane(areaChart, lineChart);
        Scene scene = new Scene(new VBox(chartPane), 1000, 650);
        Stage stage = new Stage();
        stage.setTitle("Blackout Simulation - Energy Mix");
        stage.setScene(scene);
        stage.show();

        Platform.runLater(() -> {
            Node demandLine = lineChart.lookup(".chart-series-line");
            if (demandLine != null) {
                demandLine.setStyle("-fx-stroke: black; -fx-stroke-width: 2px;");
            }

            Node chartBackground = lineChart.lookup(".chart-plot-background");
            if (chartBackground != null) {
                chartBackground.setStyle("-fx-background-color: transparent;");
            }

            int i = 0;
            for (XYChart.Series<Number, Number> series : areaChart.getData()) {
                String colorClass = ".default-color" + i;
                Node fill = areaChart.lookup(colorClass + ".chart-series-area-fill");
                Node line = areaChart.lookup(colorClass + ".chart-series-area-line");

                if (fill != null) fill.setStyle("-fx-opacity: 0.4;");
                if (line != null) line.setStyle("-fx-stroke-width: 0.5px;");
                i++;
            }
        });
    }

}
