package com.makco.smartfinance.utils.notation;

import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.utils.collection.DequeStack;
import com.makco.smartfinance.utils.collection.Stack;
import com.makco.smartfinance.utils.notation.operator.OperatorFactory;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 21 Jun 2016.
 */
public class ReversePolishNotation2 {
    private Stack<String> operatorStack = new DequeStack<>(); //operator +-... and operand 3, 2, 4, a, b, c ...
    private Stack<BigDecimal> valueStack = new DequeStack<>();
    private List<String> operandOperatorList = new ArrayList<>();
    private StringBuilder reversePolishNotation = new StringBuilder();
    private BigDecimal resultBigDecimal = new BigDecimal(0);
    private String allowedOperators = "/*-+()";
    private String unaryOperators = "-+";
    private char decimalSeparator;
    private String arithmeticNotation;

    public ReversePolishNotation2(String arithmeticNotation, char decimalSeparator){
        this.arithmeticNotation = arithmeticNotation;
        this.decimalSeparator = decimalSeparator;
    }

    public char getDecimalSeparator() {
        return decimalSeparator;
    }

    public void setDecimalSeparator(char decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    public String getArithmeticNotation() {
        return arithmeticNotation;
    }

    public void setArithmeticNotation(String arithmeticNotation) {
        this.arithmeticNotation = arithmeticNotation;
    }

    /*
    todo make this method return errors, so it should validate formula
     */
    private EnumSet<ErrorEnum> convertStringToOperandOperatorList(){
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            StringBuilder operand = new StringBuilder();
            for (int i = 0; i < arithmeticNotation.length(); i++) {
                char charInString = arithmeticNotation.charAt(i);
                if (allowedOperators.indexOf(charInString) > -1) {

                    if (operand.length() > 0) {
                        operandOperatorList.add(operand.toString());
                        operand = new StringBuilder();
                    }

                    if(unaryOperators.indexOf(charInString) > -1 && (i==0 || unaryOperators.indexOf(operandOperatorList.get(operandOperatorList.size()-1)) > -1)){
                        operand.append(charInString);
                    } else {

//                    if (i == (arithmeticNotation.length() - 1) && unaryOperators.indexOf(Operator.TYPES) > -1) {
//                        errors.add(ErrorEnum.FRM_END);
//
//                    }



                        operandOperatorList.add(Character.toString(charInString));
                    }

                } else {
                    operand.append(charInString);
                }
            }

            if (operand.length() > 0) {
                operandOperatorList.add(operand.toString());
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }

    private void getOperator(Operator operator){
        while(!operatorStack.isEmpty()){
            String topOperator = operatorStack.pop();
            if(topOperator.equals("(")){
                operatorStack.push(topOperator);
                break;
            } else {
                int precedence2 = 0;
                if(topOperator.equals("+") || topOperator.equals("-")){
                    precedence2 = 1;
                }else if(topOperator.equals("/") || topOperator.equals("*")){
                    precedence2 = 2;
                }

                if(operator.getPrecedence() > precedence2){
                    operatorStack.push(topOperator);
                    break;
                } else if(operator.getPrecedence() <= precedence2){
                    reversePolishNotation.append(" ");
                    reversePolishNotation.append(topOperator);
                }
            }
        }

        operatorStack.push(operator.getOperatorSymbol());
    }

    public void getParent(){
        while(!operatorStack.isEmpty()){
            String topOperator = operatorStack.pop();
            if(!topOperator.equals("(")){
                reversePolishNotation.append(" ");
                reversePolishNotation.append(topOperator);
            }else {
                break;
            }
        }
    }

    public String convertToReversePolishNotation(){
        convertStringToOperandOperatorList();
        System.out.println(operandOperatorList);

        for (int i = 0; i < operandOperatorList.size(); i++) {
            String element = operandOperatorList.get(i);
            if (StringUtils.containsOnly(element, Operator.TYPES)) {
                Operator operator = OperatorFactory.buildOperator(element);
                getOperator(operator);
            } else if (element.equals("(")) {
                operatorStack.push(element);
            } else if (element.equals(")")) {
                getParent();
            } else {
                if (reversePolishNotation.length() > 0) {
                    reversePolishNotation.append(" ");
                }
                reversePolishNotation.append(element);
            }
        }
        while(!operatorStack.isEmpty()){
            reversePolishNotation.append(" ");
            reversePolishNotation.append(operatorStack.pop());
        }
        System.out.println(">>>reversePolishNotation: " + reversePolishNotation.toString());
        return reversePolishNotation.toString();
    }

    private void evaluateOperation(Operator operator){
        while(!operatorStack.isEmpty()){
            String topOperator = operatorStack.pop();
            if(topOperator.equals("(")){
                operatorStack.push(topOperator);
                break;
            } else {
                int precedence2 = 0;
                if(topOperator.equals("+") || topOperator.equals("-")){
                    precedence2 = 1;
                }else if(topOperator.equals("/") || topOperator.equals("*")){
                    precedence2 = 2;
                }

                if (operator.getPrecedence() > precedence2) {
                    operatorStack.push(topOperator);
                    break;
                } else if (operator.getPrecedence() <= precedence2) {
//                        reversePolishNotation.append(" ");
//                        reversePolishNotation.append(topOperator);
                    Operator topOperatorObj = OperatorFactory.buildOperator(topOperator);
                    BigDecimal second = valueStack.pop();
                    BigDecimal first = valueStack.pop();
                    BigDecimal result = topOperatorObj.evaluate(first, second);
                    valueStack.push(result);
                }
            }
        }

        operatorStack.push(operator.getOperatorSymbol());
    }

    public void getParentForEvaluation(){
        while(!operatorStack.isEmpty()){
            String topOperator = operatorStack.pop();
            if(!topOperator.equals("(")){
//                reversePolishNotation.append(" ");
//                reversePolishNotation.append(topOperator);

                Operator topOperatorObj = OperatorFactory.buildOperator(topOperator);
                BigDecimal second = valueStack.pop();
                BigDecimal first = valueStack.pop();
                BigDecimal result = topOperatorObj.evaluate(first, second);
                valueStack.push(result);
            }else {
                break;
            }
        }
    }

    public BigDecimal evaluateReversePolishNotation(){
        convertStringToOperandOperatorList();
        System.out.println(operandOperatorList);

        for (int i = 0; i < operandOperatorList.size(); i++) {
            String element = operandOperatorList.get(i);
            if (StringUtils.containsOnly(element, Operator.TYPES)) {
                Operator operator = OperatorFactory.buildOperator(element);
                evaluateOperation(operator);
            } else if (element.equals("(")) {
                operatorStack.push(element);
            } else if (element.equals(")")) {
                getParentForEvaluation();
            } else {
                valueStack.push(new BigDecimal(element));
            }
        }
        while(!operatorStack.isEmpty()){
//            reversePolishNotation.append(" ");
//            reversePolishNotation.append(operatorStack.pop());
            Operator topOperatorObj = OperatorFactory.buildOperator(operatorStack.pop());
            BigDecimal second = valueStack.pop();
            BigDecimal first = valueStack.pop();
            BigDecimal result = topOperatorObj.evaluate(first, second);
            valueStack.push(result);
        }

        if(valueStack.size() == 1){
            resultBigDecimal =  valueStack.pop();
        } else {
            throw new RuntimeException("value stack contains more elements than 1: " + valueStack);
        }

        System.out.println(">>>resultBigDecimal: " + resultBigDecimal);
        return resultBigDecimal;
    }

}
