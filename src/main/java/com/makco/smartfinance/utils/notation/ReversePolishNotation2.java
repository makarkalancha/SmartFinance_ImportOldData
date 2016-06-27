package com.makco.smartfinance.utils.notation;

import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.utils.collection.DequeStack;
import com.makco.smartfinance.utils.collection.Stack;
import com.makco.smartfinance.utils.notation.operator.OperatorFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 21 Jun 2016.
 */
public class ReversePolishNotation2 {
    private Stack<String> operatorStack = new DequeStack<>(); //operator +-... and operand 3, 2, 4, a, b, c ...
    private List<String> operandOperatorList = new ArrayList<>();
    private StringBuilder reversePolishNotation = new StringBuilder();
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
    private EnumSet<ErrorEnum> convertStringTooperandOperatorList(){
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            StringBuilder operand = new StringBuilder();
            for (int i = 0; i < arithmeticNotation.length(); i++) {
                char charInString = arithmeticNotation.charAt(i);
                if (allowedOperators.indexOf(charInString) > -1) {

                    if(i==0 && unaryOperators.indexOf(charInString) > -1){

                    }

                    if (i == (arithmeticNotation.length() - 1) && unaryOperators.indexOf(Operator.TYPES) > -1) {
                        errors.add(ErrorEnum.FRM_END);

                    }

                    if (operand.length() > 0) {
                        operandOperatorList.add(operand.toString());
                        operand = new StringBuilder();
                    }

                    operandOperatorList.add(Character.toString(charInString));

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
        convertStringTooperandOperatorList();
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

}
