package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.utils.BigDecimalUtils;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Set<Tax> childTaxes = new HashSet<>();

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
        okBtn.setDisable(true);
        charsLbl.setText(UserInterfaceConstants.TAX_FORMULA_VALID_CHARACTERS);

        formulaTA.textProperty().addListener((observable, oldValue, newValue) -> {
//            LOG.debug(oldValue + "->" + newValue);

            String tmp = newValue.replace(DataBaseConstants.TAX_NUMBER_PLACEHOLDER, "");
            tmp = tmp.replace(DataBaseConstants.TAX_RATE_PLACEHOLDER, "");
            tmp = tmp.replace(DataBaseConstants.TAX_RATE_PLACEHOLDER, "");
            tmp = tmp.replaceAll(DataBaseConstants.TAX_CHILD_ID_PLACEHOLDER_PATTERN, "");

            Region reg = (Region) formulaTA.lookup(".content");
            if (reg != null) {
                if (StringUtils.containsOnly(tmp, FORMULA_VALID_CHARS)) {
                    validateBtn.setDisable(false);

//                    LOG.debug("new is valid->" + newValue);
//                    LOG.debug("new is valid->" + reg.getStyle());

                    reg.setStyle("");
                } else {
                    validateBtn.setDisable(true);

//                    LOG.debug("new is NOT valid->" + newValue);

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
//                    LOG.debug(">>>" + oldValue + ": " + newValue);
                    formulaTA.appendText(
                            DataBaseConstants.getTaxChildIdPlaceholder(newValue.getId())
                    );
                    formulaTA.requestFocus();

                    childTaxes.add(newValue);
//                    LOG.debug(">>>childTaxes->" + childTaxes);
                }
        );
    }

    public void setDialogStage(Stage dialogStage, Tax tax, List<Tax> taxes) {
        this.dialogStage = dialogStage;
        this.tax = tax;
        this.taxes = taxes;
        formulaTA.setText(tax.getFormula());
        taxLV.setItems(FXCollections.observableArrayList(taxes));
        childTaxes = new HashSet<>(tax.getChildTaxes());

//        LOG.debug(">>>tax.id->" + tax.getId());
//        LOG.debug(">>>taxes->" + taxes);
    }


    public String getFormula(){
        return formulaTA.getText();
    }

    public String getDenormalizedFormula(){
        return tax.getDenormalizedFormula();
    }

    public Set<Tax> getChildTaxes(){
        return childTaxes;
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
    public void onRateBtn(ActionEvent event) {
        formulaTA.appendText(DataBaseConstants.TAX_RATE_PLACEHOLDER);
        formulaTA.requestFocus();
    }

    private void fillChildTaxes() throws Exception {
        Pattern patternFormula = Pattern.compile(DataBaseConstants.TAX_CHILD_ID_PLACEHOLDER_PATTERN);
        Matcher matcherFormula = patternFormula.matcher(formulaTA.getText());

        childTaxes = new HashSet<>();
        while (matcherFormula.find()) {
            long taxId = DataBaseConstants.getTaxChildId(matcherFormula.group());
            Tax tmpTax = taxes.stream()
                    .filter(tax -> tax.getId().equals(taxId))
                    .findFirst()
                    .orElse(null);
            if(tmpTax != null){
                childTaxes.add(tmpTax);
            }
        }
    }

    @FXML
    public void onValidateBtn(ActionEvent event){
        try {
            /*
            todo denormolize formula: if you type {TAX1} without clicking on listview child list will be empty
            so add it explicitly
              */
            fillChildTaxes();
            tax.setChildTaxes(childTaxes);
            tax.setFormula(formulaTA.getText());

            BigDecimal number = BigDecimalUtils.convertStringToBigDecimal(numberTF.getText(), UserInterfaceConstants.SCALE);
            String editedFormulaForCalculation = tax.getDenormalizedFormula().replace(DataBaseConstants.TAX_NUMBER_PLACEHOLDER,
                    number.toString());
            String editedFormulaForUI = editedFormulaForCalculation;
            if(BigDecimalUtils.getGroupingSeparator() != BigDecimalUtils.getDefaultGroupingSeparator()) {
                editedFormulaForUI = editedFormulaForUI.replace(",", Character.toString(BigDecimalUtils.getGroupingSeparator()));
            }
            if(BigDecimalUtils.getDecimalSeparator() != BigDecimalUtils.getDefaultDecimalSeparator()) {
                editedFormulaForUI = editedFormulaForUI.replace(".", Character.toString(BigDecimalUtils.getDecimalSeparator()));
            }

            long start1 = System.nanoTime();
            BigDecimal resultTaxCalculateFormula = BigDecimalUtils.calculateFormulaRPN(tax.getDenormalizedFormula(), number);
            long end1 = System.nanoTime();

            long start2 = System.nanoTime();
            BigDecimal resultTaxCalculateFormulaWihNashorn = BigDecimalUtils.calculateFormulaNashorn(tax.getDenormalizedFormula(), number);
            long end2 = System.nanoTime();

            StringBuilder resultSB = new StringBuilder();
            resultSB.append("tax.calculateFormula(number) ");
            resultSB.append(benchmarkCalcultaion(start1, end1));
            resultSB.append(":\n");
            resultSB.append(editedFormulaForUI);
            resultSB.append(" = ");
            resultSB.append(BigDecimalUtils.formatDecimalNumber(resultTaxCalculateFormula));

            resultSB.append("\n\ntax.calculateFormulaWihNashorn(number) ");
            resultSB.append(benchmarkCalcultaion(start2, end2));
            resultSB.append(":\n");
            resultSB.append(editedFormulaForUI);
            resultSB.append(" = ");
            resultSB.append(BigDecimalUtils.formatDecimalNumber(resultTaxCalculateFormulaWihNashorn));

            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
            ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");

            Object nashornResult = scriptEngine.eval(editedFormulaForCalculation);
            BigDecimal nashornResultBD = BigDecimalUtils.roundBigDecimal(nashornResult.toString(),
                    UserInterfaceConstants.SCALE);

            resultSB.append("\n------------------------------------------\nValidation check:\n");
            resultSB.append(BigDecimalUtils.formatDecimalNumber(resultTaxCalculateFormula));
            resultSB.append(" = ");
            resultSB.append(BigDecimalUtils.formatDecimalNumber(nashornResultBD));

            Region reg = (Region) validationResultTA.lookup(".content");
            if(resultTaxCalculateFormula.compareTo(nashornResultBD) == 0){
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

    private String benchmarkCalcultaion(long start, long end){
        long elapsed_string = end - start;
        long minutes = TimeUnit.NANOSECONDS.toMinutes(elapsed_string);
        long seconds = TimeUnit.NANOSECONDS.toSeconds(elapsed_string - TimeUnit.MINUTES.toNanos(minutes));
        long millis = TimeUnit.NANOSECONDS.toMillis(elapsed_string - TimeUnit.MINUTES.toNanos(minutes) - TimeUnit.SECONDS.toNanos(seconds));
        long nanos = elapsed_string - TimeUnit.MINUTES.toNanos(minutes) - TimeUnit.SECONDS.toNanos(seconds) - TimeUnit.MILLISECONDS.toNanos(millis);
        return String.format("[mm:ss:millis.nanos]: %s:%s:%s.%s", minutes, seconds, millis, nanos);
    }
}
