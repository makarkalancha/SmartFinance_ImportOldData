package com.makco.smartfinance.javafx.control;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mcalancea on 2016-05-16.
 */
/**
 * version 1
 *
 * autocomplete sources:
 * http://stackoverflow.com/questions/19924852/autocomplete-combobox-in-javafx
 * http://tech.chitgoks.com/2013/08/20/how-to-create-autocomplete-combobox-or-textfield-in-java-fx-2/
 */

/**
 * http://stackoverflow.com/questions/30882634/what-is-wrong-with-this-javafx-fxml-custom-component
 * http://docs.oracle.com/javafx/2/fxml_get_started/custom_control.htm
 *
 * https://jaxenter.com/netbeans/making-custom-javafx-controls-available-in-the-scene-builder
 * 1) make a build without any third-party library
 * 2) remove previous unsuccessful import of jar from C:\Users\mcalancea\AppData\Roaming\Scene Builder\Library\
 * 3) import just built jar
 *
 *  logger is not working
 */

/**
 * bug:
 * 1) type any letter
 * 2) choose anything from list (without pressing enter)
 * 3) move cursor so it is somewhere inside string (between start and end)
 * 4) press shift+end
 * 5) change listener keeps running even after app is closed:
 >>>observable:StringProperty [bean: ComboBoxListViewSkin$FakeFocusTextField@30d996e[styleClass=text-input text-field], name: text, value: jacob.smith@example.com]
 >>>oldValue:
 >>>newValue:jacob.smith@example.com
 >>>selected:jacob.smith@example.com
 >>>observable:StringProperty [bean: ComboBoxListViewSkin$FakeFocusTextField@30d996e[styleClass=text-input text-field], name: text, value: jaco(]
 >>>oldValue:jacob.smith@example.com
 >>>newValue:jaco(
 >>>selected:jacob.smith@example.com
 >>>filtering...
 >>>observable:StringProperty [bean: ComboBoxListViewSkin$FakeFocusTextField@30d996e[styleClass=text-input text-field], name: text, value: ]
 >>>oldValue:jaco(
 >>>newValue:
 >>>selected:null
 >>>filtering...
 >>>observable:StringProperty [bean: ComboBoxListViewSkin$FakeFocusTextField@30d996e[styleClass=text-input text-field], name: text, value: j]
 >>>oldValue:
 >>>newValue:j
 >>>selected:null
 >>>filtering...
 >>>observable:StringProperty [bean: ComboBoxListViewSkin$FakeFocusTextField@30d996e[styleClass=text-input text-field], name: text, value: jacob.smith@example.com]
 >>>oldValue:j
 >>>newValue:jacob.smith@example.com
 >>>selected:jacob.smith@example.com
 >>>observable:StringProperty [bean: ComboBoxListViewSkin$FakeFocusTextField@30d996e[styleClass=text-input text-field], name: text, value: ]
 >>>oldValue:jacob.smith@example.com
 >>>newValue:
 >>>selected:null
 >>>filtering...
 >>>observable:StringProperty [bean: ComboBoxListViewSkin$FakeFocusTextField@30d996e[styleClass=text-input text-field], name: text, value: emma(johnson)]
 >>>oldValue:
 >>>newValue:emma(johnson)
 >>>selected:emma(johnson)
 >>>observable:StringProperty [bean: ComboBoxListViewSkin$FakeFocusTextField@30d996e[styleClass=text-input text-field], name: text, value: ]
 >>>oldValue:emma(johnson)
 >>>newValue:
 >>>selected:null
 >>>filtering...
 >>>observable:StringProperty [bean: ComboBoxListViewSkin$FakeFocusTextField@30d996e[styleClass=text-input text-field], name: text, value: emma(johnson)]
 >>>oldValue:
 >>>newValue:emma(johnson)
 >>>selected:emma(johnson)
 >>>observable:StringProperty [bean: ComboBoxListViewSkin$FakeFocusTextField@30d996e[styleClass=text-input text-field], name: text, value: ]
 >>>oldValue:emma(johnson)
 >>>newValue:
 >>>selected:null
 >>>filtering...
 >>>observable:StringProperty [bean: ComboBoxListViewSkin$FakeFocusTextField@30d996e[styleClass=text-input text-field], name: text, value: emma(johnson)]
 >>>oldValue:
 >>>newValue:emma(johnson)
 >>>selected:emma(johnson)
 */
