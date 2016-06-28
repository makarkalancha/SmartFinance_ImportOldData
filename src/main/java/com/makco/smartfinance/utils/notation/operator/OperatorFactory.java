package com.makco.smartfinance.utils.notation.operator;

import com.makco.smartfinance.utils.notation.Operator;

/**
 * Created by mcalancea on 23 Jun 2016.
 */
public class OperatorFactory {
    public static Operator buildOperator(String operator, int roundScale){
        if(operator.equals("+")){
            return new Add();
        } else if(operator.equals("-")){
            return new Substract();
        } else if(operator.equals("*")){
            return new Multiply();
        } else if(operator.equals("/")){
            return new Divide(roundScale);
        }
        return null;
    }
}
