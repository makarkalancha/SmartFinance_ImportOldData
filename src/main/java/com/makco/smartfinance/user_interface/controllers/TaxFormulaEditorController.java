package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.utils.BigDecimalUtils;
import com.makco.smartfinance.utils.collection.CollectionUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.util.List;

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
    private Tax tax;
    private List<Tax> taxes;
    private boolean isOkClicked = false;

    @FXML
    private TextArea formulaTA;
    @FXML
    private Button okBtn;
    @FXML
    private Button operandBtn;
    @FXML
    private Button rateBtn;
    @FXML
    private Button validateBtn;
    @FXML
    private Label charsLbl;
    @FXML
    private TextField numberTF;
    @FXML
    private TextArea validationResultTA;
    @FXML
    private ListView<Tax> taxLV;

    @FXML
    private void initialize(){
        charsLbl.setText(UserInterfaceConstants.TAX_FORMULA_VALID_CHARACTERS);

        formulaTA.textProperty().addListener((observable, oldValue, newValue) -> {
            /*
            todo enter only allowed characters and block buttons OK and VALIDATE when error
            color still is needed because you cannot type but paste an invalid formula
            you can
            */

            LOG.debug(oldValue + "->" + newValue);
//            LOG.debug(newValue.matches(FORMULA_PATTERN));

            String tmp = newValue.replace(DataBaseConstants.TAX_NUMBER_PLACEHOLDER, "");
            tmp = tmp.replace(DataBaseConstants.TAX_RATE_PLACEHOLDER, "");
            //!!!todo put TAX_CHILD_ID_PLACEHOLDER
            tmp = tmp.replace(DataBaseConstants.TAX_RATE_PLACEHOLDER, "");
            tmp = tmp.replaceAll(DataBaseConstants.TAX_CHILD_ID_PLACEHOLDER_PATTERN, "");

            Region reg = (Region) formulaTA.lookup(".content");
            if (reg != null) {
                if (StringUtils.containsOnly(tmp, FORMULA_VALID_CHARS)) {
                    //            if(tmp.length() == 0){
                    validateBtn.setDisable(false);
                    okBtn.setDisable(false);

                    LOG.debug("new is valid->" + newValue);
                    LOG.debug("new is valid->" + reg.getStyle());

                    reg.setStyle("");
                } else {
                    validateBtn.setDisable(true);
                    okBtn.setDisable(true);

                    LOG.debug("new is NOT valid->" + newValue);

                    reg.setStyle(UserInterfaceConstants.INVALID_CONTROL_BGCOLOR);

                }
            }
        });

        taxLV.setCellFactory(new Callback<ListView<Tax>, ListCell<Tax>>() {
            @Override
            public ListCell<Tax> call(ListView<Tax> param) {
                ListCell<Tax>listCell = new ListCell<Tax>(){

                    @Override
                    protected void updateItem(Tax item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            BigDecimal rate = item.getRate();
                            setText(
                                    new StringBuilder(item.getName())
                                            .append(" (")
                                            .append((rate == null) ? 0 : rate)
                                            .append("%)")
                                    .toString()
                            );
                        }
                    }
                };
                return listCell;
            }
        });

        taxLV.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    LOG.debug(">>>" + oldValue + ": " + newValue);
                    formulaTA.appendText(
                            DataBaseConstants.getTaxChildIdPlaceholder(newValue.getId())
                    );
                    formulaTA.requestFocus();
                }
        );
    }

    public void setDialogStage(Stage dialogStage, Tax tax, List<Tax> taxes) {
        this.dialogStage = dialogStage;
        this.tax = tax;
        this.taxes = taxes;
        formulaTA.setText(tax.getFormula());
        taxLV.setItems(FXCollections.observableArrayList(taxes));

        LOG.debug(">>>tax.id->" + tax.getId());
        LOG.debug(">>>taxes->" + taxes);
    }


    public String getFormula(){
        return formulaTA.getText();
    }

    public boolean isOkClicked(){
        return isOkClicked;
    }

    @FXML
    public void onOKButton(ActionEvent event){
        isOkClicked = true;
        dialogStage.close();
    }

    @FXML
    public void onOperandBtn(ActionEvent event){
        formulaTA.appendText(DataBaseConstants.TAX_NUMBER_PLACEHOLDER);
        formulaTA.requestFocus();
    }

    @FXML
    public void onRateBtn(ActionEvent event){
        formulaTA.appendText(DataBaseConstants.TAX_RATE_PLACEHOLDER);
        formulaTA.requestFocus();
    }

    @FXML
    public void onValidateBtn(ActionEvent event){
        try {
            String formula = formulaTA.getText();
            tax.setFormula(formula);

            String editedFormulaForCalculation = formula.replace(DataBaseConstants.TAX_RATE_PLACEHOLDER,
                    tax.getRate().toString());
            editedFormulaForCalculation = editedFormulaForCalculation.replace(DataBaseConstants.TAX_NUMBER_PLACEHOLDER,
                    numberTF.getText());

            String editedFormula = formula.replace(DataBaseConstants.TAX_RATE_PLACEHOLDER,
                    BigDecimalUtils.formatDecimalNumber(tax.getRate()));
            editedFormula = editedFormula.replace(DataBaseConstants.TAX_NUMBER_PLACEHOLDER, numberTF.getText());

            StringBuilder resultSB = new StringBuilder(editedFormula);
            resultSB.append(" = ");
            BigDecimal resultTaxCalculation = tax.calculateFormula(
                    BigDecimalUtils.convertStringToBigDecimal(numberTF.getText(), UserInterfaceConstants.SCALE));
            resultSB.append(BigDecimalUtils.formatDecimalNumber(resultTaxCalculation));

            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
            ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");
            Object nashornResult = scriptEngine.eval(editedFormulaForCalculation);
            BigDecimal nashornResultBD = BigDecimalUtils.roundBigDecimal(nashornResult.toString(),
                    UserInterfaceConstants.SCALE);

            resultSB.append("\n------------------------------------------\nValidation check:\n");
            resultSB.append(BigDecimalUtils.formatDecimalNumber(resultTaxCalculation));
            resultSB.append(" = ");
            resultSB.append(BigDecimalUtils.formatDecimalNumber(nashornResultBD));

            Region reg = (Region) validationResultTA.lookup(".content");
            if(resultTaxCalculation.compareTo(nashornResultBD) == 0){
                okBtn.setDisable(false);

                reg.setStyle("");
                resultSB.append("\nValidation PASSED.");
            } else {
                okBtn.setDisable(true);

                reg.setStyle(UserInterfaceConstants.INVALID_CONTROL_BGCOLOR);
                resultSB.append("\nValidation FAILED.");
            }
            validationResultTA.setText(resultSB.toString());
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }
}
