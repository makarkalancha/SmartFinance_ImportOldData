package com.makco.smartfinance.utils;

import com.makco.smartfinance.utils.notation.ReversePolishNotation2;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * User: Makar Kalancha
 * Date: 21/06/2016
 * Time: 22:41
 */
public class ReversePolishNotationTest {

    @Test
    public void test_convert_1() throws Exception{
        String formula = "1+2";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator());
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_convert_2() throws Exception{
        String formula = "1+2-3";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator());
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 + 3 -";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_convert_3() throws Exception{
        String formula = "1*2/3";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator());
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 * 3 /";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_convert_4() throws Exception{
        String formula = "1+2*3";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator());
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 3 * +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_convert_5() throws Exception{
        String formula = "1*2+3";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator());
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 * 3 +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_convert_6() throws Exception{
        String formula = "1*(2+3)";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator());
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 3 + *";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_convert_7() throws Exception{
        String formula = "1*2+3*4";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator());
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 * 3 4 * +";
        assertEquals(expectedResult, factResult);
    }

//    @Test
//    public void test_equalString() throws Exception{
//        String nullString = null;
//        boolean equal = nullString.equals("something");
//        assertEquals(false, equal);
//    }

    @Test
    public void test_convert_8() throws Exception{
        String formula = "(1+2)*(3-4)";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator());
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 + 3 4 - *";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_convert_9() throws Exception{
        String formula = "((1+2)*3)-4";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator());
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 + 3 * 4 -";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_convert_10() throws Exception{
        String formula = "1+2*(3-4/(5+6))";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator());
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 3 4 5 6 + / - * +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_convert_11() throws Exception{
        String formula = "11+12*(13-14/(15+16))";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator());
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "11 12 13 14 15 16 + / - * +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_convert_12_invalid_formula() throws Exception{
        String formula = "1+*2";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator());
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "11 12 13 14 15 16 + / - * +";
        assertEquals(expectedResult, factResult);
    }

}