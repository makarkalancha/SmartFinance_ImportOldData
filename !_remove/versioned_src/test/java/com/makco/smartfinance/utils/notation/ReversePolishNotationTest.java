package com.makco.smartfinance.utils.notation;

import com.makco.smartfinance.utils.BigDecimalUtils;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 21/06/2016
 * Time: 22:41
 */
public class ReversePolishNotationTest {

    @Test
    public void test_convert_1() throws Exception{
        String formula = "1+2";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_1() throws Exception{
        String formula = "1+2";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("3"), factResult);
    }

    @Test
    public void test_convert_2() throws Exception{
        String formula = "1+2-3";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 + 3 -";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_2() throws Exception{
        String formula = "1+2-3";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("0"), factResult);
    }

    @Test
    public void test_convert_3() throws Exception{
        String formula = "1*2/3";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 * 3 /";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_3() throws Exception{
        String formula = "1*2/3";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("0.666667"), factResult);
    }

    @Test
    public void test_convert_4() throws Exception{
        String formula = "1+2*3";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 3 * +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_4() throws Exception{
        String formula = "1+2*3";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("7"), factResult);
    }

    @Test
    public void test_convert_5() throws Exception{
        String formula = "1*2+3";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 * 3 +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_5() throws Exception{
        String formula = "1*2+3";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("5"), factResult);
    }

    @Test
    public void test_convert_6() throws Exception{
        String formula = "1*(2+3)";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 3 + *";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_6() throws Exception{
        String formula = "1*(2+3)";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("5"), factResult);
    }

    @Test
    public void test_convert_7() throws Exception{
        String formula = "1*2+3*4";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 * 3 4 * +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_7() throws Exception{
        String formula = "1*2+3*4";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
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
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 + 3 4 - *";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_8() throws Exception{
        String formula = "(1+2)*(3-4)";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("-3"), factResult);
    }

    @Test
    public void test_convert_9() throws Exception{
        String formula = "((1+2)*3)-4";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 + 3 * 4 -";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_9() throws Exception{
        String formula = "((1+2)*3)-4";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("5"), factResult);
    }

    @Test
    public void test_convert_10() throws Exception{
        String formula = "1+2*(3-4/(5+6))";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "1 2 3 4 5 6 + / - * +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_10() throws Exception{
        String formula = "1+2*(3-4/(5+6))";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        BigDecimal factResult = rpn.evaluateReversePolishNotation();
        assertEquals(new BigDecimal("6.272728"), factResult);
    }

    @Test
    public void test_convert_11() throws Exception{
        String formula = "11+12*(13-14/(15+16))";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "11 12 13 14 15 16 + / - * +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_11() throws Exception{
        String formula = "11+12*(13-14/(15+16))";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
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
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        rpn.evaluateReversePolishNotation();
    }

    @Test
    public void test_convert_13() throws Exception{
        String formula = "(11+22)*31-42/(555+67)";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "11 22 + 31 * 42 555 67 + / -";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_13() throws Exception{
        String formula = "(11+22)*31-42/(555+67)";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
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
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "-3 -3 +";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_14() throws Exception{
        String formula = "-3+-3";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
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
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        String factResult = rpn.convertToReversePolishNotation();
        String expectedResult = "-3 +4 -";
        assertEquals(expectedResult, factResult);
    }

    @Test
    public void test_evaluate_15() throws Exception{
        String formula = "-3-+4";
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
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
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
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
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
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
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
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
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
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
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
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
        ReversePolishNotation rpn = new ReversePolishNotation(formula, BigDecimalUtils.getDecimalSeparator(), 6);
        rpn.evaluateReversePolishNotation();
    }

    @Test
    public void smoke_test() throws Exception{
        int cycles = 100_000;
        Double[] doubleArray = {
                (double)(1+2),
                (double)(5+2),
                (double)(1+2-3),
                (double)(1*2/3),
                (double)(1+2*3),
                (double)(1*2+3),
                (double)(1*(2+3)),
                (double)(1*2+3*4),
                (double)((1+2)*(3-4)),
                (double)(((1+2)*3)-4),
                (double)(1+2*(3-4/(5+6))),
                (double)(11+12*(13-14/(15+16))),
                (double)((11+22)*31-42/(555+67)),
                (double)(-3+-3),
                (double)(-3-+4)
        };

        double sumDouble = 0d;
        System.out.println(">>>>double");
        long start_double = System.nanoTime();
        for (int i = 0; i < cycles; i++) {
            double result = doubleArray[i % doubleArray.length];
            sumDouble += result;
            if (i % 10_000 == 0) {
                System.out.println("j=" + (i % doubleArray.length) + " " + result + " -> i=" + i);
            }

        }
        long end_double = System.nanoTime();
        long elapsed_double = end_double - start_double;
        long minutes1 = TimeUnit.NANOSECONDS.toMinutes(elapsed_double);
        long seconds1 = TimeUnit.NANOSECONDS.toSeconds(elapsed_double - TimeUnit.MINUTES.toNanos(minutes1));
        long millis1 = TimeUnit.NANOSECONDS.toMillis(elapsed_double - TimeUnit.MINUTES.toNanos(minutes1) - TimeUnit.SECONDS.toNanos(seconds1));
        long nanos1 = elapsed_double - TimeUnit.MINUTES.toNanos(minutes1) - TimeUnit.SECONDS.toNanos(seconds1) - TimeUnit.MILLISECONDS.toNanos(millis1);

        String[] stringArray = {
                "1+2",
                "5+2",
                "1+2-3",
                "1*2/3",
                "1+2*3",
                "1*2+3",
                "1*(2+3)",
                "1*2+3*4",
                "(1+2)*(3-4)",
                "((1+2)*3)-4",
                "1+2*(3-4/(5+6))",
                "11+12*(13-14/(15+16))",
                "(11+22)*31-42/(555+67)",
                "-3+-3",
                "-3-+4"
        };

        BigDecimal sumBigDecimal = new BigDecimal("0");
        System.out.println(">>>>RPN");
        long start_string = System.nanoTime();
        for (int i = 0; i < cycles; i++) {
            ReversePolishNotation rpn = new ReversePolishNotation(stringArray[i % doubleArray.length],
                    BigDecimalUtils.getDecimalSeparator(),
                    6);
            BigDecimal result = rpn.evaluateReversePolishNotation();

            sumBigDecimal = sumBigDecimal.add(result);

            if (i % 10_000 == 0) {
                System.out.println("j=" + (i % doubleArray.length) + " " + result + " -> i=" + i);
            }

        }
        long end_string = System.nanoTime();
        long elapsed_string = end_string - start_string;
        long minutes2 = TimeUnit.NANOSECONDS.toMinutes(elapsed_string);
        long seconds2 = TimeUnit.NANOSECONDS.toSeconds(elapsed_string - TimeUnit.MINUTES.toNanos(minutes2));
        long millis2 = TimeUnit.NANOSECONDS.toMillis(elapsed_string - TimeUnit.MINUTES.toNanos(minutes2) - TimeUnit.SECONDS.toNanos(seconds2));
        long nanos2 = elapsed_string - TimeUnit.MINUTES.toNanos(minutes2) - TimeUnit.SECONDS.toNanos(seconds2) - TimeUnit.MILLISECONDS.toNanos(millis2);

        BigDecimal sumBigDecimalNashorn = new BigDecimal("0");
        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
        System.out.println(">>>>Nashorn");
        long start_stringNashorn = System.nanoTime();
        for (int i = 0; i < cycles; i++) {

            Object nashornResult = scriptEngine.eval(stringArray[i % doubleArray.length]);
            BigDecimal result = BigDecimalUtils.roundBigDecimal(nashornResult.toString(), 6);

            sumBigDecimalNashorn = sumBigDecimal.add(result);

            if (i % 10_000 == 0) {
                System.out.println("j=" + (i % doubleArray.length) + " " + result + " -> i=" + i);
            }

        }
        long end_stringNashorn = System.nanoTime();
        long elapsed_stringNashorn = end_stringNashorn - start_stringNashorn;
        long minutes2Nashorn = TimeUnit.NANOSECONDS.toMinutes(elapsed_stringNashorn);
        long seconds2Nashorn = TimeUnit.NANOSECONDS.toSeconds(elapsed_stringNashorn - TimeUnit.MINUTES.toNanos(minutes2Nashorn));
        long millis2Nashorn = TimeUnit.NANOSECONDS.toMillis(elapsed_stringNashorn - TimeUnit.MINUTES.toNanos(minutes2Nashorn) - TimeUnit.SECONDS.toNanos(seconds2Nashorn));
        long nanos2Nashorn = elapsed_stringNashorn - TimeUnit.MINUTES.toNanos(minutes2Nashorn) - TimeUnit.SECONDS.toNanos(seconds2Nashorn) - TimeUnit.MILLISECONDS.toNanos(millis2Nashorn);

        System.out.println(String.format("double array elapsed [mm:ss:millis.nanos]: %s:%s:%s.%s", minutes1, seconds1, millis1, nanos1));
        System.out.println(String.format("string RPN elapsed [mm:ss:millis.nanos]: %s:%s:%s.%s", minutes2, seconds2, millis2, nanos2));
        System.out.println(String.format("string Nashorn elapsed [mm:ss:millis.nanos]: %s:%s:%s.%s", minutes2Nashorn, seconds2Nashorn, millis2Nashorn, nanos2Nashorn));

        System.out.println(String.format("doubleSum vs bigDecimalSum vs sumBigDecimalNashorn: %s ~ %s ~ %s", sumDouble, sumBigDecimal, sumBigDecimalNashorn));
    }
}