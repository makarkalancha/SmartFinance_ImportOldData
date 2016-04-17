package com.makco.smartfinance.user_interface.undoredo;

import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.utils.collection.DequeStack;
import com.makco.smartfinance.utils.collection.Stack;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: Makar Kalancha
 * Date: 11/04/2016
 * Time: 23:30
 */
public class CareTaker{
    private final static Logger LOG = LogManager.getLogger(CareTaker.class);
    private Stack<Memento> undoStates = new DequeStack<>(128);
    private Stack<Memento> redoStates = new DequeStack<>(128);

//    http://blog.netopyr.com/2012/02/02/creating-read-only-properties-in-javafx/
//    private SimpleIntegerProperty undoSize = new SimpleIntegerProperty();
//    private SimpleIntegerProperty redoSize = new SimpleIntegerProperty();
    private Memento currentState;

    private BooleanProperty isUndoEmpty = new SimpleBooleanProperty(true);
    private BooleanProperty isRedoEmpty = new SimpleBooleanProperty(true);

    public void clear(){
        try {
            undoStates.clear();
            redoStates.clear();
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void log(String source){
        try{
            LOG.debug("======================================================");
            LOG.debug("UndoCakeTaker." + source + "->currentState:" + currentState);
            LOG.debug("UndoCakeTaker." + source + "->undoStates:" + undoStates);
            LOG.debug("UndoCakeTaker." + source + "->redoStates:" + redoStates);
            LOG.debug("======================================================");
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }

    private void setStatesSizes(){
        try{
            isUndoEmpty.set(undoStates.isEmpty());
            isRedoEmpty.set(redoStates.isEmpty());
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }

    public BooleanProperty isUndoEmptyProperty() {
        return isUndoEmpty;
    }

    public BooleanProperty isRedoEmptyProperty() {
        return isRedoEmpty;
    }

    public void saveState(Memento state){
        try{
            log("saveState: before if");
            if(currentState != null) {
                undoStates.push(currentState);
                redoStates.clear();
            }
            currentState = state;
            log("saveState: after if");

            setStatesSizes();
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }

    public Memento undoState(){
        Memento result = null;
        try{
            log("undoState: before if");
            if(!undoStates.isEmpty()){
                redoStates.push(currentState);
                currentState = undoStates.pop();
                result = currentState;
                LOG.debug("getState from UndoCakeTaker.undoState");
            }
            log("undoState: after if");
            setStatesSizes();
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return result;
    }

    public Memento redoState(){
        Memento result = null;
        try{
            log("redoState: before if");
            if(!redoStates.isEmpty()){
                undoStates.push(currentState);
                currentState = redoStates.pop();
                LOG.debug("getState from UndoCakeTaker.redoState");
                result = currentState;
            }
            log("redoState: after if");
            setStatesSizes();
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return result;
    }
}
