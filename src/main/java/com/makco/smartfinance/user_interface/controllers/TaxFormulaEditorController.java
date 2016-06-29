package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.utils.BigDecimalUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private Button rateBtn;
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
        formulaTA.appendText(DataBaseConstants.TAX_NUMBER_PLACEHOLDER);
    }

    @FXML
    public void onRateBtn(ActionEvent event){
        formulaTA.appendText(DataBaseConstants.TAX_RATE_PLACEHOLDER);
    }

    //todo validation instead of true
    private boolean isValid(){
        String formula = formulaTA.getText();
        return !StringUtils.isEmpty(formula);
    }
}
