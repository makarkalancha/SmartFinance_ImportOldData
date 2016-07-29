package com.makco.smartfinance.utils.notation.operator;

import com.makco.smartfinance.utils.notation.Operator;

import java.math.BigDecimal;

/**
 * Created by Makar Kalancha on 23 Jun 2016.
 */
public class Multiply implements Operator {
    @Override
    public String getOperatorSymbol() {
        return "*";
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
        return first.multiply(second);
    }
}
