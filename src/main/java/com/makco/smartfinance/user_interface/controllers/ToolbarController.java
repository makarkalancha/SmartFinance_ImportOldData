package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.user_interface.Command;
import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.ApplicationConstants;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.undoredo.CareTaker;
import com.makco.smartfinance.user_interface.undoredo.UndoRedoScreen;
import java.net.URL;
import java.util.ResourceBundle;
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
//        LOG.debug(String.format("SAVE_ICO: %s; UNDO_ICO: %s; REDO_ICO: %s;", ApplicationConstants.SAVE_ICO, ApplicationConstants.UNDO_ICO,
//                ApplicationConstants.REDO_ICO));
        tbSaveBtn.setText("");
        tbUndoBtn.setText("");
        tbRedoBtn.setText("");
        tbSaveBtn.setGraphic(new ImageView(new Image(ApplicationConstants.SAVE_ICO)));
        tbUndoBtn.setGraphic(new ImageView(new Image(ApplicationConstants.UNDO_ICO)));
        tbRedoBtn.setGraphic(new ImageView(new Image(ApplicationConstants.REDO_ICO)));

        //        https://docs.oracle.com/javase/8/javafx/properties-binding-tutorial/binding.htm
        tbSaveBtn.setOnAction((event) -> {
            toolbar_Save.execute();
        });

        tbUndoBtn.setOnAction((event) -> {
            isNotUndo.setValue(false);
            LOG.debug("\n\n>>>>>>>>click undo");
            toolbar_Undo.execute();
        });

        tbRedoBtn.setOnAction((event) -> {
            isNotUndo.setValue(false);
            LOG.debug("\n\n>>>>>>>>click redo");
            toolbar_Redo.execute();
        });
    }


    @Override
    public void refresh() {
        toolbar_Save = myController.getToolbar_Save();
        toolbar_Undo = myController.getToolbar_Undo();
        toolbar_Redo = myController.getToolbar_Redo();
        tbSaveBtn.setDisable(toolbar_Save == null);
        tbUndoBtn.setDisable(toolbar_Undo == null);
        tbRedoBtn.setDisable(toolbar_Redo == null);
    }

    @Override
    public boolean askPermissionToClose() {
        return false;
    }

    @Override
    public void close() {

    }
}
