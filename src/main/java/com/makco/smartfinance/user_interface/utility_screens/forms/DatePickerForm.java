package com.makco.smartfinance.user_interface.utility_screens.forms;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

/**
 * Created by mcalancea on 2016-04-19.
 */
public class DatePickerForm {
    private final static Logger LOG = LogManager.getLogger(DatePickerForm.class);
    private final DatePicker datePicker;
    private final String pattern = "yyyy-MM-dd";

    public final static String USER_FRIENDLY_PATTERN = "YYYY-MM-DD";

    public DatePickerForm() {
        this.datePicker = new DatePicker();
        this.datePicker.setPromptText(USER_FRIENDLY_PATTERN); //human readable, not a java developer
        this.datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate localDate) {
                if(localDate != null) {
                    return formatter.format(localDate);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if(!StringUtils.isBlank(string)) {
                    return LocalDate.parse(string, formatter);
                } else {
                    return null;
                }
            }
        });

        datePicker.editorProperty().addListener((observable, oldValue, newValue) -> {
            LOG.debug("datePicker.editorProperty()->newValue == null:" + (newValue == null));
        });
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public ObjectProperty<LocalDate> valueProperty() {
        return datePicker.valueProperty();
    }

    public void requestFocus() {
        datePicker.requestFocus();
    }

    public LocalDate getValue() {
        return datePicker.getValue();
    }

    public ReadOnlyObjectProperty<TextField> editorProperty() {
        return datePicker.editorProperty();
    }
}
