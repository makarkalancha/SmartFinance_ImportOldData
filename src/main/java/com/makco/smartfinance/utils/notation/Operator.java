package com.makco.smartfinance.utils.notation;

import com.makco.smartfinance.utils.BigDecimalUtils;

import java.math.BigDecimal;

/**
 * Created by mcalancea on 23 Jun 2016.
 */
public interface Operator {
    String TYPES = "/*-+";
    String getOperatorSymbol();
    int getPrecedence();
    boolean isUnaryOperator();
    BigDecimal evaluate(BigDecimal first, BigDecimal second);
}
