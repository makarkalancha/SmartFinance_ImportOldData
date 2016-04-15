package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.Screens;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by mcalancea on 2016-04-01.
 */
public class Screen2Controller implements Initializable, ControlledScreen {
    private ScreensController myController;

    @Override
    public void setScreenPage(ScreensController screenPage) {
        myController = screenPage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }

    @FXML
    public void handleButtonAction(ActionEvent event){
        myController.setScreen(Screens.SCREEN1);
    }

    @FXML
    public void toMain(ActionEvent event){
        myController.setScreen(Screens.MAIN);
    }

    @Override
    public void refresh() {

    }

    @Override
    public boolean isCloseAllowed() {
        return false;
    }
}
