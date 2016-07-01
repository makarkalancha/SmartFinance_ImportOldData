package com.makco.smartfinance.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by mcalancea on 2016-06-09.
 */
public class BigDecimalUtils {
    private static DecimalFormat numberFormat = (DecimalFormat) DecimalFormat.getInstance();
    private static DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
    private static char groupingSeparator = ',';
    private static char decimalSeparator = '.';
    private static int bigDecimalScaleOf_2 = 2;
    private static int bigDecimalScaleOf_4 = 4;

    /**
     * http://stackoverflow.com/questions/12711493/custom-formatted-number-with-period-and-comma
     * Commas are always used for grouping in the pattern definition, regardless of how they appear in the target locale.
     * Similarly, periods are always used for decimals. This appears illogical, but one must remember this is a pattern
     * definition, and like #, these characters describe a replacement, not the character itself.
     */
    private static String pattern = "#,##0.0#";
    private static DecimalFormat decimalFormat;
    static {
        /**
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
        bigDecimal.setScale(scale);
        return bigDecimal;
    }

    public static char getDecimalSeparator() {
        return decimalSeparator;
    }

    public static char getGroupingSeparator() {
        return groupingSeparator;
    }

    public static int getBigDecimalDefaultScale() {
        return bigDecimalScaleOf_2;
    }
}
