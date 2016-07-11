package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.Main;
import com.makco.smartfinance.user_interface.Command;
import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.MainScreens;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.user_interface.undoredo.CareTaker;
import com.makco.smartfinance.user_interface.undoredo.UndoRedoScreen;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Makar Kalancha on 2016-04-01.
 */
public class MenuController implements Initializable, ControlledScreen {
    private final static Logger LOG = LogManager.getLogger(MenuController.class);
    private ScreensController screensController;
    private CareTaker careTaker;
    private UndoRedoScreen undoRedoScreen;

    private BooleanProperty isNotUndo = new SimpleBooleanProperty(true);
    public BooleanProperty isUndoEmpty = new SimpleBooleanProperty(true);
    public BooleanProperty isRedoEmpty = new SimpleBooleanProperty(true);

    private Command mi_Save;
    private Command mi_Undo;
    private Command mi_Redo;

    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private MenuItem undoMenuItem;
    @FXML
    private MenuItem redoMenuItem;
    @FXML
    private MenuItem quitMenuItem;

    @Override
    public void setScreenPage(ScreensController screenPage) {
        try {
            screensController = screenPage;
            careTaker = screensController.getCareTaker();
            mi_Save = screensController.getToolbar_Save();
            mi_Undo = screensController.getToolbar_Undo();
            mi_Redo = screensController.getToolbar_Redo();
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            saveMenuItem.setAccelerator(UserInterfaceConstants.SAVE_KC);
            undoMenuItem.setAccelerator(UserInterfaceConstants.UNDO_KC);
            redoMenuItem.setAccelerator(UserInterfaceConstants.REDO_KC);
            quitMenuItem.setAccelerator(UserInterfaceConstants.QUIT_KC);

////        https://docs.oracle.com/javase/8/javafx/properties-binding-tutorial/binding.htm
            isUndoEmpty.addListener((observable, oldValue, newValue) ->{
                LOG.debug("menu isUndoEmpty.addListener->oldValue: " + oldValue + "; newValue:" + newValue);
                undoMenuItem.setDisable(newValue);
            });

            isRedoEmpty.addListener((observable, oldValue, newValue) ->{
                LOG.debug("menu isRedoEmpty.addListener->oldValue: " + oldValue + "; newValue:" + newValue);
                redoMenuItem.setDisable(newValue);
            });
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void toScene1(ActionEvent event){
        screensController.setScreen(MainScreens.SCREEN1);
    }

    @FXML
    public void toScene2(ActionEvent event){
        screensController.setScreen(MainScreens.SCREEN2);
    }

    @FXML
    public void toMain(ActionEvent event){
        screensController.setScreen(MainScreens.MAIN);
    }

    @FXML
    public void toAccountManagement(ActionEvent event){
        screensController.setScreen(MainScreens.ACCOUNT_MANAGEMENT);
    }

    @FXML
    public void toCategoryManagement(ActionEvent event){
        screensController.setScreen(MainScreens.CATEGORY_MANAGEMENT);
    }

    @FXML
    public void toCurrency(ActionEvent event){
        screensController.setScreen(MainScreens.CURRENCY);
    }

    @FXML
    public void toFamilyMember(ActionEvent event){
        screensController.setScreen(MainScreens.FAMILY_MEMBER);
    }

    @FXML
    public void toOrganization(ActionEvent event){
        screensController.setScreen(MainScreens.ORGANIZATION);
    }

    @FXML
    public void toTax(ActionEvent event){
        screensController.setScreen(MainScreens.TAX);
    }

    @FXML
    public void quit(ActionEvent event) {
        Main.quit(event);
    }

    @Override
    public void refresh() {
        try{
            mi_Save = screensController.getToolbar_Save();
            mi_Undo = screensController.getToolbar_Undo();
            mi_Redo = screensController.getToolbar_Redo();

            isUndoEmpty.bind(careTaker.isUndoEmptyProperty());
            isRedoEmpty.bind(careTaker.isRedoEmptyProperty());

            saveMenuItem.setDisable(mi_Save == null);
            undoMenuItem.setDisable(mi_Undo == null || isUndoEmpty.getValue());
            redoMenuItem.setDisable(mi_Redo == null || isRedoEmpty.getValue());
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public boolean askPermissionToClose() {
        return false;
    }

    @Override
    public void close() {

    }

    @FXML
    public void onSave(ActionEvent event){
        try{
            mi_Save.execute();
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onUndo(ActionEvent event){
        try{
            isNotUndo.setValue(false);
            LOG.debug("\n\n>>>>>>>>click undo");
            mi_Undo.execute();
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onRedo(ActionEvent event){
        try{
            isNotUndo.setValue(false);
            LOG.debug("\n\n>>>>>>>>click redo");
            mi_Redo.execute();
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }
}
