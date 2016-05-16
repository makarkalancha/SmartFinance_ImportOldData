package com.makco.smartfinance.utils.collection;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 13/04/2016
 * Time: 21:39
 */
public class DequeStackTest {

    private Stack<Integer> initializeUnlimitedStack(){
        Stack<Integer> integerStack = new DequeStack<>();
        integerStack.push(1);
        integerStack.push(2);
        integerStack.push(3);
        integerStack.push(4);
        return integerStack;
    }

    @Test
    public void testPush() throws Exception {
        Stack<Integer> integerStack = new DequeStack<>();
        integerStack.push(1);
        assertEquals(new Integer(1), integerStack.peek());
        integerStack.push(2);
        assertEquals(new Integer(2), integerStack.peek());
    }

    @Test
    public void testPop() throws Exception {
        Stack<Integer> stack = initializeUnlimitedStack();
        int four = stack.pop();
        assertEquals(4, four);
    }

    @Test
    public void testPeek() throws Exception {
        Stack<Integer> stack = initializeUnlimitedStack();
        int four = stack.pop();
        assertEquals(4, four);
        int three1 = stack.peek();
        assertEquals(3, three1);
        int three2 = stack.peek();
        assertEquals(3, three2);
    }

    @Test
    public void testIsEmpty() throws Exception {
        Stack<Integer> stack = initializeUnlimitedStack();
        int four = stack.pop();
        assertEquals(false, stack.isEmpty());
        int three = stack.pop();
        assertEquals(false, stack.isEmpty());
        int two = stack.pop();
        assertEquals(false, stack.isEmpty());
        int one = stack.pop();
        assertEquals(true, stack.isEmpty());
    }

    @Test
    public void testClear() throws Exception {
        Stack<Integer> stack = initializeUnlimitedStack();
        int four = stack.pop();
        assertEquals(false, stack.isEmpty());
        int three = stack.pop();
        assertEquals(false, stack.isEmpty());
        stack.clear();
        assertEquals(true, stack.isEmpty());
    }

    @Test
    public void testLimitedStack() throws Exception{
        Stack<Integer> stack = new DequeStack<>(3);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        assertEquals(new Integer(4), stack.pop());
        assertEquals(new Integer(3), stack.pop());
        assertEquals(new Integer(2), stack.pop());
        assertEquals(null, stack.pop());
        assertEquals(null, stack.pop());

        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        assertEquals(new Integer(4), stack.pop());
        assertEquals(new Integer(3), stack.pop());
        stack.clear();
        assertEquals(null, stack.pop());
        assertEquals(null, stack.pop());
        assertEquals(null, stack.pop());
    }

    @Test
    public void testUnlimitedStack() throws Exception{
        Stack<Integer> stack = new DequeStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        assertEquals(new Integer(4), stack.pop());
        assertEquals(new Integer(3), stack.pop());
        assertEquals(new Integer(2), stack.pop());
        assertEquals(new Integer(1), stack.pop());
        assertEquals(null, stack.pop());

        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        assertEquals(new Integer(4), stack.pop());
        assertEquals(new Integer(3), stack.pop());
        stack.clear();
        assertEquals(null, stack.pop());
        assertEquals(null, stack.pop());
        assertEquals(null, stack.pop());
    }
}