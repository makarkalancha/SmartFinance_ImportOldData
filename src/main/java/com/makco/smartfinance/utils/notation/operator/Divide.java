package com.makco.smartfinance.utils.notation.operator;

import com.makco.smartfinance.utils.notation.Operator;

/**
 * Created by mcalancea on 23 Jun 2016.
 */
public class Divide implements Operator {
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
}
