package com.makco.smartfinance.utils;

import com.makco.smartfinance.utils.collection.DequeStack;
import com.makco.smartfinance.utils.collection.Stack;

/**
 * Created by mcalancea on 21 Jun 2016.
 */
public class ReversePolishNotation {
    private Stack<Character> characterStack = new DequeStack<>();

    public static String convertToReversePolishNotation(String arithmeticNotation){
        StringBuilder reversePolishNotation = new StringBuilder();
        Stack<Character> characterStack = new DequeStack<>();
//        arithmeticNotation.chars().forEach(intConsumer -> {
        char[] chars = arithmeticNotation.toCharArray();
        for (int i = 0; i < chars.length; i++) {

            System.out.println(">>intConsumer:" + intConsumer);
            switch ((char) intConsumer) {
                case '+':
                case '-':
                case '*':
                case '/':
                case '(':
                    characterStack.push(Character.valueOf((char) intConsumer));
                    break;
//                case '*':
//                case '/':
//                    reversePolishNotation.append(intConsumer);
//                    break;
                case ')':
                    reversePolishNotation.append(characterStack.pop().toString());
                    break;
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '0':
                    reversePolishNotation.append(intConsumer);
                    break;
            }
//        });
        }
        System.out.println(">>>characterStack: " + characterStack);
        return reversePolishNotation.toString();
    }

}
