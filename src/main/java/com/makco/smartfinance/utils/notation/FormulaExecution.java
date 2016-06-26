package com.makco.smartfinance.utils.notation;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by mcalancea on 23 Jun 2016.
 */

/*
todo read about formula evaluation
http://andreas.haufler.info/2013/12/how-to-write-one-of-fastest-expression.html
http://www.transylvania-jug.org/archives/5777
https://dzone.com/articles/even-faster-java-expression
https://udojava.com/2012/12/16/java-expression-parser-evaluator/

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
