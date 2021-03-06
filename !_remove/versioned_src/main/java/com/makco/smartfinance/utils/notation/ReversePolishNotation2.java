package com.makco.smartfinance.utils.notation;

import com.makco.smartfinance.utils.collection.DequeStack;
import com.makco.smartfinance.utils.collection.Stack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Makar Kalancha on 21 Jun 2016.
 * version 2
 */
public class ReversePolishNotation2 {
    private Stack<String> operatorStack = new DequeStack<>(); //operator +-... and operand 3, 2, 4, a, b, c ...
    private List<String> operandOperatorList = new ArrayList<>();
    private StringBuilder reversePolishNotation = new StringBuilder();
    private String allowedCharacters = "/*-+()";
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

    private void convertStringToOperandList(){
        StringBuilder operand = new StringBuilder();
        for (int i = 0; i < arithmeticNotation.length(); i++) {
            char charInString = arithmeticNotation.charAt(i);
            if (allowedCharacters.indexOf(charInString) > -1) {

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
    }

    private void getOperator(String operator, int precedence1){
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

                if(precedence1 > precedence2){
                    operatorStack.push(topOperator);
                    break;
                } else if(precedence1 <= precedence2){
                    reversePolishNotation.append(" ");
                    reversePolishNotation.append(topOperator);
                }
            }
        }

        operatorStack.push(operator);
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
        convertStringToOperandList();
        System.out.println(operandOperatorList);

        for (int i = 0; i < operandOperatorList.size(); i++) {
            String element = operandOperatorList.get(i);
            if (element.equals("+") || element.equals("-")){
                getOperator(element, 1);
            } else if(element.equals("*") || element.equals("/")){
                getOperator(element, 2);
            } else if (element.equals("(")) {
                operatorStack.push(element);
            } else if (element.equals(")")) {
                getParent();
            } else {
                if(reversePolishNotation.length() > 0) {
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
