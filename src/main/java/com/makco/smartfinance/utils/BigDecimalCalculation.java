package com.makco.smartfinance.utils;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.utils.notation.ReversePolishNotation;

import javax.persistence.Transient;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;

/**
 * User: Makar Kalancha
 * Date: 07/07/2016
 * Time: 20:14
 */
public class BigDecimalCalculation {
    private static ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

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
