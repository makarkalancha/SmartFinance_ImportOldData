package com.makco.smartfinance.utils.notation.operator;

import com.makco.smartfinance.utils.notation.Operator;

import java.math.BigDecimal;

/**
 * Created by Makar Kalancha on 23 Jun 2016.
 */
public class Add implements Operator {
    @Override
    public String getOperatorSymbol() {
        return "+";
    }

    @Override
    public int getPrecedence() {
        return 1;
    }

    @Override
    public boolean isUnaryOperator() {
        return true;
    }

    @Override
    public BigDecimal evaluate(BigDecimal first, BigDecimal second) {
        return first.add(second);
    }
}
