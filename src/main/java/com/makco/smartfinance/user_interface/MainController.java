package com.makco.smartfinance.user_interface;

import com.makco.smartfinance.Main;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * Created by mcalancea on 2016-04-01.
 */
public class MainController implements Initializable, ControlledScreen {
    ScreensController myController;

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }

    @FXML
    public void toScene1(ActionEvent event){
        myController.setScreen(Main.screen1ID);
    }

    @FXML
    public void toScene2(ActionEvent event){
        myController.setScreen(Main.screen2ID);
    }

    @FXML
    public void quit(ActionEvent event){
        Platform.exit();
        System.exit(0);
    }
}
