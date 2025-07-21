package edu.uoc.uoctron;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class UOCtron extends Application {

    /**
     * Window/stage's width.
     */
    public final static int WIDTH = 650;

    /**
     * Window/stage's height.
     */
    public final static int HEIGHT = 500;

    /**
     * Project's title.
     */
    private final static String TITLE = "UOCtron | PRAC 2024-2";

    /**
     * It is a reference to this class so that other classes related to the different views can use it.
     */
    public static UOCtron main;

    /**
     * It is the component where the GUI is displayed.
     */
    private Stage stage;

    /**
     * Main method
     * @param args This param is by default, but it is not used.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Override method.
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException When there is an error while loading the FXML file.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        main = this;
        stage = primaryStage;
        stage.setResizable(false);
        stage.setTitle(UOCtron.TITLE);
        stage.getIcons().addAll(
                new Image(Objects.requireNonNull(getClass().getResource("/images/favicon/favicon-16x16.png")).toExternalForm()),
                new Image(Objects.requireNonNull(getClass().getResource("/images/favicon/favicon-32x32.png")).toExternalForm()),
                new Image(Objects.requireNonNull(getClass().getResource("/images/favicon/favicon-64x64.png")).toExternalForm())
        );
        goScene("main");
    }

    /**
     * It shows the FXML view that is indicated.
     *
     * @param view Name of the FXML file.
     * @throws IOException When there is an error while loading the FXML file.
     */
    public void goScene(String view) throws IOException {
        // Load root layout from fxml file and show the scene containing the root layout.
        try{
            FXMLLoader loader = new FXMLLoader(UOCtron.class.getResource("/fxml/" + view + ".fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML resource not found: " + view);
            }
            Scene scene = new Scene(loader.load(), WIDTH, HEIGHT);
            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            System.err.println("______ERROR________" + e.getMessage());
        }

    }

}
