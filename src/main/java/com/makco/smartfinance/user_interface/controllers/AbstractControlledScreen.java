package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.user_interface.ControlledScreen;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.user_interface.undoredo.CareTaker;
import com.makco.smartfinance.user_interface.undoredo.UndoRedoScreen;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.utils.collection.CollectionUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: Makar Kalancha
 * Date: 12/06/2016
 * Time: 15:58
 */
public abstract class AbstractControlledScreen implements Initializable, ControlledScreen, UndoRedoScreen {
    private final static Logger LOG = LogManager.getLogger(AbstractControlledScreen.class);

    protected ScreensController screensController;
    protected ActionEvent actionEvent;

    protected Map<ErrorEnum, Control> errorControlDictionary = new EnumMap<ErrorEnum, Control>(ErrorEnum.class);
    protected Set<Control> erroneousControlSet = new HashSet<>();

    protected CareTaker careTaker;
    protected BooleanProperty isNotUndo = new SimpleBooleanProperty(true);

    @Override
    public void setScreenPage(ScreensController screenPage) {
        try {
            screensController = screenPage;
            careTaker = screensController.getCareTaker();
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    protected void clearErrorHighlight(){
        erroneousControlSet.forEach(control -> {
            if(control instanceof TextArea){
                Region reg = (Region) control.lookup(".content");
                //version 1 using getStyleClass().add, that is not working in listener (tax formula editor, formulaTA)
                //reg.getStyleClass().remove(UserInterfaceConstants.INVALID_CONTROL_CSS_CLASS);

                //version 2 using setStyle(), only this approach is working in listener (tax formula editor, formulaTA)
                reg.setStyle("");
            } else if(control instanceof ComboBox){
//                LOG.debug(">>>clearErrorHighlight before control.getStyleClass(): " + control.getStyleClass());
                TextField reg = ((ComboBox) control).getEditor();
                //version 1 using getStyleClass().add, that is not working in listener (tax formula editor, formulaTA)
                reg.getStyleClass().remove(UserInterfaceConstants.INVALID_CONTROL_CSS_CLASS);

//                //version 2 using setStyle(), only this approach is working in listener (tax formula editor, formulaTA)
//                reg.setStyle("");
            } else if(control instanceof TextField){
                //version 1 using getStyleClass().add, that is not working in listener (tax formula editor, formulaTA)
                control.getStyleClass().remove(UserInterfaceConstants.INVALID_CONTROL_CSS_CLASS);

//                //version 2 using setStyle(), only this approach is working in listener (tax formula editor, formulaTA)
//                control.setStyle("");
            }
        });
    }

    protected void highlightInvalidFields(EnumSet<ErrorEnum> errors){
        clearErrorHighlight();
//        LOG.debug(">>>highlightInvalidFields before nameTF.getStyleClass(): "+nameTF.getStyleClass());
//        nameTF.getStyleClass().remove(UserInterfaceConstants.INVALID_CONTROL_CSS_CLASS);
//        LOG.debug(">>>highlightInvalidFields after nameTF.getStyleClass(): "+nameTF.getStyleClass());
//
//        Region region = (Region) descTA.lookup(".content");
//        LOG.debug(">>>highlightInvalidFields before region.getStyleClass(): " + region.getStyleClass());
//        region.getStyleClass().remove(UserInterfaceConstants.INVALID_CONTROL_CSS_CLASS);
//        LOG.debug(">>>highlightInvalidFields after region.getStyleClass(): " + region.getStyleClass());
//
//        rateTF.getStyleClass().remove(UserInterfaceConstants.INVALID_CONTROL_CSS_CLASS);
//        startDP.getStyleClass().remove(UserInterfaceConstants.INVALID_CONTROL_CSS_CLASS);

        errors.forEach(error -> {
            Control control = errorControlDictionary.get(error);
            if(control != null){
                if(control instanceof TextArea){
                    Region reg = (Region) control.lookup(".content");
                    //version 1 using getStyleClass().add, that is not working in listener (tax formula editor, formulaTA)
                    reg.getStyleClass().add(UserInterfaceConstants.INVALID_CONTROL_CSS_CLASS);
                    reg.getStyleClass().setAll(CollectionUtils.convertCollectionToObservableSet(reg.getStyleClass()));

//                    //version 2 using setStyle(), only this approach is working in listener (tax formula editor, formulaTA)
//                    //removes red border around required fields
//                    reg.setStyle(UserInterfaceConstants.INVALID_CONTROL_BGCOLOR);
                } else if(control instanceof ComboBox){
                    TextField reg = ((ComboBox) control).getEditor();
                    //version 1 using getStyleClass().add, that is not working in listener (tax formula editor, formulaTA)
                    reg.getStyleClass().add(UserInterfaceConstants.INVALID_CONTROL_CSS_CLASS);
                    reg.getStyleClass().setAll(CollectionUtils.convertCollectionToObservableSet(reg.getStyleClass()));

//                    //version 2 using setStyle(), only this approach is working in listener (tax formula editor, formulaTA)
//                    //removes red border around required fields
//                    reg.setStyle(UserInterfaceConstants.INVALID_CONTROL_BGCOLOR);
                } else if(control instanceof TextField){
                    //version 1 using getStyleClass().add, that is not working in listener (tax formula editor, formulaTA)
                    control.getStyleClass().add(UserInterfaceConstants.INVALID_CONTROL_CSS_CLASS);
                    control.getStyleClass().setAll(CollectionUtils.convertCollectionToObservableSet(control.getStyleClass()));

//                    //version 2 using setStyle(), only this approach is working in listener (tax formula editor, formulaTA)
//                    //removes red border around required fields
//                    control.setStyle(UserInterfaceConstants.INVALID_CONTROL_BGCOLOR);
                }
            }
        });
    }
}
