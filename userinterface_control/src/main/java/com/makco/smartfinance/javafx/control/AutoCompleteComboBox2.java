package com.makco.smartfinance.javafx.control;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.text.StrBuilder;

/**
 * Created by mcalancea on 2016-05-18.
 */

/**
 * http://stackoverflow.com/questions/19924852/autocomplete-combobox-in-javafx
 * not UPDATE: If you use java8u60 or above, use this:
 *
 * this is "I found a solution that's working for me:"
 * //TODO finish this
 */
public class AutoCompleteComboBox2 extends ComboBox<String>/* implements EventHandler<KeyEvent> */{
    private StringBuilder sb;
    private int lastLength;


    public AutoCompleteComboBox2() {
        this.configAutoFilterListener();
    }

    public AutoCompleteComboBox2(ObservableList<String> items) {
        super(items);
        this.configAutoFilterListener();
    }

    private void configAutoFilterListener(){
        sb = new StringBuilder();
        setEditable(true);
        setOnKeyReleased(event -> );

        //add a focus listener such that if not in focus, reset the filtered typed keys
        getEditor().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    // in focus
                } else {
                    lastLength = 0;
                    sb.delete(0, sb.length());
                    selectClosestResultBasedOnTextFieldValue(false, false);
                }
            }
        });
    }

    @Override
    public void handle(KeyEvent event) {

    }

    /**
     * selectClosestResultBasedOnTextFieldValue() - selects the item and scrolls to it when the popup is shown
     * @param affect - true if combobox is clicked to show popup so text and caret position will be readjusted
     * @param inFocus - true if combobox has focus. If not, programatically press enter key to add new entry to list
     */
    private void selectClosestResultBasedOnTextFieldValue(boolean affect, boolean inFocus){
        ObservableList<String> items = getItems();
        boolean found = false;
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i) != null
                    && getEditor().getText() != null
                    && getEditor().getText().equals(items.get(i))) {

                try{
                    ListView<String> lv = ((ComboBoxListViewSkin) getSkin()).getListView();
                    lv.getSelectionModel().clearAndSelect(i);
                    lv.scrollTo(lv.getSelectionModel().getSelectedIndex());
                    found = true;
                    break;
                } catch (Exception ignored){

                }
            }
        }

        String s = getEditor().getText();
        System.out.println(">>>Found? "+found);
        if(!found && affect){
            getSelectionModel().clearSelection();
            getEditor().setText(s);
            getEditor().end();
        }

        if(!found){
            getEditor().setText(null);
            getSelectionModel().select(null);
            setValue(null);
        }

        if (!inFocus
                && getEditor().getText() != null
                && getEditor().getText().trim().length() > 0) {
            //press enter key programmatically to have this entry added
//            KeyEvent ke = new KeyEvent(this,
//                    KeyCode.ENTER.toString(),
//                    KeyCode.ENTER.getName(),
//                    KeyCode.ENTER.impl_getCode(),
//                    false,
//                    false,
//                    false,
//                    false,
//                    KeyEvent.KEY_RELEASED
//                    );
            KeyEvent ke = new KeyEvent(KeyEvent.KEY_RELEASED,
                    KeyCode.ENTER.toString(),
                    KeyCode.ENTER.toString(),
                    KeyCode.ENTER,
                    false,
                    false,
                    false,
                    false
            );
            fireEvent(ke);
        }

    }
}
