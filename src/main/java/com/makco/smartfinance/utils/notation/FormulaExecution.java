package com.makco.smartfinance.utils.notation;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by mcalancea on 23 Jun 2016.
 */
public class FormulaExecution<T> {
    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    private final String arithmeticExpression;

    public FormulaExecution(String arithmeticExpression){
        this.arithmeticExpression = arithmeticExpression;
    }

    public String getArithmeticExpression() {
        return arithmeticExpression;
    }

    public T getResult() throws Exception{
        return (T) engine.eval(arithmeticExpression);
    }
}
