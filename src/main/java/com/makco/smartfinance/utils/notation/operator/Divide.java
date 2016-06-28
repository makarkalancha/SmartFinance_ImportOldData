package com.makco.smartfinance.utils.notation.operator;

import com.makco.smartfinance.utils.notation.Operator;

import java.math.BigDecimal;

/**
 * Created by mcalancea on 23 Jun 2016.
 */
public class Divide implements Operator{
    private final int scale;

    public Divide(int scale){
        this.scale = scale;
    }

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
        return first.divide(second, scale, BigDecimal.ROUND_HALF_UP);
    }
}
