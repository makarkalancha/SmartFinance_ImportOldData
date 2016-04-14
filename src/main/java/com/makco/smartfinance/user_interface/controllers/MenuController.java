package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.RefreshableScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.ApplicationConstants;
import com.makco.smartfinance.user_interface.constants.Screens;
import com.makco.smartfinance.utils.ApplicationUtililities;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

/**
 * Created by mcalancea on 2016-04-01.
 */
public class MenuController implements Initializable, ControlledScreen, RefreshableScreen {
    private final static Logger LOG = LogManager.getLogger(MenuController.class);
    private ScreensController myController;

    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private MenuItem quitMenuItem;

    @Override
    public void setScreenPage(ScreensController screenPage) {
        myController = screenPage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveMenuItem.setAccelerator(ApplicationConstants.SAVE_KC);
        quitMenuItem.setAccelerator(ApplicationConstants.QUIT_KC);
    }

    @FXML
    public void toSave(ActionEvent event){
        LOG.debug("MenuController->toSave");
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
    public void toCurrency(ActionEvent event){
        myController.setScreen(Screens.CURRENCY);
    }

    @FXML
    public void toFamilyMember(ActionEvent event){
        myController.setScreen(Screens.FAMILY_MEMBER);
    }

    @FXML
    public void quit(ActionEvent event) {
        ApplicationUtililities.quit(event);
    }

    @Override
    public void refresh() {

    }
}
