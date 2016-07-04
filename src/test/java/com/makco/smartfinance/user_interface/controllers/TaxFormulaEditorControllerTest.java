package com.makco.smartfinance.user_interface.controllers;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.utils.TestPersistenceManager;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import static org.junit.Assert.*;

/**
 * User: Makar Kalancha
 * Date: 19/06/2016
 * Time: 19:31
 */
public class TaxFormulaEditorControllerTest {
    private static final double DELTA = 0.0000001;
    private static ScriptEngine scriptEngine;

    @BeforeClass
    public static void setUpClass() throws Exception {
//        scriptEngine = new NashornScriptEngine();
        scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
    }

    @Test
    public void test_nashorn_addition() throws Exception{
        String formula1 = "10+2";
        int result = (Integer) scriptEngine.eval(formula1);
        assertEquals(12, result);
    }

    @Test
    public void test_nashorn_substraction() throws Exception{
        String formula1 = "10-2";
        int result = (Integer) scriptEngine.eval(formula1);
        assertEquals(8, result);
    }

    @Test
    public void test_nashorn_multiplication() throws Exception{
        String formula1 = "10*2";
        int result = (Integer) scriptEngine.eval(formula1);
        assertEquals(20, result);
    }

    @Test
    public void test_nashorn_division() throws Exception{
        String formula1 = "5/2";
        double result = (Double) scriptEngine.eval(formula1);
        assertEquals(2.5, result, DELTA);
    }

    @Test
    public void test_placeholder_replacement() throws Exception{
//        String newPlaceholder = DataBaseConstants.TAX_CHILD_ID_PLACEHOLDER.replace("%d","\\d+");
        String newPlaceholder = "\\{TAX\\d+\\}";
        String formula = "{TAX123}sdf";
        String result = formula.replaceAll(newPlaceholder, "");
        assertEquals("sdf", result);
    }

}