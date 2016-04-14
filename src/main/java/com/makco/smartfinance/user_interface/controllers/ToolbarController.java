package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.RefreshableScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.ApplicationConstants;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by mcalancea on 2016-04-01.
 */
public class ToolbarController implements Initializable, ControlledScreen, RefreshableScreen {
    private final static Logger LOG = LogManager.getLogger(ToolbarController.class);
    private ScreensController myController;

    @FXML
    private Button tbSaveBtn;
    @FXML
    private Button tbUndoBtn;
    @FXML
    private Button tbRedoBtn;

    @Override
    public void setScreenPage(ScreensController screenPage) {
        myController = screenPage;
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
}
