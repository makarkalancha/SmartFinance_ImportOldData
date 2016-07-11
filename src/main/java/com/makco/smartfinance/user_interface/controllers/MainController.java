package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Makar Kalancha on 2016-04-01.
 */
public class MainController implements Initializable, ControlledScreen {
    private ScreensController screensController;

    @Override
    public void setScreenPage(ScreensController screenPage) {
        screensController = screenPage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
