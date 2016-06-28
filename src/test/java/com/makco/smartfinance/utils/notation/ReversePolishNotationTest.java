package com.makco.smartfinance.utils.notation;

import com.makco.smartfinance.utils.BigDecimalUtils;
import com.makco.smartfinance.utils.notation.ReversePolishNotation2;
import org.junit.Test;

import java.math.BigDecimal;

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
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_1() throws Exception{
        String formula = "1+2";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("3"), factResult);
    }

    @Test
    public void test_convert_2() throws Exception{
        String formula = "1+2-3";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 + 3 -";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_2() throws Exception{
        String formula = "1+2-3";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("0"), factResult);
    }

    @Test
    public void test_convert_3() throws Exception{
        String formula = "1*2/3";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 * 3 /";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_3() throws Exception{
        String formula = "1*2/3";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("0.666667"), factResult);
    }

    @Test
    public void test_convert_4() throws Exception{
        String formula = "1+2*3";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 3 * +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_4() throws Exception{
        String formula = "1+2*3";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("7"), factResult);
    }

    @Test
    public void test_convert_5() throws Exception{
        String formula = "1*2+3";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 * 3 +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_5() throws Exception{
        String formula = "1*2+3";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("5"), factResult);
    }

    @Test
    public void test_convert_6() throws Exception{
        String formula = "1*(2+3)";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 3 + *";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_6() throws Exception{
        String formula = "1*(2+3)";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("5"), factResult);
    }

    @Test
    public void test_convert_7() throws Exception{
        String formula = "1*2+3*4";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 * 3 4 * +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_7() throws Exception{
        String formula = "1*2+3*4";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("14"), factResult);
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
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 + 3 4 - *";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_8() throws Exception{
        String formula = "(1+2)*(3-4)";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("-3"), factResult);
    }

    @Test
    public void test_convert_9() throws Exception{
        String formula = "((1+2)*3)-4";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 + 3 * 4 -";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_9() throws Exception{
        String formula = "((1+2)*3)-4";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("5"), factResult);
    }

    @Test
    public void test_convert_10() throws Exception{
        String formula = "1+2*(3-4/(5+6))";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 3 4 5 6 + / - * +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_10() throws Exception{
        String formula = "1+2*(3-4/(5+6))";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("6.272728"), factResult);
    }

    @Test
    public void test_convert_11() throws Exception{
        String formula = "11+12*(13-14/(15+16))";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "11 12 13 14 15 16 + / - * +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_11() throws Exception{
        String formula = "11+12*(13-14/(15+16))";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        /*
        not "161.580645", because 14 / 31 = 0.451613 and not 0.4516129032258065
         */
        assertEquals(new BigDecimal("161.580644"), factResult);
    }

    /*
    test_evaluate_12 returns exception, no need to fix RPN
    */
//    @Test
//    public void test_convert_12_invalid_formula() throws Exception{
//        String formula = "1+*2";
//        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
//        String factResult = rpn.convertToReversePolishNotation();
//        String expectedResult = "11 12 13 14 15 16 + / - * +";
//        assertEquals(expectedResult, factResult);
//    }

    @Test(expected = RuntimeException.class)
    public void test_evaluate_12() throws Exception{
        String formula = "1+*2";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        rpn.evaluateReversePolishNotation();
    }

    /*
     * todo read
     * http://www.ibm.com/developerworks/library/j-w3eval/
     * https://www.quora.com/How-do-you-write-a-Java-program-to-evaluate-a-given-arithmetic-expression-to-get-the-maximum-possible-answer
     * https://www.google.ca/search?q=java+8+execute+string+formulas&ie=utf-8&oe=utf-8&gws_rd=cr&ei=nT9sV7a_GcGte4j-mJgB#q=java+8+validate+arithmetic+expression
     */
    @Test
    public void test_convert_13() throws Exception{
        String formula = "(11+22)*31-42/(555+67)";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "11 22 + 31 * 42 555 67 + / -";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_13() throws Exception{
        String formula = "(11+22)*31-42/(555+67)";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("1022.932476"), factResult);
    }

    @Test
    public void testOperation_unioperation_1() throws Exception{
        int result = -3+-3;
        assertEquals(-6, result);
    }

    @Test
    public void test_convert_14() throws Exception{
        String formula = "-3+-3";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "-3 -3 +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_14() throws Exception{
        String formula = "-3+-3";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("-6"), factResult);
    }

    @Test
    public void testOperation_unioperation_2() throws Exception{
        int result = -3-+4;
        assertEquals(-7, result);
    }

    @Test
    public void test_convert_15() throws Exception{
        String formula = "-3-+4";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "-3 +4 -";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_15() throws Exception{
        String formula = "-3-+4";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("-7"), factResult);
    }

    @Test
    public void testOperation_unioperation_3() throws Exception{
        int result = -3-+-4;
        assertEquals(1, result);
    }

    /*
    test_evaluate_16 returns exception, no need to fix RPN
     */
