package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.MainScreens;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by mcalancea on 2016-04-01.
 */
public class Screen1Controller implements Initializable, ControlledScreen {
    private ScreensController screensController;

    @Override
    public void setScreenPage(ScreensController screenPage) {
        screensController = screenPage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void handleButtonAction(ActionEvent event){
        screensController.setScreen(MainScreens.SCREEN2);
    }

    @FXML
    public void toMain(ActionEvent event){
        screensController.setScreen(MainScreens.MAIN);
    }

    @Override
    public void refresh() {

    }

    @Override
    public boolean askPermissionToClose() {
        return true;
    }

    @Override
    public void close() {

    }
}
