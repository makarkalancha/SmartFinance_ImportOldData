package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.Screens;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * Created by mcalancea on 2016-04-01.
 */
public class MenuController implements Initializable, ControlledScreen {
    private ScreensController myController;

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
        myController.setScreen(Screens.SCREEN1);
    }

    @FXML
    public void toScene2(ActionEvent event){
        myController.setScreen(Screens.SCREEN2);
    }

    @FXML
    public void toMain(ActionEvent event){
        myController.setScreen(Screens.MAIN);
    }

    @FXML
    public void toFamilyMember(ActionEvent event){
        myController.setScreen(Screens.FAMILY_MEMBER);
    }

    @FXML
    public void quit(ActionEvent event){
        Platform.exit();
        System.exit(0);
    }
}