public class AutoCompleteComboBox1 extends ComboBox<String> {

    ObservableList<String> initialList = FXCollections.emptyObservableList();
    private ObservableList<String> bufferList = FXCollections.observableArrayList();

    public AutoCompleteComboBox1() {
        super.setEditable(true);
        this.configAutoFilterListener();
    }

    public AutoCompleteComboBox1(ObservableList<String> items) {
        super(items);
        super.setEditable(true);
        this.initialList = items;
        this.configAutoFilterListener();
    }

    private ObservableList<String> getInitialList(){
        if(initialList.isEmpty()){
            initialList = itemsProperty().get();
        }
        return initialList;
    }

    //http://stackoverflow.com/questions/19010619/javafx-filtered-combobox
    private void configAutoFilterListener() {
        final AutoCompleteComboBox1 currentInstance = this;
        this.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

/* --working block, except for bug above
                final String selected = currentInstance.getSelectionModel().getSelectedItem();

                if (selected == null || !selected.equals(newValue)) {
                    filterItems(newValue, currentInstance);
                    currentInstance.show();
                }
*/
                //                previousValue = oldValue;
//                final TextField editor = currentInstance.getEditor();
                final String selected = currentInstance.getSelectionModel().getSelectedItem();

//                String editorGetTextDebug = editor.getText();
//
//                if (selected == null || !selected.equals(editor.getText())) {
                System.out.println(">>>observable:"+observable);
                System.out.println(">>>oldValue:"+oldValue);
                System.out.println(">>>newValue:"+newValue);
                System.out.println(">>>selected:"+selected);
//                if (selected == null || !selected.equals(newValue)) {
                if (selected != null && !selected.equals(newValue)) {
                    System.out.println(">>>filtering...");
                    filterItems(newValue, currentInstance);

//                    if (currentInstance.getItems().size() == 1) {
//                        setUserInputToOnlyOption(currentInstance, editor);
//                    }
                }
            }
        });
    }

    private void filterItems(String filter, ComboBox<String> comboBox) {
        /**
         * https://community.oracle.com/thread/2474433
         *
         * works correctly if runLater in here
         * not working if currentInstance.show(); (from method configAutoFilterListener) is in runlater
         *
         * [http://stackoverflow.com/questions/13784333/platform-runlater-and-task-in-javafx]:
         * Use Platform.runLater(...) for quick and simple operations and Task for complex and big operations .
         */
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                if(StringUtils.isEmpty(filter)){
                    bufferList = AutoCompleteComboBox1.this.readFromList(filter, getInitialList());
                }else {
                    bufferList.clear();
                    bufferList.addAll(filterString(filter));
                }

                comboBox.setItems(bufferList);

                //new added
                comboBox.show();
            }
        });
    }

    private List<String> filterString(String filter){
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
        return result;
    }

    private ObservableList<String> readFromList(String filter, ObservableList<String> originalList) {
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        for (String item : originalList) {
            if (item.toLowerCase().startsWith(filter.toLowerCase())) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }

    private void setUserInputToOnlyOption(ComboBox<String> currentInstance, final TextField editor) {
        final String onlyOption = currentInstance.getItems().get(0);
        final String currentText = editor.getText();
        if (onlyOption.length() > currentText.length()) {
            editor.setText(onlyOption);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    editor.selectRange(currentText.length(), onlyOption.length());
                }
            });
        }
    }
}