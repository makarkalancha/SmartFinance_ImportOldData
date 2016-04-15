package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.ApplicationConstants;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.unredo.CareTaker;
import com.makco.smartfinance.user_interface.unredo.UndoRedoScreen;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

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
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LOG.debug(String.format("SAVE_ICO: %s; UNDO_ICO: %s; REDO_ICO: %s;", ApplicationConstants.SAVE_ICO, ApplicationConstants.UNDO_ICO,
                ApplicationConstants.REDO_ICO));
//        tbSaveBtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(ApplicationConstants.SAVE_ICO))));
//        tbUndoBtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(ApplicationConstants.UNDO_ICO))));
//        tbRedoBtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(ApplicationConstants.REDO_ICO))));
        tbSaveBtn.setText("");
        tbUndoBtn.setText("");
        tbRedoBtn.setText("");
        tbSaveBtn.setGraphic(new ImageView(new Image(ApplicationConstants.SAVE_ICO)));
        tbUndoBtn.setGraphic(new ImageView(new Image(ApplicationConstants.UNDO_ICO)));
        tbRedoBtn.setGraphic(new ImageView(new Image(ApplicationConstants.REDO_ICO)));

        //        https://docs.oracle.com/javase/8/javafx/properties-binding-tutorial/binding.htm
        tbUndoBtn.setOnAction((event) -> {
            isNotUndo.setValue(false);
            LOG.debug("\n\n>>>>>>>>click undo");
////            String undo = (String) cakeTaker.undoState();
////            Property property = myTextFieldProperty;
////            property.setValue(undo);
//            State state = cakeTaker.undoState();
//            if(state != null) {
//                Property property = mapProperty.get(state.getPropertyFieldName());
//                property.setValue(state.getValue());
//            }

//            restoreForm();
            myController.getNodeControllerBundle().reFormState(cakeTaker.undoState());
//            FormSnapshot fs = cakeTaker.undoState();
//            mapProperty.forEach((k, v) -> {
//                        System.out.println(k + "->" + v);
//                        System.out.println("fs.getElementByPropertyName(k):" + fs.getElementByPropertyName(k));
//                        System.out.println("fs.getElementByPropertyName(k).getElementState():" + fs.getElementByPropertyName(k).getElementState());
//                        System.out.println("fs.getElementByPropertyName(k).getElementState().getState():" + fs.getElementByPropertyName(k).getElementState().getState());
//                        System.out.println("fs.getElementByPropertyName(k).getElementState().getState().getValue():" + fs.getElementByPropertyName(k).getElementState().getState().getValue());
//                        v.setValue(fs.getElementByPropertyName(k).getElementState().getState().getValue());
//                    }
//            );
        });

        tbRedoBtn.setOnAction((event) -> {
            isNotUndo.setValue(false);
            LOG.debug("\n\n>>>>>>>>click redo");
//            myTextFieldProperty.set((String) cakeTaker.redoState());
//            restoreForm();
            restoreFormState(cakeTaker.redoState());
        });
    }

//    @FXML
//    public void toScene1(ActionEvent event){
//        myController.setScreen(Screens.SCREEN1);
//    }
//
//    @FXML
//    public void toScene2(ActionEvent event){
//        myController.setScreen(Screens.SCREEN2);
//    }
//
//    @FXML
//    public void toMain(ActionEvent event){
//        myController.setScreen(Screens.MAIN);
//    }
//
//    @FXML
//    public void toCurrency(ActionEvent event){
//        myController.setScreen(Screens.CURRENCY);
//    }
//
//    @FXML
//    public void toFamilyMember(ActionEvent event){
//        myController.setScreen(Screens.FAMILY_MEMBER);
//    }
//
//    @FXML
//    public void quit(ActionEvent event) {
//        ApplicationUtililities.quit(event);
//    }

    @Override
    public void refresh() {

    }

    @Override
    public boolean isCloseAllowed() {
        return false;
    }
}
