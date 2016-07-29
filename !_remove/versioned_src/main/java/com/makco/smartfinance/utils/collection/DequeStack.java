package com.makco.smartfinance.utils.collection;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * User: Makar Kalancha
 * Date: 11/04/2016
 * Time: 23:41
 */
//http://baddotrobot.com/blog/2013/01/10/stack-vs-deque/
public class DequeStack<T> implements Stack<T> {
    private final static Logger LOG = LogManager.getLogger(DequeStack.class);
    private int sizeLimit = 0;
    private int currentSize = 0;
    private Deque<T> deque = new ArrayDeque<>();

    public DequeStack(){

    }

    public DequeStack(int sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    private boolean isLimitGTZero() {
        return sizeLimit > 0;
    }

    @Override
    public void push(T t) {
//        LOG.debug("push->sizeLimit:" + sizeLimit + "; currentSize:" + currentSize + "; size:" + deque.size());
        if (isLimitGTZero() && sizeLimit <= currentSize) {
            deque.removeLast();
        } else {
            ++currentSize;
        }
        deque.addFirst(t);
    }

    @Override
    public T pop() {
//        LOG.debug("pop->sizeLimit:" + sizeLimit + "; currentSize:" + currentSize + "; size:" + deque.size());
        if(currentSize > 0) {
            --currentSize;
            return deque.removeFirst();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
//        return deque.isEmpty();
        return currentSize == 0;
    }

    @Override
    public int size() {
//        return deque.size();
        return currentSize;
    }

    @Override
    public void clear() {
//        LOG.debug("pop->sizeLimit:" + sizeLimit + "; currentSize:" + currentSize + "; size:" + deque.size());
        currentSize = 0;
        deque.clear();
    }

    @Override
    public T peek() {
        return deque.peek();
    }

    @Override
    public String toString() {
        return deque.toString();
    }

}
