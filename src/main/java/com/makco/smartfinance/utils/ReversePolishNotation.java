package com.makco.smartfinance.utils;

import com.makco.smartfinance.utils.collection.DequeStack;
import com.makco.smartfinance.utils.collection.Stack;
import org.apache.commons.lang3.CharSetUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcalancea on 21 Jun 2016.
 */
public class ReversePolishNotation {
    private Stack<String> operandStack = new DequeStack<>();
    private List<String> operandOperatorList = new ArrayList<>();
    private String allowedCharacters = "/*-+()";
    private char decimalSeparator;
    private String arithmeticNotation;

    public ReversePolishNotation(String arithmeticNotation, char decimalSeparator){
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

                operandOperatorList.add(operand.toString());
                operand = new StringBuilder();

                operandOperatorList.add(Character.toString(charInString));

            } else {
                operand.append(charInString);
            }
        }
        operandOperatorList.add(operand.toString());
    }

    public String convertToReversePolishNotation(){
        convertStringToOperandList();
        System.out.println(operandOperatorList);

        StringBuilder reversePolishNotation = new StringBuilder();
        for (int i = 0; i < operandOperatorList.size(); i++) {
            String element = operandOperatorList.get(i);
            if (element.equals("+") ||
                    element.equals("-") ||
                    element.equals("*") ||
                    element.equals("/") ||
                    element.equals("(")
                    ) {
                operandStack.push(element);
            } else if (element.equals(")")) {
                if (!operandStack.isEmpty() && operandStack.peek().equals("(")) {
                    operandStack.pop(); //remove "("
                }
                if (!operandStack.isEmpty() && !operandStack.peek().equals("(")) {
                    reversePolishNotation.append(operandStack.pop());
                }
            } else {
                reversePolishNotation.append(element);

                if (!operandStack.isEmpty() && operandOperatorList.size() > (i + 1)) {
                    String nextOperand = operandOperatorList.get(i + 1);
                    if (operandStack.peek().equals("+") || operandStack.peek().equals("-")) {
                        if (!nextOperand.equals("(") &&
                                !nextOperand.equals("*") &&
                                !nextOperand.equals("/")) {
                            reversePolishNotation.append(operandStack.pop());
                        }
                    }else if (!nextOperand.equals("(") && (operandStack.peek().equals("*") || operandStack.peek().equals("/"))) {
                        reversePolishNotation.append(operandStack.pop());
                    }
                }
            }

            if (i == (operandOperatorList.size() - 1)) {
                while(!operandStack.isEmpty()){
                    reversePolishNotation.append(operandStack.pop());
                }
            }
        }
        System.out.println(">>>reversePolishNotation: " + reversePolishNotation.toString());
        return reversePolishNotation.toString();
    }

}
