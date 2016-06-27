package com.makco.smartfinance.utils.notation;

/**
 * Created by mcalancea on 23 Jun 2016.
 */
public interface Operator {
    String TYPES = "/*-+";
    String getOperatorSymbol();
    int getPrecedence();
    boolean isUnaryOperator();
}
