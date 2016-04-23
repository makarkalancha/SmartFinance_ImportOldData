package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.user_interface.Command;
import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.undoredo.CareTaker;
import com.makco.smartfinance.user_interface.undoredo.UndoRedoScreen;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by mcalancea on 2016-04-01.
 */
public class ToolbarController implements Initializable, ControlledScreen {
    private final static Logger LOG = LogManager.getLogger(ToolbarController.class);
    private ScreensController myController;
    private CareTaker careTaker;
    private UndoRedoScreen undoRedoScreen;

    private BooleanProperty isNotUndo = new SimpleBooleanProperty(true);
    public BooleanProperty isUndoEmpty = new SimpleBooleanProperty(true);
    public BooleanProperty isRedoEmpty = new SimpleBooleanProperty(true);

    private Command toolbar_Save;
    private Command toolbar_Undo;
    private Command toolbar_Redo;

    @FXML
    private Button tbSaveBtn;
    @FXML
    private Button tbUndoBtn;
    @FXML
    private Button tbRedoBtn;

    @Override
    public void setScreenPage(ScreensController screenPage) {
        try {
            myController = screenPage;
            careTaker = myController.getCareTaker();
            toolbar_Save = myController.getToolbar_Save();
            toolbar_Undo = myController.getToolbar_Undo();
            toolbar_Redo = myController.getToolbar_Redo();
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            tbSaveBtn.setText("");
            tbUndoBtn.setText("");
            tbRedoBtn.setText("");
            tbSaveBtn.setGraphic(new ImageView(new Image(UserInterfaceConstants.SAVE_ICO)));
            tbUndoBtn.setGraphic(new ImageView(new Image(UserInterfaceConstants.UNDO_ICO)));
            tbRedoBtn.setGraphic(new ImageView(new Image(UserInterfaceConstants.REDO_ICO)));

////https://docs.oracle.com/javase/8/javafx/properties-binding-tutorial/binding.htm
            isUndoEmpty.addListener((observable, oldValue, newValue) ->{
                LOG.debug("isUndoEmpty.addListener->oldValue: " + oldValue + "; newValue:" + newValue);
                tbUndoBtn.setDisable(newValue);
            });

            isRedoEmpty.addListener((observable, oldValue, newValue) ->{
                LOG.debug("isRedoEmpty.addListener->oldValue: " + oldValue + "; newValue:" + newValue);
                tbRedoBtn.setDisable(newValue);
            });
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void refresh() {
        try{
            toolbar_Save = myController.getToolbar_Save();
            toolbar_Undo = myController.getToolbar_Undo();
            toolbar_Redo = myController.getToolbar_Redo();

            isUndoEmpty.bind(careTaker.isUndoEmptyProperty());
            isRedoEmpty.bind(careTaker.isRedoEmptyProperty());

            tbSaveBtn.setDisable(toolbar_Save == null);
            tbUndoBtn.setDisable(toolbar_Undo == null || isUndoEmpty.getValue());
            tbRedoBtn.setDisable(toolbar_Redo == null || isRedoEmpty.getValue());
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
             toolbar_Save.execute();
         }catch (Exception e){
             DialogMessages.showExceptionAlert(e);
         }
    }

    @FXML
    public void onUndo(ActionEvent event){
        try{
            isNotUndo.setValue(false);
            LOG.debug("\n\n>>>>>>>>click undo");
            toolbar_Undo.execute();
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }

    @FXML
    public void onRedo(ActionEvent event){
        try{
            isNotUndo.setValue(false);
            LOG.debug("\n\n>>>>>>>>click redo");
            toolbar_Redo.execute();
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }
}
