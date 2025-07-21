package edu.uoc.uoctron.view;

import edu.uoc.uoctron.UOCtron;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainViewController {

    /**
     * It goes to the "play" scene.
     */
    @FXML
    public void newLogin(){
        try{
            UOCtron.main.goScene("play");
        }catch(IOException e){
            System.exit(1);
        }
    }

    /**
     * It goes to the "credits" scene.
     */
    @FXML
    public void readCredits(){
        try{
            UOCtron.main.goScene("credits");
        }catch(IOException e){
            System.exit(1);
        }
    }

}
