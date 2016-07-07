package com.makco.smartfinance.utils.notation.operator;

import com.makco.smartfinance.utils.notation.Operator;

import java.math.BigDecimal;

/**
 * Created by mcalancea on 23 Jun 2016.
 */
public class Divide implements Operator{
//    private final int scale = Integer.MAX_VALUE;//smoke test takes tooooo long
    private final int scale = 10;

    @Override
    public String getOperatorSymbol() {
        return "/";
    }

    @Override
    public int getPrecedence() {
        return 2;
    }

    @Override
    public boolean isUnaryOperator() {
        return false;
    }

    @Override
    public BigDecimal evaluate(BigDecimal first, BigDecimal second) {
        //java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result.
        return first.divide(second, scale, BigDecimal.ROUND_HALF_UP);
    }
}
