package com.makco.smartfinance.javafx.control;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mcalancea on 2016-05-16.
 */
//http://stackoverflow.com/questions/30882634/what-is-wrong-with-this-javafx-fxml-custom-component
//todo custom control http://docs.oracle.com/javafx/2/fxml_get_started/custom_control.htm
public class FilterableComboBox extends ComboBox<String> {
//    private final static Logger LOG = LogManager.getLogger(FilterableComboBox.class);

    private ObservableList<String> initialList;
    private ObservableList<String> bufferList = FXCollections.observableArrayList();
    private String previousValue = "";

    public FilterableComboBox() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "filterable_combo_box.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    public FilterableComboBox(ObservableList<String> items) {
        super(items);
        super.setEditable(true);
        this.initialList = items;

        this.configAutoFilterListener();
    }

    //http://stackoverflow.com/questions/19010619/javafx-filtered-combobox
    private void configAutoFilterListener() {
        final FilterableComboBox currentInstance = this;
        this.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                previousValue = oldValue;
                final TextField editor = currentInstance.getEditor();
                final String selected = currentInstance.getSelectionModel().getSelectedItem();

//                LOG.debug(">>>observable:"+observable);
//                LOG.debug(">>>oldValue:"+oldValue);
//                LOG.debug(">>>newValue:"+newValue);
//                LOG.debug(">>>selected:"+selected);
                if (selected == null || !selected.equals(newValue)) {
                    filterItems(newValue, currentInstance);
                    currentInstance.show();
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
                    bufferList = FilterableComboBox.this.readFromList(filter, initialList);
                }else {
                    bufferList.clear();
                    bufferList.addAll(filterString(filter));
                }

                comboBox.setItems(bufferList);
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
        for (String string : initialList) {
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