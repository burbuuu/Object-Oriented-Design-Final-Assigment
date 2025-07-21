package edu.uoc.uoctron.view;

import edu.uoc.uoctron.UOCtron;
import javafx.fxml.FXML;

import java.io.IOException;

public class CreditsViewController {

    /**
     * It goes to the "main" scene.
     */
    @FXML
    public void backMain(){
        try{
            UOCtron.main.goScene("main");
        }catch(IOException e){
            System.exit(1);
        }
    }

}
