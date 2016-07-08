package com.makco.smartfinance.utils;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.utils.notation.ReversePolishNotation;
import org.apache.commons.lang3.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by mcalancea on 2016-06-09.
 */
public class BigDecimalUtils {
    private static DecimalFormat numberFormat = (DecimalFormat) DecimalFormat.getInstance();
    private static DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
    private static final char DEFAULT_GROUPING_SEPARATOR = ',';
    private static char groupingSeparator = ',';
    private static final char DEFAULT_DECIMAL_SEPARATOR = '.';
    private static char decimalSeparator = '.';
    private static ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

    /*
     * http://stackoverflow.com/questions/12711493/custom-formatted-number-with-period-and-comma
     * Commas are always used for grouping in the pattern definition, regardless of how they appear in the target locale.
     * Similarly, periods are always used for decimals. This appears illogical, but one must remember this is a pattern
     * definition, and like #, these characters describe a replacement, not the character itself.
     */
    private static String pattern = "#,##0.0#";
    private static DecimalFormat decimalFormat;
    static {
        /*
         * http://stackoverflow.com/questions/4713166/decimal-separator-in-numberformat
         * http://stackoverflow.com/questions/18231802/how-can-i-parse-a-string-to-bigdecimal
         */
        groupingSeparator = numberFormat.getDecimalFormatSymbols().getGroupingSeparator();
        decimalFormatSymbols.setGroupingSeparator(groupingSeparator);
        decimalSeparator = numberFormat.getDecimalFormatSymbols().getDecimalSeparator();
        decimalFormatSymbols.setDecimalSeparator(decimalSeparator);

        decimalFormat = new DecimalFormat(pattern, decimalFormatSymbols);
        decimalFormat.setParseBigDecimal(true);
    }

    public static BigDecimal convertStringToBigDecimal(String string) throws Exception{
        if(StringUtils.isEmpty(string)){
            return new BigDecimal("0");
        }
        return (BigDecimal) decimalFormat.parse(string);
    }

    public static BigDecimal convertStringToBigDecimal(String string, int scale) throws Exception{
        BigDecimal bigDecimal = convertStringToBigDecimal(string);
        bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return bigDecimal;
    }

    public static BigDecimal roundBigDecimal(String string, int scale) throws Exception{
        String newString = string;
        BigDecimal bigDecimal = new BigDecimal(newString);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
//        return bigDecimal;
    }

    public static String formatDecimalNumber(BigDecimal bigDecimal/*, int scale*/) throws Exception{
        return decimalFormat.format(bigDecimal);
    }

    public static char getDefaultDecimalSeparator() {
        return DEFAULT_DECIMAL_SEPARATOR;
    }

    public static char getDefaultGroupingSeparator() {
        return DEFAULT_GROUPING_SEPARATOR;
    }

    public static char getDecimalSeparator() {
        return decimalSeparator;
    }

    public static char getGroupingSeparator() {
        return groupingSeparator;
    }

    /**
     * calculate denomalizedFormula using Reverse Polish Notation, like (11+22)*31-42/(555+67)->"11 22 + 31 * 42 555 67 + / -"
     * @param denomalizedFormula is a formula with only one placeholder {NUM}
     * @param number big decimal
     * @return big decimal
     */
    public static BigDecimal calculateFormulaRPN(String denomalizedFormula, BigDecimal number){
        BigDecimal result = new BigDecimal("0");
        String mathExpressionToCalculate = denomalizedFormula.replace(DataBaseConstants.TAX_NUMBER_PLACEHOLDER, number.toString());
        ReversePolishNotation rpn = new ReversePolishNotation(mathExpressionToCalculate, BigDecimalUtils.getDecimalSeparator(),
                UserInterfaceConstants.SCALE);
        result = rpn.evaluateReversePolishNotation();
        return result;
    }

    /**
     * calculate denomalizedFormula using java 8 javascript plugin NASHORN
     * @param denomalizedFormula is a formula with only one placeholder {NUM}
     * @param number big decimal
     * @return big decimal
     * @throws Exception ScriptException by eval,
     */
    public static BigDecimal calculateFormulaNashorn(String denomalizedFormula, BigDecimal number) throws Exception{
        BigDecimal result = new BigDecimal("0");
        String mathExpressionToCalculate = denomalizedFormula.replace(DataBaseConstants.TAX_NUMBER_PLACEHOLDER, number.toString());
        Object nashornResult = scriptEngine.eval(mathExpressionToCalculate);
        result = BigDecimalUtils.roundBigDecimal(nashornResult.toString(),
                UserInterfaceConstants.SCALE);
        return result;
    }
}
