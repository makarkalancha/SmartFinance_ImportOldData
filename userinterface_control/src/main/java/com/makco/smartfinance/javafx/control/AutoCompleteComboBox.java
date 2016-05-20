package com.makco.smartfinance.javafx.control;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.IndexRange;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mcalancea on 2016-05-18.
 */

/**
 * version 2
 *
 * http://stackoverflow.com/questions/19924852/autocomplete-combobox-in-javafx
 * not UPDATE: If you use java8u60 or above, use this:
 *
 * this is "I found a solution that's working for me:"
 */

/**
 * todo buggy home / end button
 */
public class AutoCompleteComboBox extends ComboBox<String> implements EventHandler<KeyEvent> {
    private StringBuilder sb;
    private int lastLength;
    private List<String> initialList = new ArrayList<>();
    private List<String> bufferList = new ArrayList<>();


    public AutoCompleteComboBox() {
        this.configAutoFilterListener();
    }

    public AutoCompleteComboBox(ObservableList<String> items) {
        super(items);
        this.configAutoFilterListener();
    }

    private void configAutoFilterListener(){
        sb = new StringBuilder();
        setEditable(true);
        setOnKeyReleased(AutoCompleteComboBox.this);

        //add a focus listener such that if not in focus, reset the filtered typed keys
        getEditor().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    // in focus
                    System.out.println(">>>changed if: in focus->newValue=" + newValue);
                } else {
                    System.out.println(">>>changed else: NO focus");
                    lastLength = 0;
                    sb.delete(0, sb.length());
                    selectClosestResultBasedOnTextFieldValue(false, false);
                    hide();
                }
            }
        });

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectClosestResultBasedOnTextFieldValue(true, true);
            }
        });
    }

    @Override
    public void handle(KeyEvent event) {
//        if(event.getCode() == KeyCode.HOME
//                || event.getCode() == KeyCode.END){
//            hide();
//        }

        /**
         * this variable is used to bypass the auto complete process if the length is the same.
         * this occurs if user types fast, the length of textField will record after the user
         * has typed after a certain delay.
         */
        if(lastLength != (getEditor().getLength() - getEditor().getSelectedText().length())){
            lastLength = getEditor().getLength() - getEditor().getSelectedText().length();
            System.out.println(">>>handle lastLengthn: " + lastLength);
            if(event.getCode() == KeyCode.BACK_SPACE
                    || event.getCode() == KeyCode.DELETE){
                filterItems(getEditor().getText());
            }
        }

        System.out.println(">>>handle button pressed: " + event.getCode());
        System.out.println(">>>handle sb before pressed button: " + sb.toString());
        System.out.println(">>>handle editorText before pressed button: " + getEditor().getText());
        //todo somewhere here buttons home,end,delete, backspace bug
        if (event.isControlDown()
                || event.getCode() == KeyCode.BACK_SPACE
                || event.getCode() == KeyCode.RIGHT
                || event.getCode() == KeyCode.UP
                || event.getCode() == KeyCode.LEFT
                || event.getCode() == KeyCode.DOWN
                || event.getCode() == KeyCode.DELETE
                || event.getCode() == KeyCode.INSERT
//                || event.getCode() == KeyCode.HOME
//                || event.getCode() == KeyCode.END
                || event.getCode() == KeyCode.PAGE_UP
                || event.getCode() == KeyCode.PAGE_DOWN
                || event.getCode() == KeyCode.PRINTSCREEN
                || event.getCode() == KeyCode.SCROLL_LOCK
                || event.getCode() == KeyCode.PAUSE
//                || event.getCode() == KeyCode.TAB
                || event.getCode() == KeyCode.CONTROL
                || event.getCode() == KeyCode.ALT
                || event.getCode() == KeyCode.ALT_GRAPH
                || event.getCode() == KeyCode.SHIFT
                || event.getCode() == KeyCode.CAPS
                || event.getCode() == KeyCode.NUM_LOCK
                || event.getCode() == KeyCode.WINDOWS
                || event.getCode() == KeyCode.CONTEXT_MENU
                ) {
            return;
        }

        IndexRange indexRange = getEditor().getSelection();
        sb.delete(0, sb.length());
        sb.append(getEditor().getText());
        //remove selected string index until end so only unselected text will be recorded
        try{
            sb.delete(indexRange.getStart(), sb.length());
        }catch (Exception e){
            throw new RuntimeException("AutoCompleteComboBox.handle",e);
        }

        System.out.println(">>>filterItems from handle: sb=" + sb.toString());
        filterItems(sb.toString());

        ObservableList<String> items = getItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).toLowerCase().startsWith(getEditor().getText().toLowerCase())) {
                try {
                    System.out.println(">>>handle->setText: (sb.toString() + items.get(i).substring(sb.toString().length()))="
                            + (sb.toString() + items.get(i).substring(sb.toString().length())));
                    getEditor().setText(sb.toString() + items.get(i).substring(sb.toString().length()));
                } catch (Exception e) {
//                    getEditor().setText(sb.toString());
                    throw new RuntimeException("AutoCompleteComboBox.handle: filtering", e);
                }
                getEditor().positionCaret(sb.toString().length());
                getEditor().selectEnd();
                break;
            }
        }
    }

    /**
     * selectClosestResultBasedOnTextFieldValue() - selects the item and scrolls to it when the popup is shown
     * @param affect - true if combobox is clicked to show popup so text and caret position will be readjusted
     * @param inFocus - true if combobox has focus. If not, programatically press enter key to add new entry to list
     */
    private void selectClosestResultBasedOnTextFieldValue(boolean affect, boolean inFocus){
        System.out.println(String.format(">>>selectClosestResultBasedOnTextFieldValue:affect=%b, inFocus=%b",affect, inFocus));
        System.out.println(">>>getEditor().getText():" + getEditor().getText());
        System.out.println(">>>sb:" + sb.toString());

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
        System.out.println(">>>Found? " + found);
//        System.out.println(">>>filterItems from selectClosestResultBasedOnTextFieldValue: getText=" + getEditor().getText());
        filterItems(getEditor().getText());
        if(!found && affect){
            System.out.println(">>>selectClosestResultBasedOnTextFieldValue->if(!found && affect){: s=" + s);
            getSelectionModel().clearSelection();
            getEditor().setText(s);
            getEditor().end();
        }

        if(!found){
            System.out.println(">>>selectClosestResultBasedOnTextFieldValue->if(!found){: null");
            getEditor().setText(null);
            getSelectionModel().select(null);
            setValue(null);
        }

        if (!inFocus
                && getEditor().getText() != null
                && getEditor().getText().trim().length() > 0) {

            System.out.println(">>>selectClosestResultBasedOnTextFieldValue->!inFocus && getEditor().getText() != null && getEditor().getText().trim().length() > 0) {: getEditor().getText()="+
                    getEditor().getText()
            );
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
//        System.out.println(">>>selectClosestResultBasedOnTextFieldValue->end of method: getText=" + getEditor().getText());
    }

    private List<String> getInitialList(){
        if(initialList.isEmpty()){
            initialList.addAll(getItems());
        }
        return initialList;
    }

    private List<String> filterString(String filter){
        System.out.println(">>>filterString->filter=" + filter);
        List<String> result = new ArrayList<>();
        StringBuilder regex = new StringBuilder();
        /**
         * accidently pasted "Platform.runLater(new Runnable() {" and regex crashes because of "("
         * unit test for regex
         * Exception in thread "JavaFX Application Thread" java.util.regex.PatternSyntaxException: Unclosed group near index xx
         * j.*a.*(.*
         * in order to escape special characters:
         * \Q -> Nothing, but quotes all characters until \E
         * \E -> Nothing, but ends quoting started by \Q
         */
        for (int i = 0; i < filter.length(); i++) {
            regex.append("\\Q");
            regex.append(filter.charAt(i));
            regex.append("\\E.*");
        }

        Pattern pattern = Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE);
        for (String string : getInitialList()) {
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()) {
                result.add(string);
            }
        }
        System.out.println(">>>filterString->result=" + result);
        return result;
    }

    private void filterItems(String filter) {
        System.out.println(">>>filterItems->filter=" + filter);
        bufferList.clear();
        if (StringUtils.isEmpty(filter)) {
            bufferList.addAll(getInitialList());
        } else {
            bufferList.addAll(filterString(filter));
        }
        setItems(FXCollections.observableArrayList(bufferList));
//        hide();
        /**
         * http://stackoverflow.com/questions/23094062/javafx-combobox-not-refreshing-the-number-of-visible-rows
         *
         * The bug submited is fixes, unfortunally in jdk8u66 sometimes the problem still happens with the test case attached to the bug
         */
        setVisibleRowCount(bufferList.size());
        //new added
        show();
        System.out.println(">>>filterItems->bufferList=" + bufferList);
    }
}
