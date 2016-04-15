package com.makco.smartfinance.user_interface.unredo;

import com.makco.smartfinance.utils.collection.DequeStack;
import com.makco.smartfinance.utils.collection.Stack;

/**
 * User: Makar Kalancha
 * Date: 11/04/2016
 * Time: 23:30
 */
public class CareTaker{
    private Stack<Memento> undoStates = new DequeStack<>(3);
    private Stack<Memento> redoStates = new DequeStack<>(3);

//    http://blog.netopyr.com/2012/02/02/creating-read-only-properties-in-javafx/
//    private SimpleIntegerProperty undoSize = new SimpleIntegerProperty();
//    private SimpleIntegerProperty redoSize = new SimpleIntegerProperty();
    private Memento currentState;

    public void clear(){
        undoStates.clear();
        redoStates.clear();
    }

//    public UndoFormCakeTaker(){
//        undoSize.
//    }

//    public int getUndoSize() {
//        return undoSize.get();
//    }
//
//    public SimpleIntegerProperty undoSizeProperty() {
//        return undoSize;
//    }
//
//    public int getRedoSize() {
//        return redoSize.get();
//    }
//
//    public SimpleIntegerProperty redoSizeProperty() {
//        return redoSize;
//    }

    public boolean isUndoEmpty() {
        boolean a = undoStates.isEmpty();
        System.out.println("isUndoEmpty:" + a);
        return undoStates.isEmpty();
    }

    public boolean isRedoEmpty() {
        boolean a = redoStates.isEmpty();
        System.out.println("isRedoEmpty:" + a);
        return redoStates.isEmpty();
    }

    private void log(String source){
        System.out.println("======================================================");
        System.out.println("UndoCakeTaker." + source + "->currentState:" + currentState);
        System.out.println("UndoCakeTaker." + source + "->undoStates:" + undoStates);
        System.out.println("UndoCakeTaker." + source + "->redoStates:" + redoStates);
        System.out.println("======================================================");
    }

    public void saveState(Memento state){
        log("saveState: before if");
        if(currentState != null) {
            undoStates.push(currentState);
            redoStates.clear();
        }
        currentState = state;
        log("saveState: after if");
    }

    public Memento undoState(){
        log("undoState: before if");
//        Memento result = null;
//        if(currentState != null) {
//            redoStates.push(currentState);
//        }
        if(!undoStates.isEmpty()){
            redoStates.push(currentState);
            currentState = undoStates.pop();
            System.out.println("getState from UndoCakeTaker.undoState");
//            result = currentState;
        }
// else {
//            currentState = null;
//        }
        log("undoState: after if");
//        return result;
        return currentState;
    }

    public Memento redoState(){
        log("redoState: before if");
//        V result = null;

        if(!redoStates.isEmpty()){
//            if(currentState != null) {
                undoStates.push(currentState);
//            }
            currentState = redoStates.pop();
            System.out.println("getState from UndoCakeTaker.redoState");
//            result = currentState.getState();
        }
        log("redoState: after if");
        return currentState;
    }

//    private class SizeClass extends ReadOnlyIntegerPropertyBase {
//
//        private int size;
//
//        @Override
//        public final int get() {
//            return size;
//        }
//
//        private void set(int newValue) {
//            size = newValue;
//            fireValueChangedEvent();
//        }
//
//        @Override
//        public Object getBean() {
//            return UndoFormCakeTaker.this;
//        }
//
//        @Override
//        public String getName() {
//            return "size";
//        }
//    }
//
//    private SizeClass undoSize = new SizeClass();
//    private SizeClass redoSize = new SizeClass();
//
//    public final int getUndoSize() {
//        return undoSize.get();
//    }
//
//    public final int getRedoSize() {
//        return redoSize.get();
//    }
//
//    public final ReadOnlyIntegerProperty undoSizeProperty() {
//        return undoSize;
//    }
//
//    public final ReadOnlyIntegerProperty redoSizeProperty() {
//        return redoSize;
//    }
}
