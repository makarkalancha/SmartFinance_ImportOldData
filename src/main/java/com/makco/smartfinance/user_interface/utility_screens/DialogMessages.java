package com.makco.smartfinance.user_interface.utility_screens;

import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.user_interface.utility_screens.forms.DatePickerForm;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * User: Makar Kalancha
 * Date: 06/04/2016
 * Time: 23:37
 */
public class DialogMessages {
    private final static Logger LOG = LogManager.getLogger(DialogMessages.class);

    public static boolean showConfirmationDialog(String title, String headerText, String contentText, String ico){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        ico = (StringUtils.isBlank(ico)) ? UserInterfaceConstants.MAIN_WINDOW_ICO : ico;
        alertStage.getIcons().add(new Image(ico));

        if(!StringUtils.isBlank(title)){
            alert.setTitle(title);
        }
        if(!StringUtils.isBlank(headerText)){
            alert.setHeaderText(headerText);
        }
        if(!StringUtils.isBlank(contentText)){
            alert.setContentText(contentText);
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }

    public static void showErrorDialog(String headerText, EnumSet<ErrorEnum> errors, String ico){
        //http://stackoverflow.com/questions/27877547/styling-a-dialog-from-javafx-openjfx-dialogs-project
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        ico = (StringUtils.isBlank(ico)) ? UserInterfaceConstants.MAIN_WINDOW_ICO : ico;
        alertStage.getIcons().add(new Image(ico));
        alert.setTitle("Errors");
        if(!StringUtils.isBlank(headerText)){
            alert.setHeaderText(headerText);
        }

        if(!errors.isEmpty()) {
            String message = StringUtils.join(
                    errors.stream()
                            .map(error -> error.getMessage())
                            .collect(Collectors.toCollection(ArrayList::new)).toArray(),
                    "\n"
            );
            alert.setContentText(message);
        }
        alert.showAndWait();
    }

    public static void showExceptionAlert(Throwable t){
        LOG.error(t, t);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(UserInterfaceConstants.MAIN_WINDOW_ICO));
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Exception Dialog");
        alert.setContentText(t.getMessage());

//            Exception ex = new FileNotFoundException("Could not find file blabla.txt");

// Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    public static Optional<LocalDate> showDateUnitConfirmationDialog() {
        //create custom dialog
        Dialog<LocalDate> dialog = new Dialog<>();
        dialog.setTitle("Fill dates");
        dialog.setHeaderText("Fill dates for reports in application");

        //set icons
        Stage alertStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(UserInterfaceConstants.MAIN_WINDOW_ICO));
        dialog.setGraphic(new ImageView(UserInterfaceConstants.CALENDAR));

        //set buttons
        ButtonType applyBtnTp = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(applyBtnTp, ButtonType.CANCEL);

        Label content = new Label("Choose start date for reporting (" + DatePickerForm.USER_FRIENDLY_PATTERN + "):");
        DatePickerForm startDate = new DatePickerForm();
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(content, startDate.getDatePicker());

        //Enable/Disable Apply button depending on whether a date was entered
        Node applyBtn = dialog.getDialogPane().lookupButton(applyBtnTp);
        applyBtn.setDisable(true);

        //do some validation
        startDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            LOG.debug("startDate.valueProperty()->newValue == null:" + (newValue == null));
            applyBtn.setDisable(newValue == null);
        });
        startDate.editorProperty().addListener((observable, oldValue, newValue) -> {
            LOG.debug("startDate.editorProperty()->newValue == null:" + (newValue == null));
            applyBtn.setDisable(newValue.getText().isEmpty());
        });


        dialog.getDialogPane().setContent(vb);
        Platform.runLater(() -> startDate.requestFocus());

        //convert the result to a LocalDateTime when Apply button is clicked
        dialog.setResultConverter(dialogBtn -> {
            if (dialogBtn == applyBtnTp) {
                return startDate.getValue();
            }
            return null;
        });
        Optional<LocalDate> result = dialog.showAndWait();
//        result.ifPresent(localDateTime -> {
//            LOG.debug("localDateTime:" + localDateTime.toString());
//        });
        return result;
    }
}
