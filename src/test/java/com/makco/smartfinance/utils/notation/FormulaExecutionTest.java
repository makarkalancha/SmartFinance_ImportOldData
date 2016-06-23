package com.makco.smartfinance.utils.notation;

import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import static org.junit.Assert.*;

/**
 * Created by mcalancea on 23 Jun 2016.
 */
public class FormulaExecutionTest {
    @Test
    public void test1() throws Exception{
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        String formula = "40+2";
        int result = (Integer) engine.eval(formula);
        assertEquals(42, result);
    }

    @Test
    public void test2_invalid_formula() throws Exception{
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        String formula = "40+*2";
        int result = (Integer) engine.eval(formula);
        assertEquals(42, result);
    }

    @Test
    public void test3() throws Exception{
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        String formula = "10/3";
        Object result = engine.eval(formula);
        System.out.println(result);
//        assertEquals(10d, result, 0.0001);
    }

    @Test
    public void test3_Exception() throws Exception{
        FormulaExecution<Double> formulaExecution = new FormulaExecution<>("2*3");
        double result = formulaExecution.getResult();
        assertEquals(6d, result, 0.00001);
    }

}