//    @Test
//    public void test_convert_16() throws Exception{
//        String formula = "-3-+-4";
//        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
//        String factResult = rpn.convertToReversePolishNotation();
//        String expectedResult = "-3 +-4 -";
//        assertEquals(expectedResult, factResult);
//    }

    @Test(expected = RuntimeException.class)
    public void test_evaluate_16() throws Exception{
        String formula = "-3-+-4";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        rpn.evaluateReversePolishNotation();
    }

    @Test
    public void testOperation_unioperation_4() throws Exception{
        int result = -3+-+4;
        assertEquals(-7, result);
    }

    /*
    test_evaluate_17 returns exception, no need to fix RPN
     */
//    @Test
//    public void test_convert_17() throws Exception{
//        String formula = "-3+-+4";
//        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
//        String factResult = rpn.convertToReversePolishNotation();
//        String expectedResult = "-3 -+4 +";
//        assertEquals(expectedResult, factResult);
//    }

    @Test(expected = RuntimeException.class)
    public void test_evaluate_17() throws Exception{
        String formula = "-3+-+4";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        rpn.evaluateReversePolishNotation();
    }

    public void testOperation_unioperation_syntaxError_1() throws Exception{
        //int result = -3-++4;
    }

    /*
    test_evaluate_18 returns exception, no need to fix RPN
     */
//    @Test
//    public void test_convert_18() throws Exception{
//        String formula = "-3-++4";
//        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
//        String factResult = rpn.convertToReversePolishNotation();
//        String expectedResult = "-3 ++4 -";
//        assertEquals(expectedResult, factResult);
//    }

    @Test(expected = RuntimeException.class)
    public void test_evaluate_18() throws Exception{
        String formula = "-3-++4";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        rpn.evaluateReversePolishNotation();
    }

    public void testOperation_unioperation_syntaxError_2() throws Exception{
//        int result = -3+--4;
    }

    /*
    test_evaluate_19 returns exception, no need to fix RPN
     */
//    @Test
//    public void test_convert_19() throws Exception{
//        String formula = "-3+--4";
//        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
//        String factResult = rpn.convertToReversePolishNotation();
//        String expectedResult = "-3 --4 +";
//        assertEquals(expectedResult, factResult);
//    }

    @Test(expected = RuntimeException.class)
    public void test_evaluate_19() throws Exception{
        String formula = "-3+--4";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        rpn.evaluateReversePolishNotation();
    }

    public void testOperation_unioperation_syntaxError_3() throws Exception{
//        int result = -3**4;
    }

    /*
    test_evaluate_20 returns exception, no need to fix RPN
     */
//    @Test
//    public void test_convert_20() throws Exception{
//        String formula = "-3**4";
//        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
//        String factResult = rpn.convertToReversePolishNotation();
//        String expectedResult = "-3 *4 *";
//        assertEquals(expectedResult, factResult);
//    }

    @Test(expected = RuntimeException.class)
    public void test_evaluate_20() throws Exception{
        String formula = "-3**4";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        rpn.evaluateReversePolishNotation();
    }

    public void testOperation_unioperation_syntaxError_4() throws Exception{
//        int result = -3//4;
    }

    /*
    test_evaluate_21 returns exception, no need to fix RPN
     */
//    @Test
//    public void test_convert_21() throws Exception{
//        String formula = "-3//4";
//        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
//        String factResult = rpn.convertToReversePolishNotation();
//        String expectedResult = "-3 /4 /";
//        assertEquals(expectedResult, factResult);
//    }

    @Test(expected = RuntimeException.class)
    public void test_evaluate_21() throws Exception{
        String formula = "-3//4";
        ReversePolishNotation2 rpn = new ReversePolishNotation2(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        rpn.evaluateReversePolishNotation();
    }

}