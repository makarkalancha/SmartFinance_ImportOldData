package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.user_interface.Command;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.user_interface.models.TaxModel;
import com.makco.smartfinance.user_interface.undoredo.Memento;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.utility_screens.forms.ProgressIndicatorForm;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.utils.BigDecimalUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.ResourceBundle;

/**
 * Created by mcalancea on 2016-06-19.
 */
public class TaxFormulaEditorController {
    private final static Logger LOG = LogManager.getLogger(TaxFormulaEditorController.class);
    private final static String FORMULA_PATTERN = new StringBuilder()
            .append("[\\d\\/\\*\\+\\-\\(\\)")
            .append(BigDecimalUtils.getDecimalSeparator())
            .append("]+")
            .toString();
    private final static String FORMULA_VALID_CHARS = new StringBuilder()
            .append("1234567890/*+-()")
            .append(BigDecimalUtils.getDecimalSeparator())
            .toString();

    private Stage dialogStage;
//    private Tax tax;
    private boolean isOkClicked = false;

    @FXML
    private TextArea formulaTA;
    @FXML
    private Button okBtn;
    @FXML
    private Button numBtn;
    @FXML
    private Label charsLbl;

    @FXML
    private void initialize(){
        charsLbl.setText(UserInterfaceConstants.TAX_FORMULA_VALID_CHARACTERS);
        formulaTA.textProperty().addListener((observable, oldValue, newValue) -> {
            //todo enter only allowed characters
            LOG.debug(oldValue + "->" + newValue);
            LOG.debug(newValue.matches(FORMULA_PATTERN));
            if(StringUtils.containsOnly(newValue,FORMULA_VALID_CHARS)){
                LOG.debug("new is valid->" + newValue);
            }else{
                LOG.debug("new is NOT valid->" + newValue);
            }
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    public String getFormula(){
        return formulaTA.getText();
    }

    public boolean isOkClicked(){
        return isOkClicked;
    }

    @FXML
    public void onOKButton(ActionEvent event){
        if(isValid()){
            isOkClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void onNumBtn(ActionEvent event){
        /*
        todo impelemnt number placeholder: add something like {NUM} to formula
        which shall be replaced with real number before passing formula to
        ReversePolishNotation
         */
    }



    //todo validation instead of true
    private boolean isValid(){
        String formula = formulaTA.getText();
        return !StringUtils.isEmpty(formula);
    }
}
