package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.Screens;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * Created by mcalancea on 2016-04-01.
 */
public class Screen2Controller implements Initializable, ControlledScreen {
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
    public void handleButtonAction(ActionEvent event){
        myController.setScreen(Screens.SCREEN1);
//        throw new IllegalArgumentException("hello bitch");
    }

    @FXML
    public void toMain(ActionEvent event){
        myController.setScreen(Screens.MAIN);
    }
}
